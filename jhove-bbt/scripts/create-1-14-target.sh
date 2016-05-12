#!/usr/bin/env bash

if [[ ! -d "test-root" ]]; then
  exit 1;
fi

if [[ ! -d "test-root/targets/1.14/release" ]]; then
  mkdir -p "test-root/targets/1.14/release"
fi

rm -rf "test-root/targets/1.14/release/*"

cp -R test-root/baselines/1.11/* "test-root/targets/1.14/release"

find test-root/targets/1.14/release -type f -name "*.jhove.xml" -exec sed -i 's%>http:\/\/www.iso.org<%>http:\/\/www.iso.org\/<%g' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.3">GIF-hul<\/module>$/a \
   <module release="0.1">GZIP-kb<\/module>' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.7">PDF-hul<\/module>$/a \
   <module release="1.0">PNG-gdm<\/module>' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i '/^   <module release="1.5">UTF8-hul<\/module>$/a \
   <module release="1.0">WARC-kb<\/module>' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i 's/^   <module release="1.5">UTF8-hul<\/module>$/   <module release="1.6">UTF8-hul<\/module>/' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i 's/^  <usage>java Jhove/  <usage>java JHOVE/' {} \;

find test-root/targets/1.14/release -type f -name "audit-UTF8-hul.jhove.xml" -exec sed -i 's/^  <release>1.5/  <release>1.6/' {} \;

find test-root/targets/1.14/release -type f -name "*.jhove.xml" -exec sed -i 's/2011-02-03/2014-07-18/' {} \;

find test-root/targets/1.14/release -type f -name "audit.jhove.xml" -exec sed -i 's/<rights>.*<\/rights>/<rights>Derived from software Copyright 2004-2011 by the President and Fellows of Harvard College. Version 1.7 to 1.11 independently released. Version 1.12 onwards released by Open Preservation Foundation. Released under the GNU Lesser General Public License.<\/rights>/' {} \;

find test-root/targets/1.14/release -type f -name "*.jhove.xml" -exec sed -i 's%Unicode 6\.0\.0%Unicode 7\.0\.0%g' {} \;

find test-root/targets/1.14/release -type f -name "*.jhove.xml" -exec sed -i 's%Unicode6\.0\.0%Unicode7\.0\.0%g' {} \;

find test-root/targets/1.14/release -type f -name "*.jhove.xml" -exec sed -i 's%<reportingModule release="1.5"%<reportingModule release="1.6"%' {} \;

cp test-root/candidates/1.14/examples/modules/audit-GZIP-kb.jhove.xml test-root/targets/1.14/release/examples/modules/
cp test-root/candidates/1.14/examples/modules/audit-WARC-kb.jhove.xml test-root/targets/1.14/release/examples/modules/
#if [[ -f test-root/candidates/1.14/examples/modules/audit-PNG-gdm.jhove.xml ]]; then
#  cp test-root/candidates/1.14/examples/modules/audit-PNG-gdm.jhove.xml test-root/targets/1.14/release/examples/modules/
#fi;
