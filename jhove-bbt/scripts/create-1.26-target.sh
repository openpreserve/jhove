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
# and regression/modules/PDF-hul/issue_531.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/579
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml" ]]; then
	echo " - PR:579 PDF result patch 1."
	cp "${candidateRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/pdf-hul-40-61501688X.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_531.pdf.jhove.xml" ]]; then
	echo " - ISSUE:531  PDF result patch 2."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_531.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_531.pdf.jhove.xml"
fi

# Copy the full result of regression/modules/PDF-hul/issue_375.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/596
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_375.pdf.jhove.xml" ]]; then
	echo " - ISSUE:375 PDF result patch."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_375.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_375.pdf.jhove.xml"
fi

# Copy the full result of errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf,
# errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf and regression/modules/PDF-hul/issue_358.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/359
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml" ]]; then
	echo " - PR:359 PDF result patch 1."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-14-govdocs-489354.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" ]]; then
	echo " - PR:359 result patch 2."
	cp "${candidateRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/pdf-hul-81-govdocs-128112.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_358.pdf.jhove.xml" ]]; then
	echo " - ISSUE:358 PDF result patch 3."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_358.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_358.pdf.jhove.xml"
fi

# Copy the full result of regression/modules/PNG-gdm/issue_148.png and
# regression/modules/PNG-gdm/issue_694.png
# fixed by: https://github.com/openpreserve/jhove/pull/580
if [[ -f "${candidateRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml" ]]; then
	echo " - ISSUE:148 PNG result patch 1."
	cp "${candidateRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml" "${targetRoot}/regression/modules/PNG-gdm/issue_148.png.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PNG-gdm/issue_694.png.jhove.xml" ]]; then
	echo " - ISSUE:693 PNG result patch 1."
	cp "${candidateRoot}/regression/modules/PNG-gdm/issue_694.png.jhove.xml" "${targetRoot}/regression/modules/PNG-gdm/issue_694.png.jhove.xml"
fi

# Patch the offset in errors/modules/PDF-hul/pdf-hul-76-372051162.pdf.jhove.xml
# fixed by https://github.com/openpreserve/jhove/pull/652
echo " - ISSUE:645 & ISSUE:646 PDF result patch 1."
find "${targetRoot}" -type f -name "pdf-hul-76-372051162.pdf.jhove.xml" -exec sed -i 's/^   <message offset="268334" severity="error" id="PDF-HUL-66">Lexical error<\/message>$/   <message offset="268333" severity="error" id="PDF-HUL-66">Lexical error<\/message>/' {} \;

# Copy the full result of regression/modules/PDF-hul/issue_645.pdf and
# regression/modules/PDF-hul/issue_646.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/652
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_645.pdf.jhove.xml" ]]; then
	echo " - ISSUE:645 & ISSUE:646 PDF result patch 2."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_645.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_645.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_646.pdf.jhove.xml" ]]; then
	echo " - ISSUE:645 & ISSUE:646 PDF result patch 2."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_646.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_646.pdf.jhove.xml"
fi

# Patch the offset in regression/modules/PDF-hul/issue_662.pdf.jhove.xml
# fixed by https://github.com/openpreserve/jhove/pull/665
echo " - ISSUE:662 PDF result patch 1."
find "${targetRoot}" -type f -name "issue_662.pdf.jhove.xml" -exec sed -i 's/^       <value>AutoCAD Architecture 2010 2010 (18.0s (LMS Tech<\/value>$/       <value>AutoCAD Architecture 2010 2010 (18.0s (LMS Tech))<\/value>/' {} \;

# Copy the full result of regression/modules/PDF-hul/issue_473_a.pdf and
# regression/modules/PDF-hul/issue_473_b.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/652
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_473_a.pdf.jhove.xml" ]]; then
	echo " - ISSUE:473 PDF result patch 1."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_473_a.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_473_a.pdf.jhove.xml"
fi
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_473_b.pdf.jhove.xml" ]]; then
	echo " - ISSUE:473 PDF result patch 2."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_473_b.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_473_b.pdf.jhove.xml"
fi

# Copy the full result of regression/modules/PDF-hul/issue_306.pdf
# fixed by: https://github.com/openpreserve/jhove/pull/#708
if [[ -f "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" ]]; then
	echo " - ISSUE:306 PDF result patch 1."
	cp "${candidateRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml" "${targetRoot}/regression/modules/PDF-hul/issue_306.pdf.jhove.xml"
fi

# Copy the XML result of examples/modules/XML-hul/jhoveconf.xml.jhove.xml
# changed by https://github.com/openpreserve/jhove/pull/634
if [[ -f "${candidateRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml" ]]; then
echo " - PR:634 XML result patch 1."
	cp "${candidateRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml" "${targetRoot}/examples/modules/XML-hul/jhoveconf.xml.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml" ]]; then
	echo " - PR:634 XML result patch 2."
	cp "${candidateRoot}/examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml" "${targetRoot}/examples/modules/XML-hul/external-parsed-entity.ent.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml" ]]; then
	echo " - PR:634 XML result patch 3."
	cp "${candidateRoot}/examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml" "${targetRoot}/examples/modules/XML-hul/external-unparsed-entity.ent.jhove.xml"
fi
if [[ -f "${candidateRoot}/examples/modules/XML-hul/valid-external.dtd.jhove.xml" ]]; then
	echo " - PR:634 XML result patch 4."
	cp "${candidateRoot}/examples/modules/XML-hul/valid-external.dtd.jhove.xml" "${targetRoot}/examples/modules/XML-hul/valid-external.dtd.jhove.xml"
fi

# Patch the coverage statement in */audit-PDF-hul.jhove.xml
# changed by https://github.com/openpreserve/jhove/pull/393
echo " - PR:393 PDF result patch 1."
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/\; PDF\/A (ISO\/CD 19005-1)//' {} \;

# Patch the text values in errors/modules/PDF-hul/pdf-hul-22-govdocs-000187.pdf.jhove.xml,
# errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml and
# errors/modules/PDF-hul/pdf-hul-43-govdocs-486355.pdf.jhove.xml
# fixed by https://github.com/openpreserve/jhove/pull/734
echo " - PR:734 PDF result patch 1."
find "${targetRoot}" -type f -name "pdf-hul-22-govdocs-000187.pdf.jhove.xml" -exec sed -i 's/0xb2715c537a8c9240e95cebdeafe6c4ed6afc6923d39cd860b782605cede5b39529/0xb27129537a8c9240e95cebdeafe6c4ed6afc6923d39cd860b782600dede5b395/' {} \;
find "${targetRoot}" -type f -name "pdf-hul-22-govdocs-000187.pdf.jhove.xml" -exec sed -i 's/0x9f457103394b9dd97f14f1d233cbf0980000000000000000000000000000000029/0x9f457103394b9dd97f14f1d233cbf09800000000000000000000000000000000/' {} \;
echo " - PR:734 PDF result patch 2."
find "${targetRoot}" -type f -name "pdf-hul-43-govdocs-486355.pdf.jhove.xml" -exec sed -i 's/0x0fab5bbd5c432bae08db3a9454ea1897394f9f9ba09e6461dbf0d89ce7f1da6d29/0x0fab5bbd28432bae08db3a9454ea1897394f9f9ba09e6461dbf0d89ce7f1da6d/' {} \;
find "${targetRoot}" -type f -name "pdf-hul-43-govdocs-486355.pdf.jhove.xml" -exec sed -i 's/0x403f07cb10c8eef28d625c1f596881110000000000000000000000000000000029/0x403f07cb10c8eef28d620d1f5968811100000000000000000000000000000000/' {} \;
echo " - PR:734 PDF result patch 3."
find "${targetRoot}" -type f -name "pdf-hul-10-govdocs-803945.pdf.jhove.xml" -exec sed -i 's/0x8431511c2cbf12475e48d0013e36c4c629/0x8431511c2cbf12475e48d0013e36c4c6/' {} \;
find "${targetRoot}" -type f -name "pdf-hul-10-govdocs-803945.pdf.jhove.xml" -exec sed -i 's/0x5c951120e00faad182edc884a297d9bc29/0x5c951120e00faad182edc884a297d9bc/' {} \;

# Version and release date patches
echo "Patching version numbers and release dates."
find "${targetRoot}" -type f -name "*.aif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.6.1" date="2019-12-10">AIFF-hul<\/reportingModule>$/  <reportingModule release="1.6.2" date="2022-04-22">AIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.AIF.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.6.1" date="2019-12-10">AIFF-hul<\/reportingModule>$/  <reportingModule release="1.6.2" date="2022-04-22">AIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.6.1">AIFF-hul<\/module>$/   <module release="1.6.2">AIFF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-AIFF-hul.jhove.xml" -exec sed -i 's/^  <release>1.6.1<\/release>$/  <release>1.6.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-AIFF-hul.jhove.xml" -exec sed -i 's/2019-12-10/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.txt.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.1" date="2019-04-17">ASCII-hul<\/reportingModule>$/  <reportingModule release="1.4.2" date="2022-04-22">ASCII-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.md.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.1" date="2019-04-17">ASCII-hul<\/reportingModule>$/  <reportingModule release="1.4.2" date="2022-04-22">ASCII-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.1">ASCII-hul<\/module>$/   <module release="1.4.2">ASCII-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.1<\/release>$/  <release>1.4.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-ASCII-hul.jhove.xml" -exec sed -i 's/2019-04-17/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.gif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.2" date="2019-12-10">GIF-hul<\/reportingModule>$/  <reportingModule release="1.4.3" date="2022-04-22">GIF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.2">GIF-hul<\/module>$/   <module release="1.4.3">GIF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-GIF-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.2<\/release>$/  <release>1.4.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-GIF-hul.jhove.xml" -exec sed -i 's/2019-12-10/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.html.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.1" date="2019-04-17">HTML-hul<\/reportingModule>$/  <reportingModule release="1.4.2" date="2022-04-22">HTML-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.1">HTML-hul<\/module>$/   <module release="1.4.2">HTML-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-HTML-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.1<\/release>$/  <release>1.4.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-HTML-hul.jhove.xml" -exec sed -i 's/2019-04-17/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.md.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.2" date="2019-10-18">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.jp2.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.2" date="2019-10-18">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.jpx.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.4.2" date="2019-10-18">JPEG2000-hul<\/reportingModule>$/  <reportingModule release="1.4.3" date="2022-04-22">JPEG2000-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.4.2">JPEG2000-hul<\/module>$/   <module release="1.4.3">JPEG2000-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/^  <release>1.4.2<\/release>$/  <release>1.4.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG2000-hul.jhove.xml" -exec sed -i 's/2019-10-18/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.jpg.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.5.2" date="2019-11-05">JPEG-hul<\/reportingModule>$/  <reportingModule release="1.5.3" date="2022-04-22">JPEG-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.2">JPEG-hul<\/module>$/   <module release="1.5.3">JPEG-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.2<\/release>$/  <release>1.5.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-JPEG-hul.jhove.xml" -exec sed -i 's/2019-11-05/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.12.2" date="2019-12-10">PDF-hul<\/reportingModule>$/  <reportingModule release="1.12.3" date="2022-04-22">PDF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.12.2">PDF-hul<\/module>$/   <module release="1.12.3">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.12.2<\/release>$/  <release>1.12.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/2019-12-10/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.tif.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.2" date="2019-12-10">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.9.3" date="2022-04-22">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "*.g3.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.9.2" date="2019-12-10">TIFF-hul<\/reportingModule>$/  <reportingModule release="1.9.3" date="2022-04-22">TIFF-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.9.2">TIFF-hul<\/module>$/   <module release="1.9.3">TIFF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/^  <release>1.9.2<\/release>$/  <release>1.9.3<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-TIFF-hul.jhove.xml" -exec sed -i 's/2019-12-10/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.txt.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.7.1" date="2019-04-17">UTF8-hul<\/reportingModule>$/  <reportingModule release="1.7.2" date="2022-04-22">UTF8-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.7.1">UTF8-hul<\/module>$/   <module release="1.7.2">UTF8-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/^  <release>1.7.1<\/release>$/  <release>1.7.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/2019-04-17/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.8.1" date="2019-12-10">WAVE-hul<\/reportingModule>$/  <reportingModule release="1.8.2" date="2022-04-22">WAVE-hul<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.8.1">WAVE-hul<\/module>$/   <module release="1.8.2">WAVE-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <release>1.8.1<\/release>$/  <release>1.8.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/2019-12-10/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5.1">XML-hul<\/module>$/   <module release="1.5.2">XML-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/^  <release>1.5.1<\/release>$/  <release>1.5.2<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-XML-hul.jhove.xml" -exec sed -i 's/2019-04-17/2022-04-22/' {} \;

find "${targetRoot}" -type f -name "*.png.jhove.xml" -exec sed -i 's/^  <reportingModule release="1.0" date="2016-02-25">PNG-gdm<\/reportingModule>$/  <reportingModule release="1.1" date="2022-04-22">PNG-gdm<\/reportingModule>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/^  <release>1.0<\/release>$/  <release>1.1<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PNG-gdm.jhove.xml" -exec sed -i 's/2016-02-25/2022-04-22/' {} \;


find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/<outputHandler release="1.0">JSON/<outputHandler release="1.1">JSON/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/module release="1.0"/module release="1.1"/' {} \;
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/module release="0.1"/module release="0.2"/' {} \;


find "${targetRoot}" -type f -name "audit-*-hul.jhove.xml" -exec sed -i 's/^  <date>2019-10-18<\/date>$/  <date>2022-04-22<\/date>/' {} \;

