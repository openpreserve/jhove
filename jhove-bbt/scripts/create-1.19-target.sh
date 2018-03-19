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
# PDF Module changes
##
# Add the Release Canidate PDF-HUL details
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.10">PDF-hul<\/module>$/   <module release="1.11-RC">PDF-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <release>1.10<\/release>$/  <release>1.11-RC<\/release>/' {} \;
find "${targetRoot}" -type f -name "audit-PDF-hul.jhove.xml" -exec sed -i 's/^  <date>2017-10-31<\/date>$/  <date>2018-03-19<\/date>/' {} \;
find "${targetRoot}" -type f -name "*.pdf.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-19">PDF%' {} \;
find "${targetRoot}" -type f -name "README.jhove.xml" -exec sed -i 's%<reportingModule release="1.10" date="2017-10-31">PDF%<reportingModule release="1.11-RC" date="2018-03-19">PDF%' {} \;
# Improved handling of inconsistent XRef table
if [[ -f "${candidateRoot}/errors/modules/PDF-hul/corruptionOneByteMissing.pdf.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/PDF-hul/corruptionOneByteMissing.pdf.jhove.xml" "${targetRoot}/errors/modules/PDF-hul/"
fi

##
# WAV Module changes
##
# Copy over the new WAV module version audit file
if [[ -f "${candidateRoot}/examples/modules/audit-WAVE-hul.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/audit-WAVE-hul.jhove.xml" "${targetRoot}/examples/modules/"
fi;
# Copy over the new WAV module version audit file
if [[ -f "${candidateRoot}/errors/modules/audit-WAVE-hul.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/audit-WAVE-hul.jhove.xml" "${targetRoot}/errors/modules/"
fi;
# New version details for WAV\ module
find "${targetRoot}" -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5">WAVE-hul<\/module>$/   <module release="1.6-RC">WAVE-hul<\/module>/' {} \;
find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's%<reportingModule release="1.5" date="2017-10-31">WAVE%<reportingModule release="1.6-RC" date="2018-03-16">WAVE%' {} \;
# New MIME type info for WAV
find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's%<mimeType>audio/vnd.wave</mimeType>%<mimeType>audio/vnd.wave; codec=1</mimeType>%' {} \;
# New message for WAV module ignored chunk types
find "${targetRoot}" -type f -name "*.wav.jhove.xml" -exec sed -i 's%>Ignored Chunk type:%>Ignored unrecognized chunk:%' {} \;
# Finally copy the results files from candidate output for any major changes
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-missing.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-missing.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-missing.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-before-fmt.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-before-fmt.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-before-fmt.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-missing.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-missing.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-data-chunk-missing.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-missing.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-multiple.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-multiple.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-fmt-chunk-multiple.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-final-chunk.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-final-chunk.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-final-chunk.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-inner-chunk.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-riff.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-riff.wav.jhove.xml" "${targetRoot}/errors/modules/WAVE-hul/wf-pcm-44khz-8bit-mono-truncated-riff.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-alaw-44khz-8bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-alaw-44khz-8bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-alaw-44khz-8bit-mono-minimal.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-float-44khz-32bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-float-44khz-32bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-float-44khz-32bit-mono-minimal.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-float-44khz-64bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-float-44khz-64bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-float-44khz-64bit-mono-minimal.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-unnecessary.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-unnecessary.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-ds64-chunk-unnecessary.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-pcm-44khz-8bit-mono-minimal.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/rf64-ulaw-44khz-8bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/rf64-ulaw-44khz-8bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/rf64-ulaw-44khz-8bit-mono-minimal.wav.jhove.xml"
fi;
if [[ -f "${candidateRoot}/examples/modules/WAVE-hul/wfe-pcm-44khz-8bit-mono-minimal.wav.jhove.xml" ]]; then
	cp "${candidateRoot}/examples/modules/WAVE-hul/wfe-pcm-44khz-8bit-mono-minimal.wav.jhove.xml" "${targetRoot}/examples/modules/WAVE-hul/wfe-pcm-44khz-8bit-mono-minimal.wav.jhove.xml"
fi;
