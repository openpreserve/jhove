#!/usr/bin/env bash

testRoot="test-root"
paramCandidateVersion=""
paramBaselineVersion=""
baselineRoot="${testRoot}/baselines"
candidateRoot="${testRoot}/candidates"
targetRoot="${testRoot}/targets"
# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?b:c:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		b)	paramBaselineVersion=$OPTARG
			;;
		c)	paramCandidateVersion=$OPTARG
			;;
		esac
	done

	if [ -z "$paramBaselineVersion" ] || [ -z "$paramCandidateVersion" ]
	then
		showHelp
		exit 0
	fi

	baselineRoot="${baselineRoot}/${paramBaselineVersion}"
	candidateRoot="${candidateRoot}/${paramCandidateVersion}"
	targetRoot="${targetRoot}/${paramCandidateVersion}"
}

# Show usage message
showHelp() {
	echo "usage: create-target [-b <baselineVersion>] [-c <candidateVersion>] [-h|?]"
	echo ""
	echo "  baselineVersion  : The version number id for the baseline data."
	echo "  candidateVersion : The version number id for the candidate data."
	echo ""
	echo "  -h|? : This message."
}

# Execution starts here
checkParams "$@";
if [[ -d "${targetRoot}" ]]; then
	rm -rf "${targetRoot}"
fi

echo "Executing baseline update"
# Simply copy baseline for now we're not making any changes
cp -R "${baselineRoot}" "${targetRoot}"

# BYTESTREAM Module
#
# New version details
# In the JHOVE Audit file
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.3">BYTESTREAM<\/module>$/   <module release="1.4">BYTESTREAM<\/module>/' {} \;
# and the results files
find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%<reportingModule release="1.3" date="2007-04-10">BYTESTREAM%<reportingModule release="1.4" date="2018-10-01">BYTESTREAM%' {} \;

# ASCII Module
#
# New version details
# In the JHOVE Audit file
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.3">ASCII-hul<\/module>$/   <module release="1.4">ASCII-hul<\/module>/' {} \;
# In the ASCII Module Audit file
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's%>2006-09-05</date>%>2018-10-01</date>%' {} \;
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's/>1.3<\/release>$/>1.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's/2003-2007/2003-2015/' {} \;
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's/College. Released under/College. Copyright 2015-2019 by the Open Preservation Foundation. Version 1.4 onwards developed by Open Preservation Foundation. Released under/' {} \;
# In the README file
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.3" date="2006-09-05">ASCII%<reportingModule release="1.4" date="2018-10-01">ASCII%' {} \;
# Replace in the results files
find "${targetRoot}" -type f -name "*.txt.jhove.xml" -exec sed -i 's%<reportingModule release="1.3" date="2006-09-05">ASCII%<reportingModule release="1.4" date="2018-10-01">ASCII%' {} \;
# Reporting changes
# FIX ASCII Module TAB code
find "${targetRoot}" -type f -name "control.txt.jhove.xml" -exec sed -i 's%TAB (0x09)%HT (0x09)%' {} \;

# PDF Module
#
# New version details
# In the JHOVE Audit file
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.11">PDF-hul<\/module>$/   <module release="1.12">PDF-hul<\/module>/' {} \;
# In the PDF Module Audit file
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's%>2018-03-29</date>%>2018-10-01</date>%' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/>1.11<\/release>$/>1.12<\/release>/' {} \;
# Replace in the results files
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's%<reportingModule release="1.11" date="2018-03-29">PDF%<reportingModule release="1.12" date="2018-10-01">PDF%' {} \;
# In the README file
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.11" date="2018-03-29">PDF%<reportingModule release="1.12" date="2018-10-01">PDF%' {} \;

# Fix Class Cast Error in PDF Module see https://github.com/openpreserve/jhove/issues/173
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/class-cast.pdf.jhove.xml" ]]; then
	echo "Copying #173 fix"
	cp "${candidateRoot}/regression/modules/PDF-hul/class-cast.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi

# UTF-8 Module
#
# New version details
# In the JHOVE Audit file
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.6">UTF8-hul<\/module>$/   <module release="1.7">UTF8-hul<\/module>/' {} \;
# In the ASCII Module Audit file
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's%>2014-07-18</date>%>2018-10-01</date>%' {} \;
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/>1.6<\/release>$/>1.7<\/release>/' {} \;
# In the README file
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.6" date="2014-07-18">UTF8%<reportingModule release="1.7" date="2018-10-01">UTF8%' {} \;
# Replace in the results files
find "${targetRoot}" -type f -name "*.txt.jhove.xml" -exec sed -i 's%<reportingModule release="1.6" date="2014-07-18">UTF8%<reportingModule release="1.7" date="2018-10-01">UTF8%' {} \;
