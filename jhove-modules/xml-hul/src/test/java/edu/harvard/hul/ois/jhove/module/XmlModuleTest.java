package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.RepInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class XmlModuleTest {

    private XmlModule module;

    @Before
    public void setup() throws JhoveException {
        JhoveBase base = new JhoveBase();
        module = new XmlModule();
        module.setBase(base);
    }

    @Test
    public void validateXmlSucceedsWhenHasRootSchema() {
        String xml = "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"" +
                " xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" +
                "  <dc:identifier>1234:example</dc:identifier>\n" +
                "</oai_dc:dc>";
        RepInfo info = new RepInfo("uri:test");

        int parseIndex = module.parse(stream(xml), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(xml), info, parseIndex);

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

        int parseIndex = module.parse(stream(xml), info, 0);

        assertEquals(1, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());

        parseIndex = module.parse(stream(xml), info, parseIndex);

        assertEquals(0, parseIndex);
        assertEquals(RepInfo.TRUE, info.getWellFormed());
        assertEquals(RepInfo.FALSE, info.getValid());
    }

    private InputStream stream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

}
