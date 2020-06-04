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

# Copy the full result of regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/579
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml" ]]; then
	echo " - PR:579 result patch."
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml"
fi

# Copy the full result of errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf and
# errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/359
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml" ]]; then
	echo " - PR:359 result patch 1."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" ]]; then
	echo " - PR:359 result patch 2."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml"
fi
