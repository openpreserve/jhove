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

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^  <usage>java Jhove/  <usage>java JHOVE/' {} \;

find "${targetRoot}" -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/^  <release>1.5/  <release>1.6/' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's/2011-02-03/2014-07-18/' {} \;

find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/<rights>.*<\/rights>/<rights>Derived from software Copyright 2004-2011 by the President and Fellows of Harvard College. Version 1.7 to 1.11 independently released. Version 1.12 onwards released by Open Preservation Foundation. Released under the GNU Lesser General Public License.<\/rights>/' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%Unicode 6\.0\.0%Unicode 7\.0\.0%g' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%Unicode6\.0\.0%Unicode7\.0\.0%g' {} \;

find "${targetRoot}" -type f -name "*.jhove.xml" -exec sed -i 's%<reportingModule release="1.5"%<reportingModule release="1.6"%' {} \;

if [[ -f "${candidateRoot}/examples/modules/audit-GZIP-kb.jhove.xml" ]]; then
cp "${candidateRoot}/examples/modules/audit-GZIP-kb.jhove.xml" "${targetRoot}/examples/modules/"
fi;
if [[ -f "${candidateRoot}/examples/modules/audit-WARC-kb.jhove.xml" ]]; then
cp "${candidateRoot}/examples/modules/audit-WARC-kb.jhove.xml" "${targetRoot}/examples/modules/"
fi;
if [[ -f "${candidateRoot}/examples/modules/audit-PNG-gdm.jhove.xml" ]]; then
  cp "${candidateRoot}/examples/modules/audit-PNG-gdm.jhove.xml" "${targetRoot}/examples/modules/"
fi;
