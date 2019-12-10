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

# Copy valid JP2K files across for new MIX metadata see https://github.com/openpreserve/jhove/pull/445
if [[ -d "${candidateRoot}/examples/modules/JPEG2000-hul" ]]; then
	echo "Copying valid JPEG2000 examples."
	cp -Rf "${candidateRoot}/examples/modules/JPEG2000-hul" "${targetRoot}/examples/modules/"
fi
if [[ -d "${candidateRoot}/errors/modules/JPEG2000-hul" ]]; then
	echo "Copying JPEG2000 errors."
	cp -Rf "${candidateRoot}/errors/modules/JPEG2000-hul" "${targetRoot}/errors/modules/"
fi

# Copy WAV files across for new MIX metadata see https://github.com/openpreserve/jhove/pull/445
if [[ -d "${candidateRoot}/examples/modules/WAVE-hul" ]]; then
	echo "Copying valid WAVE examples."
	cp -Rf "${candidateRoot}/examples/modules/WAVE-hul" "${targetRoot}/examples/modules/"
fi
if [[ -d "${candidateRoot}/errors/modules/WAVE-hul" ]]; then
	echo "Copying WAVE errors."
	cp -Rf "${candidateRoot}/errors/modules/WAVE-hul" "${targetRoot}/errors/modules/"
fi

# Copy PDF files across for extra error ids
if [[ -d "${candidateRoot}/regression/modules/PDF-hul" ]]; then
	echo "Copying valid PDF regressions."
	cp -Rf "${candidateRoot}/regression/modules/PDF-hul" "${targetRoot}/regression/modules/"
fi
if [[ -d "${candidateRoot}/errors/modules/PDF-hul" ]]; then
	echo "Copying PDF errors."
	cp -Rf "${candidateRoot}/errors/modules/PDF-hul" "${targetRoot}/errors/modules/"
fi

# Copy TIIF across for new Message IDs https://github.com/openpreserve/jhove/pull/510
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/chase-tif-f.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/chase-tif-f.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/g3test.g3.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/g3test.g3.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/smallliz.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/smallliz.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/peppers.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/zackthecat.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/zackthecat.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/fax2d.g3.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/fax2d.g3.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/quad-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" ]]; then
	echo "Copying affected TIFF examples."
	cp -Rf "${candidateRoot}/examples/modules/TIFF-hul/cramps-tile.tif.jhove.xml" "${targetRoot}/examples/modules/TIFF-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/valid-external.dtd.jhove.xml" ]]; then
	echo "Copying affected XML examples."
	cp -Rf "${candidateRoot}/examples/modules/XML-hul/valid-external.dtd.jhove.xml" "${targetRoot}/examples/modules/XML-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml" ]]; then
	echo "Copying affected XML examples."
	cp -Rf "${candidateRoot}/examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml" "${targetRoot}/examples/modules/XML-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml" ]]; then
	echo "Copying affected XML examples."
	cp -Rf "${candidateRoot}/examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml" "${targetRoot}/examples/modules/XML-hul/"
fi
if [[ -f "${candidateRoot}/examples/modules/PDF-hul/AA_Banner.pdf.jhove.xml" ]]; then
	echo "Copying affected PDF examples."
	cp -Rf "${candidateRoot}/examples/modules/PDF-hul/AA_Banner.pdf.jhove.xml" "${targetRoot}/examples/modules/PDF-hul/"
fi

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.1">JPEG2000-hul<\/module>$/   <module release="1.4.2">JPEG2000-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <outputHandler release="1.8">XML/   <outputHandler release="1.9">XML/' {} \;
find . -type f -name "audit.jhove.xml" -exec sed -i 's/^   <outputHandler release="1.1">Audit<\/outputHandler>/   <outputHandler release="1.1">Audit<\/outputHandler>\n   <outputHandler release="1.0">JSON<\/outputHandler>/' {} \;

find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.1<\/release>$/  <release>1.4.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <date>2019-04-17<\/date>$/  <date>2019-10-18<\/date>/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.7.1">WAVE-hul<\/module>$/   <module release="1.8.1">WAVE-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <release>1.7.1<\/release>$/  <release>1.8.1<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <date>2019-04-17<\/date>$/  <date>2019-12-10<\/date>/' {} \;

find "${targetRoot}" -type f -name "*.aif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.1" date="2019-04-17">AIFF-hul<\/reportingModule>$/  <reportingModule release="1.6.1" date="2019-12-10">AIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.AIF.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.1" date="2019-04-17">AIFF-hul<\/reportingModule>$/  <reportingModule release="1.6.1" date="2019-12-10">AIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.1">AIFF-hul<\/module>$/   <module release="1.6.1">AIFF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-AIFF-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.1<\/release>$/  <release>1.6.1<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-AIFF-hul.jhove.xml" -exec sed -i 's/^  <date>2019-04-17<\/date>$/  <date>2019-12-10<\/date>/' {} \;

find "${targetRoot}" -type f -name "*.tif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.1" date="2019-04-17">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.10.1" date="2019-12-10">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.g3.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.1" date="2019-04-17">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.10.1" date="2019-12-10">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.9.1">TIFF-hul<\/module>$/   <module release="1.10.1">TIFF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/^  <release>1.9.1<\/release>$/  <release>1.10.1<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/^  <date>2019-04-17<\/date>$/  <date>2019-12-10<\/date>/' {} \;

find  "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec xmlstarlet ed --inplace -N 'ns=http://schema.openpreservation.org/ois/xml/ns/jhove' -d '//ns:identifiers[.//ns:identifier//ns:value[text()="http://hul.harvard.edu/jhove/references.html#classf" ]]' {} \;
find  "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i '/^    $/d' {} \;

# Remove new SHA-256 values
find  "${candidateRoot}" -type f -name "*.jhove.xml" -exec xmlstarlet ed --inplace -N 'ns=http://schema.openpreservation.org/ois/xml/ns/jhove' -d '//ns:checksums//ns:checksum[@type = "SHA-256"]' {} \;
find  "${targetRoot}" -type f -name "*.jhove.xml" -exec xmlstarlet ed --inplace -N 'ns=http://schema.openpreservation.org/ois/xml/ns/jhove' -d '//ns:checksums//ns:checksum[@type = "SHA-256"]' {} \;
