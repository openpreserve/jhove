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

# Simply copy baseline for now we're not making any changes
cp -R "${baselineRoot}" "${targetRoot}"

# Add the Release Canidate PDF-HUL details
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.10">PDF-hul<\/module>$/   <module release="1.11-RC">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.10<\/release>$/  <release>1.11-RC<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <date>2017-10-31<\/date>$/  <date>2018-03-16<\/date>/' {} \;
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-16">PDF%' {} \;
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-16">PDF%' {} \;
