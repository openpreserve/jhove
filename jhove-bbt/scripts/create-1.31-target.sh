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

# Copy the TIFF fix affected files from the candidate to the target
declare -a tiff_affected=("examples/modules/TIFF-hul/cramps.tif.jhove.xml"
				"examples/modules/TIFF-hul/text.tif.jhove.xml"
				"examples/modules/TIFF-hul/testpage-small.tif.jhove.xml")
for filename in "${tiff_affected[@]}"
do
	if [[ -f "${candidateRoot}/${filename}" ]]; then
		cp "${candidateRoot}/${filename}" "${targetRoot}/${filename}"
	fi
done

# Copy the XHTML fix affected files from the candidate to the target
declare -a xhtml_affected=("errors/modules/HTML-hul/xhtml-trans-no-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-strict-no-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-frames-no-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-1-1-no-xml-dec.html.jhove.xml")
for filename in "${xhtml_affected[@]}"
do
	if [[ -f "${candidateRoot}/${filename}" ]]; then
		cp "${candidateRoot}/${filename}" "${targetRoot}/${filename}"
	fi
done

# Copy the XML fix affected files from the candidate to the target
declare -a xhtml_affected=("errors/modules/HTML-hul/xhtml-trans-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-strict-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-frames-xml-dec.html.jhove.xml"
				"errors/modules/HTML-hul/xhtml-1-1-xml-dec.html.jhove.xml"
				"examples/modules/XML-hul/valid-external.dtd.jhove.xml"
				"examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml"
				"examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml")
for filename in "${xhtml_affected[@]}"
do
	if [[ -f "${candidateRoot}/${filename}" ]]; then
		cp "${candidateRoot}/${filename}" "${targetRoot}/${filename}"
	fi
done

# Copy all of the AIF and WAV results as these are changed by the AES schema changes
cp -rf "${candidateRoot}/examples/modules/AIFF-hul" "${targetRoot}/examples/modules/"
cp -rf "${candidateRoot}/examples/modules/WAVE-hul" "${targetRoot}/examples/modules/"
cp -rf "${candidateRoot}/errors/modules/WAVE-hul" "${targetRoot}/errors/modules/"

# Copy the results of the new XML fixes for multiple redirect lookups and to ensure no regression for repeat XML warnings
cp -rf "${candidateRoot}/errors/modules/XML-hul" "${targetRoot}/errors/modules/"

# Copy the results of the PDF offset message fix
declare -a pdf_offset_affected=("errors/modules/PDF-hul/pdf-hul-5-govdocs-659152.pdf.jhove.xml"
				"errors/modules/PDF-hul/pdf-hul-10-govdocs-803945.pdf.jhove.xml"
				"regression/modules/PDF-hul/issue_306.pdf.jhove.xml")
for filename in "${pdf_offset_affected[@]}"
do
	if [[ -f "${candidateRoot}/${filename}" ]]; then
		cp "${candidateRoot}/${filename}" "${targetRoot}/${filename}"
	fi
done
