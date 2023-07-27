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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class XmlModuleTest {

    private static final String RESOURCE_DIR = "src/test/resources/edu/harvard/hul/ois/jhove/module/";
    private static final String MODULE_NAME = "XML-hul";
    private static final int SIG_BYTES = 1024;

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
    public void validateXmlSucceedsWhenHasRootSchema() throws IOException {
        File file = new File(RESOURCE_DIR + "mods-well-formed.xml");
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = parse(file, info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = parse(file, info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.TRUE, info.getValid());
    }

    @Test
    public void doNotValidateXmlWhenMissingRootSchema() throws IOException {
        File file = new File(RESOURCE_DIR + "mods-missing-root-schema.xml");
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = parse(file, info, 0);

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
    public void validateXmlFailsWhenHasSchemaAndInvalid() throws IOException {
        File file = new File(RESOURCE_DIR + "mods-invalid.xml");
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = parse(file, info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = parse(file, info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
    }

    private int parse(File file, RepInfo info, int parseIndex) throws IOException {
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return module.parse(stream, info, parseIndex);
        }
    }

    private InputStream stream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

}
