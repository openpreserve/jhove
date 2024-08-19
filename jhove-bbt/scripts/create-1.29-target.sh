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
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pr_871_c.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pr_871_c.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pr_871_c.pdf.jhove.xml"
fi

# Copy the PDF Module results changed by https://github.com/openpreserve/jhove/pull/882
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml"
fi

# Copy the PNG Module results changed by https://github.com/openpreserve/jhove/pull/843
if [[ -f "${candidateRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml" "${targetRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml"
fi

declare -a indent_affected=("errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml"
				"errors/modules/PDF-hul/pdf-hul-9-govdocs-065694.pdf.jhove.xml"
				"errors/modules/PDF-hul/pdf-hul-1-govdocs-519846.pdf.jhove.xml"
				"errors/modules/PDF-hul/pdf-hul-49-32932439X.pdf.jhove.xml"
				"errors/modules/JPEG2000-hul/is_jpx.jp2.jhove.xml"
				"errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-2-unrecognized-bytes.wav.jhove.xml"
				"regression/modules/PNG-gdm/issue_694.png.jhove.xml"
				"regression/modules/PDF-hul/null-string.pdf.jhove.xml"
				"regression/modules/PDF-hul/pdf-hul-94-false-positive.pdf.jhove.xml"
				"regression/modules/PDF-hul/issue_646.pdf.jhove.xml"
				"regression/modules/PDF-hul/null-string-sig-2.pdf.jhove.xml"
				"regression/modules/PDF-hul/null-string-sig-1.pdf.jhove.xml"
				"regression/modules/PDF-hul/pdf-hul-40-govdocs-088919.pdf.jhove.xml"
				"examples/modules/TIFF-hul/cramps.tif.jhove.xml"
				"examples/modules/TIFF-hul/text.tif.jhove.xml"
				"examples/modules/TIFF-hul/testpage-small.tif.jhove.xml"
				"examples/modules/JPEG2000-hul/ROITest.jpx.jhove.xml"
				"examples/modules/WAVE-hul/8-Bit-Noise-1.wav.jhove.xml"
				"examples/modules/WAVE-hul/8-Bit-Noise-2.wav.jhove.xml"
				)
for filename in "${indent_affected[@]}"
do
	if [[ -f "${candidateRoot}/${filename}" ]]; then
		cp "${candidateRoot}/${filename}" "${targetRoot}/${filename}"
	fi
done