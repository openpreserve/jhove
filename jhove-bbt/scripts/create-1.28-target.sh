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
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.12.3" date="2022-04-22">PDF-hul<\/reportingModule>$/  <reportingModule release="1.12.4" date="2023-03-16">PDF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.12.3">PDF-hul<\/module>$/   <module release="1.12.4">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.12.3<\/release>$/  <release>1.12.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

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
find "${targetRoot}" -type f -name "*.jpg.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.3" date="2022-04-22">JPEG-hul<\/reportingModule>$/  <reportingModule release="1.5.4" date="2023-03-16">JPEG-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.3">JPEG-hul<\/module>$/   <module release="1.5.4">JPEG-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.3<\/release>$/  <release>1.5.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Copy the XML result of examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml
# changed by https://github.com/openpreserve/jhove/pull/748
if [[ -f "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" ]]; then
echo " - PR:748 JPEG result patch 1."
	cp "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" "${targetRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml"
fi

# Place IDs into all of the JP2K messages that have errors, there's only one code.
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/offset="0" severity="error>"/offset="0" severity="error" id="JPEG2000-HUL-5">/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/offset="11" severity="error>"/offset="11" severity="error" id="JPEG2000-HUL-5">/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/offset="95" severity="error>"/offset="95" severity="error" id="JPEG2000-HUL-5">/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/offset="594" severity="error>"/offset="594" severity="error" id="JPEG2000-HUL-5">/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/severity="error">/severity="error" id="JHOVE-CORE-5">/' {} \;


# Update release details for JP2K module
find "${targetRoot}" -type f -name "*.md.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.4" date="2023-03-16">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.jpx.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.4" date="2023-03-16">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.4" date="2023-03-16">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.3">JPEG2000-hul<\/module>$/   <module release="1.4.4">JPEG2000-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.3<\/release>$/  <release>1.4.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Update release details for TIFF module
find "${targetRoot}" -type f -name "*.tif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.3" date="2022-04-22">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.9.4" date="2023-03-16">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.g3.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.3" date="2022-04-22">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.9.4" date="2023-03-16">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.9.3">TIFF-hul<\/module>$/   <module release="1.9.4">TIFF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/^  <release>1.9.3<\/release>$/  <release>1.9.4<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Update release details for HTML module
find "${targetRoot}" -type f -name "*.html.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.2" date="2022-04-22">HTML-hul<\/reportingModule>$/  <reportingModule release="1.4.3" date="2023-03-16">HTML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.2">HTML-hul<\/module>$/   <module release="1.4.3">HTML-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-HTML-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.2<\/release>$/  <release>1.4.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-HTML-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Update release details for UTF8 module
find "${targetRoot}" -type f -name "*.txt.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.7.2" date="2022-04-22">UTF8-hul<\/reportingModule>$/  <reportingModule release="1.7.3" date="2023-03-16">UTF8-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.7.2">UTF8-hul<\/module>$/   <module release="1.7.3">UTF8-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/^  <release>1.7.2<\/release>$/  <release>1.7.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Update release details for XML module
find "${targetRoot}" -type f -name "*.xml.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.2" date="2022-04-22">XML-hul<\/reportingModule>$/  <reportingModule release="1.5.3" date="2023-03-16">XML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.ent.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.2" date="2022-04-22">XML-hul<\/reportingModule>$/  <reportingModule release="1.5.3" date="2023-03-16">XML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.dtd.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.2" date="2022-04-22">XML-hul<\/reportingModule>$/  <reportingModule release="1.5.3" date="2023-03-16">XML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.2">XML-hul<\/module>$/   <module release="1.5.3">XML-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.2<\/release>$/  <release>1.5.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

# Update release details for PNG module
find "${targetRoot}" -type f -name "*.png.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.1" date="2022-04-22">PNG-gdm<\/reportingModule>$/  <reportingModule release="1.2" date="2023-03-16">PNG-gdm<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.1">PNG-gdm<\/module>$/   <module release="1.2">PNG-gdm<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/^  <release>1.1<\/release>$/  <release>1.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/2022-04-22/2023-03-16/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.1">EPUB-ptc<\/module>$/   <module release="1.2">EPUB-ptc<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="0.2">GZIP-kb<\/module>$/   <module release="0.3">GZIP-kb<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.1">WARC-kb<\/module>$/   <module release="1.2">WARC-kb<\/module>/' {} \;

# Copy the XML result of regression/modules/JPEG-hul/19_e190014.jpg.jhove.xml follwing https://github.com/openpreserve/jhove/pull/784
if [[ -f "${candidateRoot}/regression/modules/JPEG-hul/19_e190014.jpg.jhove.xml" ]]; then
echo " - PR:784 JPEG result patch 1."
	cp "${candidateRoot}/regression/modules/JPEG-hul/19_e190014.jpg.jhove.xml" "${targetRoot}/regression/modules/JPEG-hul/19_e190014.jpg.jhove.xml"
fi

# Finally copy the files affected by the duplicate error removal
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-86-govdocs-445892.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-86-govdocs-445892.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-86-govdocs-445892.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-35-govdocs-156429.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-35-govdocs-156429.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-35-govdocs-156429.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk-by-2-bytes.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk-by-2-bytes.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk-by-2-bytes.wav.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-chunk-size-larger-than-bytes-remaining.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-chunk-size-larger-than-bytes-remaining.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-chunk-size-larger-than-bytes-remaining.wav.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/class-cast.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/class-cast.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/class-cast.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/smallliz.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/smallliz.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/smallliz.tif.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/zackthecat.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/zackthecat.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/zackthecat.tif.jhove.xml"
fi

# Patch release details of the reporting module.
find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's/jhove\/1.8\/jhove.xsd/jhove\/1.9\/jhove.xsd/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/outputHandler release="1.9"/outputHandler release="1.10"/' {} \;
