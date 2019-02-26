package edu.harvard.hul.ois.jhove.module;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.harvard.hul.ois.jhove.module.utf8.Utf8BlockTests;
import edu.harvard.hul.ois.jhove.module.utf8.Utf8ByteOrderTests;

@RunWith(Suite.class)
@SuiteClasses({ Utf8BlockTests.class, Utf8ByteOrderTests.class })
public class Utf8Test {
	// Empty test suite that runs the UTF-8 Module tests.
}
