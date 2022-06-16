@ECHO OFF

REM JHOVE - JSTOR/Harvard Object Validation Environment
REM
REM Copyright 2003-2005 by JSTOR and the President and Fellows of Harvard College
REM JHOVE is made available under the GNU General Public License (see the
REM file LICENSE for details)
REM
REM Usage: jhove [-c config] [-m module] [-h handler] [-e encoding]
REM              [-H handler] [-o output] [-x saxclass] [-t tempdir]
REM              [-b bufsize] [-l loglevel] [[-krs] dir-file-or-uri [...]]
REM
REM Where -c config   Configuration file pathname
REM       -m module   Module name
REM       -h handler  Output handler name (defaults to TEXT)
REM       -e encoding Character encoding of output handler (defaults to UTF-8)
REM       -H handler  About handler name
REM       -o output   Output file pathname (defaults to standard output)
REM       -x saxclass SAX parser class (defaults to J2SE 1.4 default)
REM       -t tempdir  Temporary directory in which to create temporary files
REM       -b bufsize  Buffer size for buffered I/O (defaults to J2SE default)
REM       -l loglevel Logging level
REM       -k          Calculate CRC32, MD5, SHA-1 and SHA-256 checksums
REM       -r          Display raw data flags, not textual equivalents
REM       -s          Format identification based on internal signatures only
REM       dir-file-or-uri Directory, file pathname, or URI of formatted content

REM *************************************************************************
REM Configuration options:
REM EXTRA_JARS  Extra JAR files to add to the Java class path
REM *************************************************************************
SET "EXTRA_JARS="

REM NOTE: Nothing below this line should be edited
REM #########################################################################

REM Infer JHOVE_HOME from script location
SET "JHOVE_HOME=%~dp0"

REM Create Java class path
SET "CP=%JHOVE_HOME%bin\*"
IF "%EXTRA_JARS%" NEQ "" (
  SET "CP=%CP%;%EXTRA_JARS%"
)

REM Set default configuration location
SET "CONFIG=%JHOVE_HOME%conf\jhove.conf"

REM Set class path and invoke Java
java -Xss1024k -classpath "%CP%" edu.harvard.hul.ois.jhove.Jhove -c "%CONFIG%" %*
