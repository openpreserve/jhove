#!/usr/bin/env bash
##
# author Carl Wilson carl@openpreservation.org
##
# Functions defined first
##
# Remove trailing backslash to parameter if present
removeBackSlash() {
	glbCleanedBackRet="";
	if [ -z "$1" ]
	then
		return;
	fi;
	glbCleanedBackRet=$1;
	length=${#glbCleanedBackRet}
  ((length--))
  if [[ "${glbCleanedBackRet:$length:1}" == "/" ]]
  then
    glbCleanedBackRet="${glbCleanedBackRet::-1}"
  fi
}

installLegacyJhove() {
  if [ -z "$1" ] || [ -z "$2" ]
	then
		return;
	fi;
	minorVersion=$1;
	installDest=$( realpath "$2" );
	wget "https://github.com/openpreserve/jhove/releases/download/v1.${minorVersion}/jhove-1_${minorVersion}.tar.gz" -O - | tar -xz  -C "${installDest}"
	jhoveHome="${installDest}/1.${minorVersion}";
	if [[ -e $jhoveHome ]]
	then
		rm -rf "${jhoveHome}"
	fi;
	mv "${installDest}/jhove" "${jhoveHome}"
	sed -i "s%^JHOVE_HOME=.*%JHOVE_HOME=${jhoveHome}%" "${jhoveHome}/jhove"
	sed -i "s%^ <jhoveHome>.*% <jhoveHome>${jhoveHome}</jhoveHome>%" "${jhoveHome}/conf/jhove.conf"
	sed -i "s%^ <tempDirectory>.*% <tempDirectory>/tmp</tempDirectory>%" "${jhoveHome}/conf/jhove.conf"
	chmod +x "${jhoveHome}/jhove"
}

installJhoveFromURL() {
  if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]
	then
		return;
	fi;
	jhoveURL=$1;
	installDest=$2;
	ident=$3
	tmpDownload="/tmp/jhove-installer.jar"
	wget "${jhoveURL}" -O "${tmpDownload}"
	installJhoveFromFile "${tmpDownload}" "${installDest}/${ident}"
	rm "${tmpDownload}"
}

installJhoveFromFile() {
  if [ -z "$1" ] || [ -z "$2" ]
	then
		return;
	fi;
	# First param should be the jhove installer jar
	jhoveFile=$1;
	# Param 2 is the installation directory
	installDestParam=$2
	echo "Checking for installation directory: ${installDestParam}"

	# Remove any existing installation at the location
	if [[ -e $installDestParam ]]
	then
		echo " - removing existing installation directory: ${installDestParam}"
		rm -rf "${installDestParam}"
	fi;

	echo " - creating fresh install directory: ${installDestParam}."
	mkdir -p "${installDestParam}"

	# need full path of installation directory for auto installer
	installDest=$(realpath "$installDestParam");

	# Overwrite the auto-install configured installation directory using sed.
	autoInstallConfig="${SCRIPT_DIR}/auto-vagrant-install.xml"
	sed -i "s%^<installpath>.*%<installpath>${installDest}</installpath>%" "${autoInstallConfig}"

	# run the installer
	java -jar "${jhoveFile}" "${autoInstallConfig}"
}
