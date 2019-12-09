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
# Copying baseline for now we're not making any changes
cp -R "${baselineRoot}" "${targetRoot}"

# # Copy valid JP2K files across for new MIX metadata see https://github.com/openpreserve/jhove/pull/445
if [[ -d "${candidateRoot}/examples/modules/JPEG2000-hul" ]]; then
	echo "Copying valid JPEG2000 examples."
	cp -Rf "${candidateRoot}/examples/modules/JPEG2000-hul" "${targetRoot}/examples/modules/"
fi
if [[ -d "${candidateRoot}/errors/modules/JPEG2000-hul" ]]; then
	echo "Copying JPEG2000 errors."
	cp -Rf "${candidateRoot}/errors/modules/JPEG2000-hul" "${targetRoot}/errors/modules/"
fi

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.1">JPEG2000-hul<\/module>$/   <module release="1.4.2">JPEG2000-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <outputHandler release="1.8">XML/   <outputHandler release="1.9">XML/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.1<\/release>$/  <release>1.4.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <date>2019-04-17<\/date>$/  <date>2019-10-18<\/date>/' {} \;

find  "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec xmlstarlet ed --inplace -P -N 'ns=http://schema.openpreservation.org/ois/xml/ns/jhove' -d '//ns:identifiers[.//ns:identifier//ns:value[text()="http://hul.harvard.edu/jhove/references.html#classf" ]]' {} \;
find  "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i '/^    $/d' {} \;
