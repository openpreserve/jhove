package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.RepInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class XmlModuleTest {

    private static final String RESOURCE_DIR = "src/test/resources/edu/harvard/hul/ois/jhove/module/";
    private static final String LOCAL_DC_SCHEMA_PATH = RESOURCE_DIR + "simpledc20021212.xsd";

    private static final String MODULE_NAME = "XML-hul";
    private static final int SIG_BYTES = 1024;

    private static final URI DC_SCHEMA_URI = URI.create("http://dublincore.org/schemas/xmls/simpledc20021212.xsd");

    private static final String DC_XML = "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"" +
            " xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" +
            "  <dc:identifier>1234:example</dc:identifier>\n" +
            "</oai_dc:dc>";

    private XmlModule module;

    @Before
    public void setup() throws JhoveException {
        JhoveBase base = new JhoveBase();
        base.setSigBytes(SIG_BYTES);
        module = new XmlModule();
        module.setBase(base);
    }

    @Test
    public void shouldDetectXmlWhenHasDeclarationAndNotWellFormed() throws IOException {
        File file = new File(RESOURCE_DIR + "not-well-formed.xml");
        RepInfo info = new RepInfo(file.toURI().toString());

        module.checkSignatures(file, new FileInputStream(file), info);

        assertEquals(1, info.getSigMatch().size());
        assertEquals(MODULE_NAME, info.getSigMatch().get(0));
        assertEquals(RepInfo.TRUE, info.getWellFormed());
    }

    @Test
    public void validateXmlSucceedsWhenHasRootSchema() {
        RepInfo info = new RepInfo("uri:test");

        addSchema(DC_SCHEMA_URI, URI.create("file:" + Paths.get(LOCAL_DC_SCHEMA_PATH).toAbsolutePath()));

        int parseIndex = module.parse(stream(DC_XML), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(DC_XML), info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
    }

    @Test
    public void doNotValidateXmlWhenMissingRootSchema() {
        String xml = "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"" +
                " xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <dc:identifier>1234:example</dc:identifier>\n" +
                "</oai_dc:dc>";
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = module.parse(stream(xml), info, 0);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.UNDETERMINED, info.getValid());
    }

    @Test
    public void doNotValidateXmlWhenHasNoSchema() {
        String xml = "<dc>\n" +
                "  <identifier>1234:example</identifier>\n" +
                "</dc>";
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = module.parse(stream(xml), info, 0);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.UNDETERMINED, info.getValid());
    }

    @Test
    public void validateXmlFailsWhenHasSchemaAndInvalid() {
        String xml = "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"" +
                " xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" +
                "  <dc:bogus>1234:example</dc:bogus>\n" +
                "</oai_dc:dc>";
        RepInfo info = new RepInfo("uri:test");

        addSchema(DC_SCHEMA_URI, URI.create("file:" + Paths.get(LOCAL_DC_SCHEMA_PATH).toAbsolutePath()));

        int parseIndex = module.parse(stream(xml), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(xml), info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
    }

    @Test
    public void validateXmlSucceedsWhenLocalRelativeSchema() {
        RepInfo info = new RepInfo("uri:test");

        addSchema(DC_SCHEMA_URI, URI.create(Paths.get(LOCAL_DC_SCHEMA_PATH).toAbsolutePath().toString()));

        int parseIndex = module.parse(stream(DC_XML), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(DC_XML), info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
    }

    @Test
    public void validateXmlSucceedsWhenLocalHttpSchema() {
        RepInfo info = new RepInfo("uri:test");

        addSchema(DC_SCHEMA_URI, URI.create("http://digital.library.wisc.edu/1711.dl/XMLSchema-SimpleDC"));

        int parseIndex = module.parse(stream(DC_XML), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(DC_XML), info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
    }

    private InputStream stream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

    private void addSchema(URI source, URI destination) {
        module.param(String.format("schema=%s;%s", source, destination));
    }

}
