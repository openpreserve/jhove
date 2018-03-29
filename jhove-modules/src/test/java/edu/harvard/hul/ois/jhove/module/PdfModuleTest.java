package edu.harvard.hul.ois.jhove.module;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.harvard.hul.ois.jhove.module.pdf.DocCatTests;
import edu.harvard.hul.ois.jhove.module.pdf.HeaderTests;
import edu.harvard.hul.ois.jhove.module.pdf.PageTreeTests;

@RunWith(Suite.class)
@SuiteClasses({ DocCatTests.class, HeaderTests.class, PageTreeTests.class })
class PdfModuleTest {
	// Empty test suite that runs the PDF Module tests.
}
