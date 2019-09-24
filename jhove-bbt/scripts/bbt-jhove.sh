#!/usr/bin/env bash
##
# author Carl Wilson carl@openpreservation.org
#
# Documentation and returns:
#
#  0 If succeeds
#
#
# Script expects 2 parameter:
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

# Include utils script
# shellcheck source=inc/bb-utils.sh
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Globals to hold the checked param vals
paramBaseline=""
paramCorpusLoc=""
paramJhoveLoc=""
paramOutputLoc=""
paramKey=""
paramVerbose=false
paramIgnoreRelease=false

# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?vib:j:k:o:c:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		b)	paramBaseline=$OPTARG
			;;
		c)	paramCorpusLoc=$OPTARG
			;;
		j)	paramJhoveLoc=$OPTARG
			;;
		k)	paramKey=$OPTARG
			;;
    o)	paramOutputLoc=$OPTARG
			;;
		i)	paramIgnoreRelease=true
			;;
		v)	paramVerbose=true
		    export paramVerbose
			;;
		esac
	done

	if [ -z "$paramBaseline" ] || [ -z "$paramCorpusLoc" ] || [ -z "$paramJhoveLoc" ] || [ -z "$paramOutputLoc" ] || [ -z "$paramKey" ]
	then
		showHelp
		exit 0
	fi

  # Check dest dir exists
	[[ -d "$paramBaseline" ]] || mkdir -p "$paramBaseline"

  # Check dest dir exists
	if  [[ ! -d "$paramCorpusLoc" ]]
	then
		echo "Corpus directory not found: $paramCorpusLoc"
		exit 1;
	fi

  # Check dest dir exists
	if  [[ ! -d "$paramJhoveLoc" ]]
	then
		echo "JHOVE project directory not found: $paramJhoveLoc"
		exit 1;
	fi

  # Check dest dir exists
	if  [[ ! -d "$paramOutputLoc" ]]
	then
		echo "Candidate directory not found: $paramOutputLoc"
		exit 1;
	fi

  if  [[ -d "${paramOutputLoc}/${paramKey}" ]]
	then
    rm -rf "${paramOutputLoc:?}/${paramKey}"
	fi
  mkdir "${paramOutputLoc}/${paramKey}"
}


# Show usage message
showHelp() {
	echo "usage: bbt-jhove [-b <baselineRoot>] [-c <corpusRoot>] [-j <jhoveRoot>] [-o <outputRoot>] [-k <key>] [-i] [-h|?]"
	echo ""
	echo "  baselineRoot : The full path to a root folder of baseline test output."
	echo "  corpusRoot   : The full path to a root folder of corpus to test."
  echo "  jhoveRoot    : The full path to a root folder of a JHOVE install to test."
  echo "  outputRoot   : The full path to a root folder for candidate test output."
	echo "  key          : a unique key for the comparison."
	echo ""
	echo "  -i   : ignore release detail variables, i.e. version number and release dates."
	echo "  -h|? : This message."
}

##
# Script Execution Starts HERE
##

# Check and setup parameters
checkParams "$@";
candidate="${paramOutputLoc:?}/${paramKey}"
tempInstallLoc="/tmp/to-test";
sed -i 's/^java.*/java -javaagent:${HOME}\/\.m2\/repository\/org\/jacoco\/org\.jacoco\.agent\/0.7.9\/org\.jacoco.agent-0\.7\.9-runtime\.jar=destfile=jhove-apps\/target\/jacoco\.exec -classpath "$CP" Jhove -c "${CONFIG}" "${@}"/g' "${tempInstallLoc}/jhove"
bash "$SCRIPT_DIR/baseline-jhove.sh" -j "${tempInstallLoc}" -c "${paramCorpusLoc}" -o "${candidate}"

echo "java -Xms2g -Xmx8g -jar ${paramJhoveLoc:?}/jhove-bbt/jhove-bbt.jar -b ${paramBaseline} -c ${candidate} -k ${paramKey} -i"
if [ "$paramIgnoreRelease" =  true ] ;
then
	java -Xms2g -Xmx8g -jar "${paramJhoveLoc:?}/jhove-bbt/jhove-bbt.jar" -b "${paramBaseline}" -c "${candidate}" -k "${paramKey}" -i
else
	java -Xms2g -Xmx8g -jar "${paramJhoveLoc:?}/jhove-bbt/jhove-bbt.jar" -b "${paramBaseline}" -c "${candidate}" -k "${paramKey}"
fi
exit "${?}";
