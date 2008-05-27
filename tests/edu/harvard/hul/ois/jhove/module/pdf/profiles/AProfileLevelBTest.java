package edu.harvard.hul.ois.jhove.module.pdf.profiles;
/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: May 27, 2008
 * Time: 11:22:58 AM
 * To change this template use File | Settings | File Templates.
 */

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.module.PdfModule;
import edu.harvard.hul.ois.jhove.module.SimplestPdfTestModule;
import junit.framework.TestCase;

import java.util.List;

public class AProfileLevelBTest extends TestCase {

    AProfileLevelB aProfileLevelB;
    PdfModule module;

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
        if (!aProfileLevelB.trailerDictOK()) {
            fail("Trailer dict should be ok");
        }
    }

    public void testCatalogDictOK() throws Exception {
        if (aProfileLevelB.catalogOK()) {
            fail("Catalog dict should not be ok");
        }
    }

    public void testSatisfiesThisProfile() throws Exception {
        if (!aProfileLevelB.satisfiesThisProfile()) {
            List errors = aProfileLevelB.getReasonsForNonCompliance();
            for (int i = 0; i < errors.size(); i++) {
                Property error = (Property) errors.get(i);
                System.out.println(error.getName() + ": " + error.getValue());
            }
        }
    }

    public void testFontsOK() throws Exception {
        if (!aProfileLevelB.fontsOK()) {
            fail("Fonts should be ok");
        }
    }


    public void testResourcesOK() throws Exception {
        if (!aProfileLevelB.resourcesOK()) {
            fail("Fonts should be empty");
        }
    }
}