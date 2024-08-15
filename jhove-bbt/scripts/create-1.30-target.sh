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

# Patch release details of the reporting module.
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/outputHandler release="1.10">XML/outputHandler release="1.11">XML/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/outputHandler release="1.1">JSON/outputHandler release="1.2">JSON/' {} \;

# Update release details for PDF module
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.12.4" date="2023-03-16">PDF-hul<\/reportingModule>$/  <reportingModule release="1.12.6" date="2024-07-31">PDF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.12.4">PDF-hul<\/module>$/   <module release="1.12.6">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.12.4<\/release>$/  <release>1.12.6<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/2023-03-16/2024-07-31/' {} \;

# Update release details for PNG module
find "${targetRoot}" -type f -name "*.png.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.2" date="2023-03-16">PNG-gdm<\/reportingModule>$/  <reportingModule release="1.3" date="2024-03-05">PNG-gdm<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.2">PNG-gdm<\/module>$/   <module release="1.3">PNG-gdm<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/^  <release>1.2<\/release>$/  <release>1.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/2023-03-16/2024-03-05/' {} \;

# Update release details for WAVE module
find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.8.2" date="2022-04-22">WAVE-hul<\/reportingModule>$/  <reportingModule release="1.8.3" date="2024-03-05">WAVE-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.8.2">WAVE-hul<\/module>$/   <module release="1.8.3">WAVE-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <release>1.8.2<\/release>$/  <release>1.8.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/2022-04-22/2024-03-05/' {} \;

# Update release details for XML module
find "${targetRoot}" -type f '(' -name "*.xml.jhove.xml" -o -name "*.ent.jhove.xml" -o -name "*.dtd.jhove.xml" ')' -exec sed -i 's/^  <reportingModule release="1.5.3" date="2023-03-16">XML-hul<\/reportingModule>$/  <reportingModule release="1.5.4" date="2024-03-05">XML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.3">XML-hul<\/module>$/   <module release="1.5.4">XML-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.3<\/release>$/  <release>1.5.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/2023-03-16/2024-03-05/' {} \;

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

# Copy the TIFF Module results changed by https://github.com/openpreserve/jhove/pull/915
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/AA_Banner.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/AA_Banner.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/AA_Banner.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/strike.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/strike.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/strike.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/testpage-large.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/testpage-large.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/testpage-large.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/testpage-medium.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/testpage-medium.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/testpage-medium.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/oxford.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/oxford.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/oxford.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/jim___gg.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/jim___gg.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/jim___gg.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/bathy1.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/bathy1.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/bathy1.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/jim___cg.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/jim___cg.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/jim___cg.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/compos.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/compos.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/compos.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/pagemaker.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/pagemaker.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/pagemaker.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/jello.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/jello.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/jello.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/little-endian.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/little-endian.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/little-endian.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/jim___ah.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/jim___ah.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/jim___ah.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/g3test.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/g3test.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/g3test.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/6mp_soft.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/6mp_soft.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/6mp_soft.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/ycbcr-cat.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/ycbcr-cat.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/ycbcr-cat.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/quad-lzw.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/quad-lzw.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/quad-lzw.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/jim___dg.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/jim___dg.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/jim___dg.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/fax2d.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/fax2d.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/fax2d.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml"
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