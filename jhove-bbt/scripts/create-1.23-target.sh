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
	echo " - removing existing baseline at ${targetRoot}."
	rm -rf "${targetRoot}"
fi

echo "TEST BASELINE: Creating baseline"
# Simply copy baseline for now we're not making any changes
echo " - copying ${baselineRoot} baseline to ${targetRoot}"
cp -R "${baselineRoot}" "${targetRoot}"

###
# E-PUB Module Fixes
###
# These copies are all OK as they're new files and don't overwrite
if [[ -f "${candidateRoot}/errors/modules/audit-EPUB-ptc.jhove.xml" ]]; then
	echo "   - EPUB copying EPUB audit results"
	cp "${candidateRoot}/errors/modules/audit-EPUB-ptc.jhove.xml" "${targetRoot}/errors/modules/audit-EPUB-ptc.jhove.xml"
fi
if [[ -d "${candidateRoot}/errors/modules/EPUB-ptc" ]]; then
	echo "   - EPUB copying error test reults"
	cp -R "${candidateRoot}/errors/modules/EPUB-ptc" "${targetRoot}/errors/modules"
fi
if [[ -f "${candidateRoot}/examples/modules/audit-EPUB-ptc.jhove.xml" ]]; then
	echo "   - EPUB copying JHOVE audit results"
	cp "${candidateRoot}/examples/modules/audit-EPUB-ptc.jhove.xml" "${targetRoot}/examples/modules/audit-EPUB-ptc.jhove.xml"
fi
if [[ -d "${candidateRoot}/examples/modules/EPUB-ptc" ]]; then
	echo "   - EPUB copying examples test reults"
	cp -R "${candidateRoot}/examples/modules/EPUB-ptc" "${targetRoot}/examples/modules"
fi
# Replace the text for XML Parser, EPub won't build without that version and JHOVE seems fine about it.
echo "   - EPUB replacing XML parser text"
sed -i 's%com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl$JAXPSAXParser%org.apache.xerces.jaxp.SAXParserImpl$JAXPSAXParser%' "${targetRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml"
# Add line for EPUB module to JHOVE audit file
sed -i '14 a \ \ \ <module release="1.0">EPUB-ptc</module>' "${targetRoot}/audit.jhove.xml"
