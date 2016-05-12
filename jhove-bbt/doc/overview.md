Overview
========
Aims
----
Compare XML output from a development branch of JHOVE against XML output
previously established as a production "baseline". This process will be used
to ensure that there's no unintended changes to JHOVE's validation and reporting
functions. It will also compare the modules packaged and there versions.

MVP functionality:
 - recurse directories of output files and filter files of one type;
 - report differences in file system, i.e. missing output files;
 - compare output files and report differences;
 - ignore variable data, e.g. report date, version details;
 - detection of development events, e.g. update of module or new module;
 - automatically creation of baseline and candidate output;
 - ability to run sensibly on structured test corpus; and
 - automated use of git bisect to trace the source of bugs.

Structure
---------
The prototype uses a folder structure convention to operate.

    .\testroot
        -\corpora
            -\a-corpus
            -\another-corpus
                -\AIFF
                -\ASCII
                -\GIF
                -\HTML
                -\JPEG
                -\JPEG2000
                -\PDF
                -\UTF-8
                -\WAVE
                -\XML
        -\baselines
            -\1-10
            -\1-11
                -\a-corpus
                -\another-corpus
                    -\AIFF
                    -\ASCII
                    -\GIF
                    -\HTML
                    -\JPEG
                    -\JPEG2000
                    -\PDF
                    -\UTF-8
                    -\WAVE
                    -\XML
            -\1-12-dev
        -\candidates
            -\dev-snapshot
        -\reports
            -\dev-snapshot-to-1-11
                -\20160430T123501

Whats's Tested
--------------
## JHOVE software audit
List the JHOVE software and installed module details, generated thus:

    jhove-loc/jhove -c jhoveLoc/conf/jhove.conf -h xml -o outputDir/audit.jhove.xml

Setting up JHOVE installations
-------------------------------
### Vagrant
To enable symlinks via shared folders on VirtualBox & Vagrant

    VBoxManage setextradata VM_NAME VBoxInternal2/SharedFoldersEnableSymlinksCreate 1

where `VM_NAME` is the name of the Vagrant box. **THIS DOESN'T APPEAR TO WORK**
### 1.9
To get JHOVE 1.9 working:

    chmod +x jhove/1.9/jhove
then edit `jhove/1.9/jhove` and change `JHOVE_HOME`, e.g.

    JHOVE_HOME=some-dir/test-root/jhove/1.9
