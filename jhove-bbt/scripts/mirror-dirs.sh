#!/usr/bin/env bash
##
# Given two root directories source (A) & destination (B) this script ensures
# that B becomes a sub-directory "mirror" of A. After the script runs B will be
# the parent of a copy of all sub-directories that are decendants of A.
#
# author Carl Wilson carl@openpreservation.org
#
# Documentation and returns:
#
#  0 If sub-directory setup succeeds
#
#  1 If source directory doesn't exist or can't be read.
#  2 If destination root directory doesn't exist.
#  3 If there's some problem writing to the destination.
#
# Script expects 2 parameters:
#
#  $1 path to a folder to use as a source of a directory tree
#
#  $2 path to the root destination for mirrored sub-dirs
##
##
# Functions defined first, control flow at the bottom of script
##

# Globals to hold the checked param vals
paramSource=""
paramDest=""
paramVerbose=false

# Global for Backslashed string value
glbCleanedBackRet=""

# Include utils script
# shellcheck source=inc/bb-utils.sh
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?vs:d:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		s)	paramSource=$OPTARG
			;;
    d)	paramDest=$OPTARG
			;;
		v)
			paramVerbose=true
			;;
		esac
	done

	if [ -z "$paramSource" ] || [ -z "$paramDest" ]
	then
		showHelp
		exit 0
	fi

# Check source dir exists
	if  [[ ! -d "$paramSource" ]]
	then
		echo "Source directory not found: $paramSource"
		exit 1;
	fi

	# Remove any trailing backslash from name
	removeBackSlash $paramSource;
	paramSource=${glbCleanedBackRet};

  # Check dest dir exists
	if  [[ ! -d "$paramDest" ]]
	then
		echo "Destination directory not found: $paramDest"
		exit 1;
	fi

	# Remove any trailing backslash from name
	removeBackSlash $paramDest;
	paramDest=${glbCleanedBackRet};
}

# Show usage message
showHelp() {
	echo "usage: mirror-dirs [-s <sourceDir>] [-d <destDir] [-v] [-h|?]"
	echo ""
	echo "  sourceDir : The full path to a source folder you wish to mirror the sub-directory tree of."
  echo "  destDir   : The full path to a root folder used as the mirror destination."
	echo ""
	echo "  -v        : Verbose output."
	echo "  -h|?      : This message."
}

# Cycle through the test module directories and invoke the correct JHOVE module
copyDirectories() {
  while IFS= read -r -d '' DIR
  do
    if [[ ! ${DIR} == "${paramSource}" ]]
    then
      destDir="$( echo ${DIR} | sed "s%^${paramSource}/%${paramDest}/%" )"
      if [[ ! -d "${destDir}" ]]
      then
        mkdir -p ${destDir};
				if [ "$paramVerbose" =  true ] ;
				then
        	echo "Creating mirror sub-directory ${destDir}";
				fi
      fi
    fi
  done <    <(find "$paramSource" -type d -print0)
}


##
# Script Execution Starts HERE
##

# Check and setup parameters
checkParams "$@";
copyDirectories;
