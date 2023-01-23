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

# Modification of output due to
# https://github.com/openpreserve/jhove/pull/804
find "${targetRoot}" -type f -name "6mp_soft.tif.jhove.xml" -exec sed -i 's/>DSC</>digital still camera</' {} \;

# Update release details for PDF module
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.12.3" date="2022-04-22">PDF-hul<\/reportingModule>$/  <reportingModule release="1.12.4" date="2023-01-31">PDF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.12.3">PDF-hul<\/module>$/   <module release="1.12.4">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.12.3<\/release>$/  <release>1.12.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-01-31/' {} \;

# Copy examples for encrypted dictionaries
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/issue6010_1.pdf.jhove.xml" ]]; then
	echo " - First example for non reference encryption dictionaries."
	cp "${candidateRoot}/errors/modules/PDF-hul/issue6010_1.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/issue6010_1.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/issue6010_2.pdf.jhove.xml" ]]; then
	echo " - Second example for non reference encryption dictionaries."
	cp "${candidateRoot}/errors/modules/PDF-hul/issue6010_2.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/issue6010_2.pdf.jhove.xml"
fi

# Copy example for bad size int in PDF
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-73-bug-size-int.pdf.jhove.xml" ]]; then
	echo " - Bad size entry example."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-73-bug-size-int.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-73-bug-size-int.pdf.jhove.xml"
fi

# Copy the two existing error files changed by handling of empty strings in PDF
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml" ]]; then
	echo " - Copied because result altered by fix to empty string handling."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml" ]]; then
	echo " - Copied because result altered by fix to empty string handling."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml"
fi

# Copy Regression corpus results for empty string handling
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-94-false-positive.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling 1."
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-94-false-positive.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pdf-hul-94-false-positive.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/null-string-sig-1.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling 2."
	cp "${candidateRoot}/regression/modules/PDF-hul/null-string-sig-1.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/null-string-sig-1.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/null-string-sig-2.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling 3."
	cp "${candidateRoot}/regression/modules/PDF-hul/null-string-sig-2.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/null-string-sig-2.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/null-string.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling 4."
	cp "${candidateRoot}/regression/modules/PDF-hul/null-string.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/null-string.pdf.jhove.xml"
fi

# Copy Regression corpus results for files affected by fix for issue 672, filters as indirect objects
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-8-Secured.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling."
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-8-Secured.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pdf-hul-8-Secured.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-11-govdocs-152588.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling."
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-11-govdocs-152588.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pdf-hul-11-govdocs-152588.pdf.jhove.xml"
fi

# Copy Regression corpus results for files affected by PR 780 Check extension is a direct object
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/extensions-adbe-other.pdf.jhove.xml" ]]; then
	echo " - Regression check for empty PDF string handling."
	cp "${candidateRoot}/regression/modules/PDF-hul/extensions-adbe-other.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/extensions-adbe-other.pdf.jhove.xml"
fi


# Update release details for PDF module
find "${targetRoot}" -type f -name "*.jpg.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.3" date="2022-04-22">JPEG-hul<\/reportingModule>$/  <reportingModule release="1.5.4" date="2023-01-31">JPEG-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.3">JPEG-hul<\/module>$/   <module release="1.5.4">JPEG-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.3<\/release>$/  <release>1.5.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-01-31/' {} \;

# Copy the XML result of examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml
# changed by https://github.com/openpreserve/jhove/pull/748
if [[ -f "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" ]]; then
echo " - PR:748 JPEG result patch 1."
	cp "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" "${targetRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml"
fi
