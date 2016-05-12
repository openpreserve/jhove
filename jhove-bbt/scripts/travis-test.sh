#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export SCRIPT_DIR

# Include utils script
. "$SCRIPT_DIR/inc/bb-utils.sh"

# Install v1.11 and create test baseline
if [ -d test-root/jhove ]; then
	rm -rf test-root/jhove
fi
mkdir -p test-root/jhove

if [ -d test-root/baselines ]; then
	rm -rf test-root/baselines
fi
mkdir -p test-root/baselines

bash "$SCRIPT_DIR/legacy-baselines.sh"

if [ -d test-root/candidates ]; then
	rm -rf test-root/candidates
fi
mkdir -p test-root/candidates/1.12

tempInstallLoc="/tmp/to-test";
if [[ -d "${tempInstallLoc}" ]]; then
	rm -rf "${tempInstallLoc}"
fi
MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
installJhoveFromFile "./jhove-installer/target/jhove-xplt-installer-${MVN_VERSION}.jar" "${tempInstallLoc}"
bash "$SCRIPT_DIR/baseline-jhove.sh" -j "${tempInstallLoc}" -c test-root/corpora -o test-root/candidates/1.12

if [ -d test-root/targets ]; then
	rm -rf test-root/targets
fi
mkdir -p test-root/targets/1.12

mv test-root/baselines/1.11 test-root/targets/1.12/release
bash "$SCRIPT_DIR/create-1-12-target.sh"
bash "$SCRIPT_DIR/bbt-jhove.sh" -b test-root/targets/1.12/release -c ./test-root/corpora -j ./ -o test-root/candidates -k dev-test -i
exit $?
