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
cp -R "${baselineRoot}" "${targetRoot}"

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%>http:\/\/www.iso.org<%>http:\/\/www.iso.org\/<%g' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.3">GIF-hul<\/module>$/a \
   <module release="0.1">GZIP-kb<\/module>' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.7">PDF-hul<\/module>$/a \
   <module release="1.0">PNG-gdm<\/module>' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.5">UTF8-hul<\/module>$/a \
   <module release="1.0">WARC-kb<\/module>' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5">UTF8-hul<\/module>$/   <module release="1.6">UTF8-hul<\/module>/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.7">PDF-hul<\/module>$/   <module release="1.9">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.7<\/release>$/  <release>1.9<\/release>/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.3">WAVE-hul<\/module>$/   <module release="1.4">WAVE-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <release>1.3<\/release>$/  <release>1.4<\/release>/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^  <usage>java Jhove/  <usage>java JHOVE/' {} \;

find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/^  <release>1.5/  <release>1.6/' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's/2011-02-03/2014-07-18/' {} \;

find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <date>2012-08-12<\/date>$/  <date>2017-07-20<\/date>/' {} \;
find "${targetRoot}" -type f -name "audit-WAVE-hul.jhove.xml" -exec sed -i 's/^  <date>2007-12-14<\/date>$/  <date>2017-03-14<\/date>/' {} \;

find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's%<reportingModule release="1.7" date="2012-08-12">PDF%<reportingModule release="1.9" date="2017-07-20">PDF%' {} \;
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.7" date="2012-08-12">PDF%<reportingModule release="1.9" date="2017-07-20">PDF%' {} \;
find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's%<reportingModule release="1.3" date="2007-12-14">WAVE%<reportingModule release="1.4" date="2017-03-14">WAVE%' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/<rights>.*<\/rights>/<rights>Derived from software Copyright 2004-2011 by the President and Fellows of Harvard College. Version 1.7 to 1.11 independently released. Version 1.12 onwards released by Open Preservation Foundation. Released under the GNU Lesser General Public License.<\/rights>/' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%Unicode 6\.0\.0%Unicode 7\.0\.0%g' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%Unicode6\.0\.0%Unicode7\.0\.0%g' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%<reportingModule release="1.5"%<reportingModule release="1.6"%' {} \;

falsePositivePdfs="pdf-hul-2-simple-annotated-in-adobe-x.pdf.jhove.xml
pdf-hul-4-615006647.pdf.jhove.xml
pdf-hul-4-govdocs-788261.pdf.jhove.xml
pdf-hul-5-externalLink.pdf.jhove.xml
pdf-hul-10-814778526.pdf.jhove.xml
pdf-hul-15-grid-system.pdf.jhove.xml
pdf-hul-33-826355544.pdf.jhove.xml
pdf-hul-39-616615442.pdf.jhove.xml
pdf-hul-40-61501688X.pdf.jhove.xml
pdf-hul-40-govdocs-088919.pdf.jhove.xml
pdf-hul-41-834460599.pdf.jhove.xml
pdf-hul-44-629642362.pdf.jhove.xml
pdf-hul-45-52897422X.pdf.jhove.xml
pdf-hul-45-govdocs-600753.pdf.jhove.xml
pdf-hul-51-govdocs-085551.pdf.jhove.xml
pdf-hul-52-govdocs-983827.pdf.jhove.xml
pdf-hul-55-govdocs-616137.pdf.jhove.xml
pdf-hul-56-improperly-constructed-page-tree.pdf.jhove.xml
pdf-hul-59-629642362.pdf.jhove.xml
pdf-hul-59-govdocs-681811.pdf.jhove.xml
pdf-hul-64-616615027.pdf.jhove.xml
pdf-hul-65-847453723.pdf.jhove.xml
pdf-hul-84-govdocs-484279.pdf.jhove.xml
pdf-hul-87-embedded_video_avi.pdf.jhove.xml
pdf-hul-87-webCapture.pdf.jhove.xml"

for PDF in $falsePositivePdfs; do
	cp "${candidateRoot}/errors/modules/PDF-hul/${PDF}" "${targetRoot}/errors/modules/PDF-hul/"
done

if [[ -f "${candidateRoot}/examples/modules/audit-GZIP-kb.jhove.xml" ]]; then
cp "${candidateRoot}/examples/modules/audit-GZIP-kb.jhove.xml" "${targetRoot}/examples/modules/"
fi;
if [[ -f "${candidateRoot}/examples/modules/audit-WARC-kb.jhove.xml" ]]; then
cp "${candidateRoot}/examples/modules/audit-WARC-kb.jhove.xml" "${targetRoot}/examples/modules/"
fi;
if [[ -f "${candidateRoot}/examples/modules/audit-PNG-gdm.jhove.xml" ]]; then
  cp "${candidateRoot}/examples/modules/audit-PNG-gdm.jhove.xml" "${targetRoot}/examples/modules/"
fi;
