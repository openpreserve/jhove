package edu.harvard.hul.ois.jhove.module;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.harvard.hul.ois.jhove.module.pdf.DocCatTests;
import edu.harvard.hul.ois.jhove.module.pdf.HeaderTests;

@RunWith(Suite.class)
@SuiteClasses({ DocCatTests.class, HeaderTests.class })
class PdfModuleTest {
	// Empty test suite that runs the PDF Module tests.
}
