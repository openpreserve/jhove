package edu.harvard.hul.ois.jhove.module.pdf.profiles;
/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: May 27, 2008
 * Time: 11:22:58 AM
 * To change this template use File | Settings | File Templates.
 */

import junit.framework.*;
import edu.harvard.hul.ois.jhove.module.pdf.profiles.AProfileLevelB;
import edu.harvard.hul.ois.jhove.module.PdfModuleQueryInterface;
import edu.harvard.hul.ois.jhove.module.SimplestPdfTestModule;

public class AProfileLevelBTest extends TestCase {

    AProfileLevelB aProfileLevelB;
    PdfModuleQueryInterface module;

    /**
     * Sets up the fixture, for example, open a network connection. This method
     * is called before a test is executed.
     */
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        module = new SimplestPdfTestModule();
        aProfileLevelB = new AProfileLevelB(module);
    }



    public void testTrailerDictOK() throws Exception {
        if (!aProfileLevelB.trailerDictOK()){
            fail("Trailer dict should be ok");
        }
    }
}