package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.RepInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

}
