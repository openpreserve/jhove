---
layout: page
title: Testing JHOVE
---
{{ page.title }}
================

Regression Testing
------------------
If you are building JHOVE on a Linux system, you can run regression testing of the output of the JHOVE modules
against a standard test corpus of files using scripts supplied in the JHOVE project. You can run a full regression test 
of all modules using a single supplied script.

This installs the latest stable release of JHOVE and runs it against the full test corpus to establish the baseline
output; it then builds the latest development version from the project code and runs that over the same test corpus.
Finally, the output of the two sets of output are compared and any differences recorded.

### Running Full Regression Test
In the root of your JHOVE project, run the script:

	./jhove-bbt/scripts/travis-test.sh

This outputs a full trace to the stdout stream.

### Running Regression tests manually
#### Establishing a Baseline
The script `jhove-bbt/scripts/baseline-jhove.sh` can be run using an existing installation of JHOVE, and can be used to 
test a smaller or more focussed test corpus. the usage for the script is:

	jhove-baseline [-j <pathToJhoveRoot>] [-c <pathToCorpora>] [-o <pathToOutput>] [-h|?]"
	
		-j pathToJhoveRoot		The full path to the root of a JHOVE installation.
		-c pathToCorpora		The path to the root directory of the test corpora.
		-o pathToOutput			The path to the root directory for baseline output.
		-h 						Show usage
		-?						Show usage

The directory specified by `pathToCorpora` should contain at least one descendent directory called "modules", below 
which should be further directories with names matching JHOVE modules (note: the "modules" directory does not need to be
a direct child of the `pathToCorpora` directory, but the JHOVE module directories should be direct children of the
"modules" directory). Each of these directories should contain test files to be analysed by that JHOVE module. 

For example, if using `/home/user/testdata` as the `pathToCorpora`, this directory should look like:

	/home/user/testdata
					  |-jhove
					  		|-modules
					  				|-PDF-hul
					  						|-TestFile1.pdf
					  						|-TestFile2.pdf
			  						|-XML-hul
			  								|-TestFile3.xml
			  								|-TestFile4.xml

This creates an xml file in the output for each test file in the corpus, mirroring the corpora directory structure, e.g.
if you use `/home/user/output` for the `pathToOutput` variable, you will see output like:

	/home/user/output
					  |-jhove
					  		|-modules
					  				|-PDF-hul
					  						|-TestFile1.pdf.jhove.xml
					  						|-TestFile2.pdf.jhove.xml
			  						|-XML-hul
			  								|-TestFile3.xml.jhove.xml
			  								|-TestFile4.xml.jhove.xml

To run this baseline script against the full test corpus, you can run:

	${JHOVE_PROJECT_DIR}/jhove-bbt/scripts/jhove-baseline.sh -j ${JHOVE_INSTALL_DIR} -c ${JHOVE_PROJECT_DIR}/test-root/corpora -o ${OUTPUT}