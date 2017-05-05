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
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Globals to hold the checked param vals
paramBaseline=""
paramCandidate=""
paramKey=""
paramVerbose=false
paramIgnoreRelease=false

# Check the passed params to avoid disapointment
checkParams () {
	OPTIND=1	# Reset in case getopts previously used

	while getopts "h?vib:c:k:" opt; do	# Grab the options
		case "$opt" in
		h|\?)
			showHelp
			exit 0
			;;
		b)	paramBaseline=$OPTARG
			;;
		c)	paramCandidate=$OPTARG
			;;
		k)	paramKey=$OPTARG
			;;
		i)	paramIgnoreRelease=true
			;;
		v)	paramVerbose=true
				export paramVerbose
			;;
		esac
	done

	if [ -z "$paramBaseline" ] || [ -z "$paramCandidate" ] || [ -z "$paramKey" ]
	then
		showHelp
		exit 0
	fi

  # Check dest dir exists
	if  [[ ! -d "$paramBaseline" ]]
	then
		echo "Baseline output directory not found: $paramBaseline"
		exit 1;
	fi

  # Check dest dir exists
	if  [[ ! -d "$paramCandidate" ]]
	then
		echo "Candidate directory not found: $paramCandidate"
		exit 1;
	fi
}

# Show usage message
showHelp() {
	echo "usage: bbt-jhove [-b <baselineRoot>] [-c <candidateRoot>] [-k <key>] [-i] [-h|?]"
	echo ""
	echo "  baselineRoot  : The full path to a root folder of baseline test output."
	echo "  candidateRoot : The full path to a root folder of candidate test output."
	echo "  key           : a unique key for the comparison."
	echo ""
	echo "  -i   : ignore release detail variables, i.e. version number and release dates."
	echo "  -h|? : This message."
}

##
# Script Execution Starts HERE
##

# Check and setup parameters
checkParams "$@";
if [ "$paramIgnoreRelease" =  true ] ;
then
	java -jar /vagrant/jhove-bbt/jhove-bbt.jar -b "${paramBaseline}" -c "${paramCandidate}" -k "${paramKey}" -i
else
	java -jar /vagrant/jhove-bbt/jhove-bbt.jar -b "${paramBaseline}" -c "${paramCandidate}" -k "${paramKey}"
fi
exit "${?}";
