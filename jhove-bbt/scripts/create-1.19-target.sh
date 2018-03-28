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

##
# Output Handler version changes
#
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/<outputHandler release="1.5">TEXT<\/outputHandler>/<outputHandler release="1.6-RC">TEXT<\/outputHandler>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/<outputHandler release="1.7">XML<\/outputHandler>/<outputHandler release="1.8-RC">XML<\/outputHandler>/' {} \;

# Add the Release Canidate PDF-HUL details
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.10">PDF-hul<\/module>$/   <module release="1.11-RC">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.10<\/release>$/  <release>1.11-RC<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <date>2017-10-31<\/date>$/  <date>2018-03-16<\/date>/' {} \;
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-16">PDF%' {} \;
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-16">PDF%' {} \;

##
# NISO MIME output changes
#
# Add MIX format designation data
fmtDesEleName="mix:FormatDesignation"
fmtNameEleName="mix:formatName"
fmtDesEleOpen="         <${fmtDesEleName}>"
fmtDesEleClose="         <\/${fmtDesEleName}>"
fmtNameEleOpen="          <${fmtNameEleName}>"
fmtNameEleClose="<\/${fmtNameEleName}>"
# For GIF
fmtDesEle="<\/mix:ObjectIdentifier>\n${fmtDesEleOpen}\n${fmtNameEleOpen}image\/gif${fmtNameEleClose}\n${fmtDesEleClose}"
find "${targetRoot}" -type f -name "*.gif.jhove.xml" -exec sed -i "s/<\/mix:ObjectIdentifier>$/${fmtDesEle}/g" {} \;
# For JPEG
fmtDesEle="<\/mix:ObjectIdentifier>\n${fmtDesEleOpen}\n${fmtNameEleOpen}image\/jpeg${fmtNameEleClose}\n${fmtDesEleClose}"
find "${targetRoot}" -type f -name "*.jpg.jhove.xml" -exec sed -i "s/<\/mix:ObjectIdentifier>$/${fmtDesEle}/g" {} \;
# For JP2
fmtDesEle="<\/mix:ObjectIdentifier>\n${fmtDesEleOpen}\n${fmtNameEleOpen}image\/jp2${fmtNameEleClose}\n${fmtDesEleClose}"
find "${targetRoot}" -type f -name "*.jpx.jhove.xml" -exec sed -i "s/<\/mix:ObjectIdentifier>$/${fmtDesEle}/g" {} \;
# For PNG
fmtDesEle="<\/mix:ObjectIdentifier>\n${fmtDesEleOpen}\n${fmtNameEleOpen}image\/png${fmtNameEleClose}\n${fmtDesEleClose}"
find "${targetRoot}" -type f -name "*.png.jhove.xml" -exec sed -i "s/<\/mix:ObjectIdentifier>$/${fmtDesEle}/g" {} \;
# For TIFF
fmtDesEle="<\/mix:ObjectIdentifier>\n${fmtDesEleOpen}\n${fmtNameEleOpen}image\/tiff${fmtNameEleClose}\n${fmtDesEleClose}"
find "${targetRoot}" -type f -name "*.tif.jhove.xml" -exec sed -i "s/<\/mix:ObjectIdentifier>$/${fmtDesEle}/g" {} \;
# Now for PDFs with embedded images, just copy these over
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-1-govdocs-519846.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-1-govdocs-519846.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-49-32932439X.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-49-32932439X.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-51-govdocs-085551.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-51-govdocs-085551.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-52-govdocs-983827.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-52-govdocs-983827.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-79-govdocs-095305.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-79-govdocs-095305.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/AA_Banner-single.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/PDF-hul/AA_Banner-single.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/AA_Banner.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/PDF-hul/AA_Banner.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/bedfordcompressed.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/PDF-hul/bedfordcompressed.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/fallforum03.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/PDF-hul/fallforum03.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/imd.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/PDF-hul/imd.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi
# Same for JPEG indentation woes
if [[ -f "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/JPEG-hul/20150213_140637.jpg.jhove.xml" "${targetRoot}/examples/modules/JPEG-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/JPEG-hul/AA_Banner-progressive.jpg.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/JPEG-hul/AA_Banner-progressive.jpg.jhove.xml" "${targetRoot}/examples/modules/JPEG-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/JPEG-hul/AA_Banner.jpg.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/JPEG-hul/AA_Banner.jpg.jhove.xml" "${targetRoot}/examples/modules/JPEG-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/GIF-hul/AA_Banner.gif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/GIF-hul/AA_Banner.gif.jhove.xml" "${targetRoot}/examples/modules/GIF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/GIF-hul/hul-banner.gif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/GIF-hul/hul-banner.gif.jhove.xml" "${targetRoot}/examples/modules/GIF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/JPEG2000-hul/monochrome.jp2.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/JPEG2000-hul/monochrome.jp2.jhove.xml" "${targetRoot}/examples/modules/JPEG2000-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/testpage-small.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/testpage-small.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/text.tif.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/TIFF-hul/text.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-10-814778526.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-10-814778526.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-33-826355544.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-33-826355544.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-govdocs-088919.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-govdocs-088919.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-41-834460599.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-41-834460599.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-44-629642362.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-44-629642362.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-55-govdocs-616137.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-55-govdocs-616137.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-59-629642362.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-59-629642362.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-59-govdocs-681811.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-59-govdocs-681811.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-64-616615027.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-64-616615027.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-65-847453723.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-65-847453723.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/"
fi
