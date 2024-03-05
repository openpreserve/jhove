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

# Update release details for ePub module
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.2">EPUB-ptc<\/module>$/   <module release="1.3">EPUB-ptc<\/module>/' {} \;

# Copy the XML file output changed by https://github.com/openpreserve/jhove/pull/889
if [[ -f "${candidateRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml" "${targetRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml"
fi

# Copy the PDF Module results changed by https://github.com/openpreserve/jhove/pull/871
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pr_871_a.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pr_871_a.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pr_871_a.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pr_871_b.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pr_871_b.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pr_871_b.pdf.jhove.xml"
fi
