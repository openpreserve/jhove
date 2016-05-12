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
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Globals to hold the checked param vals
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
		echo "Destination testRoot not found: $paramTestRoot"
		exit 1;
	fi

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
	minorVersion=$1;
	jhoveRoot="${paramTestRoot}/jhove"
	baselinesRoot="${paramTestRoot}/baselines/1.${minorVersion}"
	installLegacyJhove "${minorVersion}" "${jhoveRoot}"

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
