#!/usr/bin/env bash
##
# author Carl Wilson carl@openpreservation.org
#
# Documentation and returns:
#
#  0 If succeeds
#
#
# Script expects 1 parameter:
#
#  $1 path to a folder as root for installs
#
##
##
# Functions defined first, control flow at the bottom of script
##
# Remove trailing backslash to parameter if present
##
# Functions defined first, control flow at the bottom of script
##
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export SCRIPT_DIR

# Global for Backslashed string value
glbCleanedBackRet=""

# Include utils script
# shellcheck source=inc/bb-utils.sh
# shellcheck disable=SC1091
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Globals to hold the checked param vals, default to JHOVE version 11
paramTestRoot="./test-root"
paramMinorVersion="11"

# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?d:v:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
    d)	paramTestRoot=$OPTARG
			;;
		v)	paramMinorVersion=$OPTARG
			;;
		esac
	done

  # Check dest dir exists
	if  [[ ! -d "$paramTestRoot" ]]
	then
		echo "Test Root folder not found: $paramTestRoot"
		exit 1;
	fi
	# Remove the backslash and assign the retval
	removeBackSlash $paramTestRoot;
	paramTestRoot=${glbCleanedBackRet};
}


# Show usage message
showHelp() {
	echo "usage: install-baselines [-d <testRoot] [-v <minorVersion>]  [-h|?]"
	echo ""
	echo "  destDir      : The full path to a root folder for test data structure."
	echo "                 default ./test-root"
	echo "  minorVersion : The minor version of JHOVE to baseline [9|10|11]."
	echo "                 default 11"
	echo ""
	echo "  -h|?         : This message."
}

baselineLegacyJhove() {
	if [ -z "$1" ]
	then
		return;
	fi;
	# Get the minor version param
	minorVersion=$1;
	# Test installation root is the jhove/minorVersion, e.g. ./test-root/jhove/1.11 for JHOVE version 11
	jhoveRoot="${paramTestRoot}/jhove"
	# Test baselines root is the jhove/minorVersion, e.g. ./test-root/baselines/1.11 for JHOVE version 11
	baselinesRoot="${paramTestRoot}/baselines/1.${minorVersion}"
	if [ -d "${baselinesRoot}" ]
	then
		return;
	fi;

	# Install the legacy
	installLegacyJhove "${minorVersion}" "${jhoveRoot}"
	# We can now clean the baselines directory as we'll be creating a new baseline.
	if [ -e "${baselinesRoot}" ]
	then
		rm -rf "${baselinesRoot}"
	fi;
	mkdir "${baselinesRoot}"

	bash "$SCRIPT_DIR/baseline-jhove.sh" -j "${jhoveRoot}/1.${minorVersion}" -c "${paramTestRoot}/corpora" -o "${baselinesRoot}"
}
##
# Script Execution Starts HERE
##

# Check and setup parameters
checkParams "$@";
baselineLegacyJhove "${paramMinorVersion}"
