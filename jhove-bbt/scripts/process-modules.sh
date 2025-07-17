#!/usr/bin/env bash
##
# Processes a modules root directory, invoking the correct JHOVE module
# derived from the directory name.
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
#  $2 path to a root directory for module processing
#
#  $3 path to a root directory for output of module testing
##
##
# Functions defined first, control flow at the bottom of script
##

# Globals to hold the checked param vals
paramJhoveLoc=""
paramModuleLoc=""
paramOutputRootDir=""


# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?j:m:o:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		j)	paramJhoveLoc=$OPTARG
			;;
    m)	paramModuleLoc=$OPTARG
			;;
    o)	paramOutputRootDir=$OPTARG
    	;;
		esac
	done

	if [ -z "$paramJhoveLoc" ] || [ -z "$paramModuleLoc" ] || [ -z "$paramOutputRootDir" ]
	then
		showHelp
		exit 0
	fi

# Check that the JOVE testing tool exists
	if  [[ ! -e "$paramJhoveLoc"/jhove ]]
	then
		echo "JHOVE not found: $paramJhoveLoc"
		exit 1;
	fi

  # Check that the corpora directory exists
	if  [[ ! -d "$paramModuleLoc" ]]
	then
		echo "Corpora directory not found: $paramModuleLoc"
		exit 1;
	fi

  # Check that the output directory exists
	if  [[ ! -d "$paramOutputRootDir" ]]
	then
		echo "Output directory not found: $paramOutputRootDir"
		exit 1;
	fi
}

# Show usage message
showHelp() {
	echo "usage: process-modules [-j <pathToJhoveRoot>] [-m <pathToModules>] [-o <pathToOutput>] [-h|?]"
	echo ""
	echo "  pathToJhoveRoot : The full path to the root of a JHOVE installation."
  echo "  pathToModules   : The path to the root directory for module processing."
  echo "  pathToOutput    : The path to the root directory for baseline output."
}

# Cycle through the test module directories and invoke the correct JHOVE module
getCorpusModules() {
	for DIR in "$paramModuleLoc"/*/
	do
	  # https://stackoverflow.com/questions/1371261/get-current-directory-name-without-full-path-in-a-bash-script
	  moduleName="${DIR%"${DIR##*[!/]}"}" # extglob-free multi-trailing-/ trim
	  moduleName="${moduleName##*/}"      # remove everything before the last /
		if [ "$moduleName" = "EPUB-ptc" ]
		then
			 continue
	    fi
		if [[ ! -e "$paramOutputRootDir/audit-$moduleName.jhove.xml" ]]
		then
			bash "$SCRIPT_DIR/exec-with-to.sh" -t 10 "$paramJhoveLoc/jhove" -m "${moduleName}" -h xml -o "$paramOutputRootDir/audit-$moduleName.jhove.xml"
		fi
		processModuleDir "$paramModuleLoc/$moduleName"
	done
}

processModuleDir() {
	moduleDir=$1
	while IFS= read -r -d '' FILE; do
		fileName=$( basename "$FILE" )
		if [[ ! $fileName == ".gitignore" ]] && [[ ! $fileName == "README" ]]; then
			echo "Testing ${FILE} with ${paramJhoveLoc}/jhove"
			bash "$SCRIPT_DIR/exec-with-to.sh" -t 30 "$paramJhoveLoc/jhove" -m "${moduleName}" -h xml -o "$paramOutputRootDir/$moduleName/$fileName.jhove.xml" -k "$FILE"
		fi
	done <    <(find "$moduleDir" -type f -print0)
}

##
# Script Execution Starts HERE
##

# Check and setup parameters
checkParams "$@";
getCorpusModules;
