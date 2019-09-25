#!/usr/bin/env bash
# Grab the execution directory
SCRIPT_DIR="$( dirname "$( readlink -f "${BASH_SOURCE[0]}" )")"
export SCRIPT_DIR

# Include utils script
# shellcheck source=inc/bb-utils.sh
# shellcheck disable=SC1091
. "${SCRIPT_DIR}/inc/bb-utils.sh"
# TEST_ROOT directory default
TEST_ROOT="./test-root"
TEST_BASELINES_ROOT="${TEST_ROOT}/baselines"
TEST_INSTALL_ROOT="${TEST_ROOT}/jhove"
CANDIADATE_ROOT="${TEST_ROOT}/candidates"
TARGET_ROOT="${TEST_ROOT}/targets"
BASELINE_VERSION=1.22

# Create the JHOVE test root if it doesn't exist
[[ -d "${TEST_ROOT}" ]] || mkdir -p "${TEST_ROOT}"
# Create the JHOVE test installation root if it doesn't exist
[[ -d "${TEST_INSTALL_ROOT}" ]] || mkdir -p "${TEST_INSTALL_ROOT}"
# Create the JHOVE baseline installation root if it doesn't exist
[[ -d "${TEST_BASELINES_ROOT}" ]] || mkdir -p "${TEST_BASELINES_ROOT}"

# Install baseline JHOVE version and create test baseline
echo "INFO: Checking for baseline installation for release version ${BASELINE_VERSION}."
if [[ ! -d "${TEST_INSTALL_ROOT}/${BASELINE_VERSION}" ]]; then
	echo " - INFO: installing baseline version ${BASELINE_VERSION} to: ${TEST_INSTALL_ROOT}/${BASELINE_VERSION}."
	installJhoveFromURL "http://software.openpreservation.org/rel/jhove/jhove-${BASELINE_VERSION}.jar" "${TEST_INSTALL_ROOT}" "${BASELINE_VERSION}"
else
	echo " - INFO: using baseline version ${BASELINE_VERSION} at: ${TEST_INSTALL_ROOT}/${BASELINE_VERSION}."
fi

echo "INFO: Baseline data generation for version: ${BASELINE_VERSION}."
if [[ ! -d "${TEST_BASELINES_ROOT}/${BASELINE_VERSION}" ]]; then
	echo " - INFO: generating new baseline data to: ${TEST_BASELINES_ROOT}/${BASELINE_VERSION}."
	mkdir "${TEST_BASELINES_ROOT}/${BASELINE_VERSION}"
  bash "$SCRIPT_DIR/baseline-jhove.sh" -j "${TEST_INSTALL_ROOT}/${BASELINE_VERSION}" -c "${TEST_ROOT}/corpora" -o "${TEST_BASELINES_ROOT}/${BASELINE_VERSION}"
else
	echo " - INFO: using existing baseline data at: ${TEST_BASELINES_ROOT}/${BASELINE_VERSION}."
fi

# Create the JHOVE baseline installation root if it doesn't exist
[[ -d "${CANDIADATE_ROOT}" ]] || mkdir -p "${CANDIADATE_ROOT}"

# Set up the temp install location for JHOVE development
tempInstallLoc="/tmp/to-test";
if [[ -d "${tempInstallLoc}" ]]; then
	rm -rf "${tempInstallLoc}"
fi

# Create the test target root if it doesn't exist
[[ -d "${TARGET_ROOT}" ]] && rm -rf "${TARGET_ROOT:?}/"*
[[ -d "${TARGET_ROOT}" ]] || mkdir -p "${TARGET_ROOT}"

# Grab the Major and Minor versions from the full Maven project version string
MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
MAJOR_MINOR_VER="${MVN_VERSION%.*}"
JHOVE_INSTALLER="./jhove-installer/target/jhove-xplt-installer-${MVN_VERSION}.jar"

echo "INFO: Checking dev build of Jhove installer: ${JHOVE_INSTALLER}."
if [[ ! -e "${JHOVE_INSTALLER}" ]]
then
	echo " - INFO: mvn building the dev Jhove installer: ${JHOVE_INSTALLER}"
	mvn clean package
else
	echo " - INFO: found existing dev build Jhove installer: ${JHOVE_INSTALLER}"
fi
echo "INFO: Installing the development build of the Jhove installer to ${tempInstallLoc}."
installJhoveFromFile "${JHOVE_INSTALLER}" "${tempInstallLoc}"

[[ -d "${CANDIADATE_ROOT}/${MAJOR_MINOR_VER}" ]] || mkdir -p "${CANDIADATE_ROOT}/${MAJOR_MINOR_VER}"

echo "INFO: Checking baseline data for target Jhove: ${MAJOR_MINOR_VER}."
if [[ ! -d "${TARGET_ROOT}/${MAJOR_MINOR_VER}" ]]
then
	echo " - INFO: Generating the baseline for ${MAJOR_MINOR_VER} at: ${CANDIADATE_ROOT}/${MAJOR_MINOR_VER}."
	sed -i 's/^java.*/java -Xss1024k -javaagent:${HOME}\/\.m2\/repository\/org\/jacoco\/org\.jacoco\.agent\/0.7.9\/org\.jacoco.agent-0\.7\.9-runtime\.jar=destfile=jhove-apps\/target\/jacoco\.exec -classpath "$CP" Jhove -c "${CONFIG}" "${@}"/g' "${tempInstallLoc}/jhove"
	bash "$SCRIPT_DIR/baseline-jhove.sh" -j "${tempInstallLoc}" -c "${TEST_ROOT}/corpora" -o "${CANDIADATE_ROOT}/${MAJOR_MINOR_VER}"
fi

if [[ -f "${SCRIPT_DIR}/create-${MAJOR_MINOR_VER}-target.sh" ]]
then
	echo " - INFO: applying the baseline patches for ${MAJOR_MINOR_VER} at: ${TARGET_ROOT}/${MAJOR_MINOR_VER}"
	bash "${SCRIPT_DIR}/create-${MAJOR_MINOR_VER}-target.sh" -b "${BASELINE_VERSION}" -c "${MAJOR_MINOR_VER}"
else
	echo " - ERROR: no bash script found for baseline patches for ${MAJOR_MINOR_VER} at: ${TARGET_ROOT}/${MAJOR_MINOR_VER}."
	exit 1
fi

cat "${tempInstallLoc}/jhove"
echo ""
echo "Testing ${MAJOR_MINOR_VER}."
echo "=========================="
echo " - using development JHOVE installer: ${TEST_ROOT}/targets/${MAJOR_MINOR_VER}."
echo "java -Xms2g -Xmx8g -jar jhove-bbt/jhove-bbt.jar -b ${TEST_ROOT}/targets/${MAJOR_MINOR_VER} -c ${CANDIADATE_ROOT}/${MAJOR_MINOR_VER} -k dev-${MAJOR_MINOR_VER} -i"
java -Xms2g -Xmx8g -jar "jhove-bbt/jhove-bbt.jar" -b "${TEST_ROOT}/targets/${MAJOR_MINOR_VER}" -c "${CANDIADATE_ROOT}/${MAJOR_MINOR_VER}" -k "dev-${MAJOR_MINOR_VER}" -i
exitStatus=$?
echo ""
echo "RESULTS"
echo "======="
echo " - test comparison key is: dev-${MAJOR_MINOR_VER}."
echo " - BB Testing output is: ${exitStatus}"
exit $exitStatus
