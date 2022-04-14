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
