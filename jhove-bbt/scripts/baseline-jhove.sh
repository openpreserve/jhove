#!/usr/bin/env bash
##
# Automates the production of JHOVE baseline output for regression tesing.
#
# author Carl Wilson carl@openpreservation.org
#
# Documentation and returns:
#
#  0           If all commands succeed
#
#  1   -  99   For execution problems
#  100 - 199   If any commands timeout
#
# Script expects 3 parameters:
#
#  $1 path to the version of JHOVE to test
#
#  $2 path to a root directory of corpus files
#
#  $3 path to a root directory for output of test baseline
##
##
# Functions defined first, control flow at the bottom of script
##
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export SCRIPT_DIR

# Globals to hold the checked param vals
paramJhoveLoc=""
paramCorporaLoc=""
paramOutputRootDir=""

# Global for Backslashed string value
glbCleanedBackRet=""

# Include utils script
# shellcheck source=inc/bb-utils.sh
# shellcheck disable=SC1091
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?j:c:o:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		j)	paramJhoveLoc=$OPTARG
			;;
    c)	paramCorporaLoc=$OPTARG
			;;
    o)	paramOutputRootDir=$OPTARG
    	;;
		esac
	done

	if [ -z "$paramJhoveLoc" ] || [ -z "$paramCorporaLoc" ] || [ -z "$paramOutputRootDir" ]
	then
		showHelp
		exit 0
	fi

# Check that the JOVE testing tool exists
	if  [[ ! -e "$paramJhoveLoc"/jhove ]]
	then
		echo "JHOVE not found at: $paramJhoveLoc"
		exit 1;
	fi

  # Check that the corpora directory exists
	if  [[ ! -d "$paramCorporaLoc" ]]
	then
		echo "Corpora directory not found at: $paramCorporaLoc"
		exit 1;
	fi

	# Remove any trailing backslash from name
	removeBackSlash "$paramCorporaLoc";
	paramCorporaLoc=${glbCleanedBackRet};

  # Check that the output directory exists
	if  [[ ! -d "$paramOutputRootDir" ]]
	then
		echo "Output directory not found at: $paramOutputRootDir"
		exit 1;
	fi

	# Remove any trailing backslash from name
	removeBackSlash "$paramOutputRootDir";
	paramOutputRootDir=${glbCleanedBackRet};
}

# Show usage message
showHelp() {
	echo "usage: jhove-baseline [-j <pathToJhoveRoot>] [-c <pathToCorpora>] [-o <pathToOutput>] [-h|?]"
	echo ""
	echo "  pathToJhoveRoot : The full path to the root of a JHOVE installation."
  echo "  pathToCorpora   : The path to the root directory of the test corpora."
  echo "  pathToOutput    : The path to the root directory for baseline output."
}

# Execute the JHOVE "about" command and serialise the Output
auditJhove() {
  bash "$paramJhoveLoc/jhove" -h xml -o "$paramOutputRootDir/audit.jhove.xml"
}

# Cycle through the test module directories and invoke the correct JHOVE module
processModuleDirs() {
  while IFS= read -r -d '' DIR
  do
		destDir="$( echo "${DIR}" | sed "s%^${paramCorporaLoc}/%${paramOutputRootDir}/%" )"
		bash "$SCRIPT_DIR/process-modules.sh" -j "$paramJhoveLoc" -m "$DIR" -o "$destDir"
  done <    <(find "$paramCorporaLoc" -type d -name modules -print0)
}

##
# Script Execution Starts HERE
##
# Check and setup parameters
checkParams "$@";
bash "$SCRIPT_DIR/mirror-dirs.sh" -s "$paramCorporaLoc" -d "$paramOutputRootDir"
auditJhove;
processModuleDirs;
