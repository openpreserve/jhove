package edu.harvard.hul.ois.jhove.module;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.RepInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlModuleCacheTest {

    private static final String XML_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"%s\">\n" +
            "  <child>value</child>\n" +
            "</root>\n";

    private static final String XSD_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"child\" type=\"xs:string\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>\n";

    private HttpServer server;
    private ExecutorService serverExecutor;
    private Path tempDir;

    @Before
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("xml-cache-test");
    }

    @After
    public void teardown() throws IOException {
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (serverExecutor != null) {
            serverExecutor.shutdownNow();
            serverExecutor = null;
        }
        if (tempDir != null) {
            deleteRecursively(tempDir.toFile());
            tempDir = null;
        }
    }

    @Test
    public void shouldNotCacheSchemaWithoutCacheDirectory() throws Exception {
        AtomicInteger requestCount = new AtomicInteger(0);
        startHttpServer(requestCount, false);

        XmlModule module = createModule();
        String xml = String.format(XML_TEMPLATE, getSchemaUrl());

        validateDocument(module, xml);
        validateDocument(module, xml);

        assertEquals(2, requestCount.get());
    }

    @Test
    public void shouldCacheSchemaWithCacheDirectory() throws Exception {
        AtomicInteger requestCount = new AtomicInteger(0);
        startHttpServer(requestCount, false);

        XmlModule module = createModule();
        module.param("schemacachedirectory=" + tempDir.toFile().getAbsolutePath());
        String xml = String.format(XML_TEMPLATE, getSchemaUrl());

        validateDocument(module, xml);
        validateDocument(module, xml);

        assertEquals(1, requestCount.get());
        Path cachedSchema = tempDir.resolve("localhost").resolve("test.xsd");
        assertTrue(Files.exists(cachedSchema));
        assertTrue(Files.isRegularFile(cachedSchema));
        assertTrue(Files.notExists(tempDir.resolve("localhost").resolve("test.xsd.download")));
    }

    @Test
    public void shouldExpireVeryShortCache() throws Exception {
        AtomicInteger requestCount = new AtomicInteger(0);
        startHttpServer(requestCount, false);

        XmlModule module = createModule();
        module.param("schemacachedirectory=" + tempDir.toFile().getAbsolutePath());
        module.param("schemacacheexpiration=1");
        String xml = String.format(XML_TEMPLATE, getSchemaUrl());

        validateDocument(module, xml);
        // Allow the cached schema to expire before validating again.
        TimeUnit.MILLISECONDS.sleep(1100);
        validateDocument(module, xml);

        assertEquals(2, requestCount.get());
    }

    @Test
    public void shouldUsePermanentCacheWhenExpirationNotSet() throws Exception {
        AtomicInteger requestCount = new AtomicInteger(0);
        startHttpServer(requestCount, false);

        XmlModule module = createModule();
        module.param("schemacachedirectory=" + tempDir.toFile().getAbsolutePath());
        String xml = String.format(XML_TEMPLATE, getSchemaUrl());

        validateDocument(module, xml);
        validateDocument(module, xml);

        assertEquals(1, requestCount.get());
        Path cachedSchema = tempDir.resolve("localhost").resolve("test.xsd");
        assertTrue(Files.exists(cachedSchema));
        assertTrue(Files.isRegularFile(cachedSchema));
        assertTrue(Files.notExists(tempDir.resolve("localhost").resolve("test.xsd.download")));
    }

    @Test
    public void shouldProtectConcurrentSchemaDownloads() throws Exception {
        AtomicInteger requestCount = new AtomicInteger(0);
        startHttpServer(requestCount, true);

        int tasks = 3;
        ExecutorService executor = Executors.newFixedThreadPool(tasks);
        CountDownLatch startLatch = new CountDownLatch(1);
        String xml = String.format(XML_TEMPLATE, getSchemaUrl());

        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            futures.add(executor.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    startLatch.await();
                    XmlModule module = createModule();
                    module.param("schemacachedirectory=" + tempDir.toFile().getAbsolutePath());
                    String xmlContent = xml;
                    validateDocument(module, xmlContent);
                    return null;
                }
            }));
        }
        startLatch.countDown();

        for (Future<Void> future : futures) {
            future.get(10, TimeUnit.SECONDS);
        }

        executor.shutdownNow();
        assertEquals(1, requestCount.get());
        assertTrue(Files.notExists(tempDir.resolve("localhost").resolve("test.xsd.download")));
    }

    private XmlModule createModule() throws JhoveException {
        JhoveBase base = new JhoveBase();
        base.setSigBytes(1024);
        XmlModule module = new XmlModule();
        module.setBase(base);
        return module;
    }

    private void validateDocument(XmlModule module, String xml) throws IOException {
        RepInfo info = new RepInfo("uri:test");
        int parseIndex = module.parse(stream(xml), info, 0);
        assertEquals(1, parseIndex);
        parseIndex = module.parse(stream(xml), info, parseIndex);
        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
    }

    private InputStream stream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

    private void startHttpServer(AtomicInteger requestCount, boolean delayFirstRequest) throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        serverExecutor = Executors.newCachedThreadPool();
        server.setExecutor(serverExecutor);
        server.createContext("/test.xsd", new HttpHandler() {
            private volatile boolean firstRequest = true;

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (delayFirstRequest && firstRequest) {
                    firstRequest = false;
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
                requestCount.incrementAndGet();
                byte[] body = XSD_CONTENT.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/xml");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            }
        });
        server.start();
    }

    private String getSchemaUrl() {
        InetSocketAddress address = server.getAddress();
        return "http://localhost:" + address.getPort() + "/test.xsd";
    }

    private void deleteRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        Files.deleteIfExists(file.toPath());
    }
}
