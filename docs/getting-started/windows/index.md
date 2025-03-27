---
title: Getting started
layout: page
---

# JHOVE Installation Guide for Windows XP  
<span class="note">[Contributed 2006-08-22 by Michael Stuart, UCSD]</span>

{: #download-install-jre .section-heading}
## 1 Download and install a J2SE 6 Java Runtime Environment (JRE)

a. The Sun J2SE 6 SDK (including a JRE and all code packages necessary for re-compilation) is available [here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html).

b. Execute the downloaded file, installing the SDK in the folder:  
`C:\Program Files\Java`

{: #download-install-jhove .section-heading}
## 2 Download and install JHOVE

a. JHOVE is available [here](https://github.com/openpreserve/jhove).

b. Extract the ZIP file to a new folder named:  
`C:\Documents and Settings\USERNAME\jhove`  
where `USERNAME` is the user name of your Windows XP account on the installation machine. Note the name of the installation directory. In this example, we use `C:\Program Files\java\jre1.6.0_10`.

c. Edit the JHOVE configuration file:  
`C:\Documents and Settings\USERNAME\jhove\conf\jhove.conf`  

   i. Change the value of the `<jhoveHome>` tag so that it reads as follows:  
   ```
   <jhoveHome>"C:\Documents and Settings\USERNAME\jhove"</jhoveHome>
   ```
   Make sure to include the quotation marks at the beginning and end of the tag value.

   ii. Change the value of the `<tempDirectory>` tag so that it reads as follows:  
   ```
   <tempDirectory>C:\temp</tempDirectory>
   ```

d. Edit the JHOVE batch file:  
`C:\Documents and Settings\USERNAME\jhove\jhove.bat`  

   Change the following parameters to these values (making sure to include the quotation marks, and adjusting JAVA_HOME to match your downloaded JRE):  
   ```
   JHOVE_HOME="C:\Documents and Settings\USERNAME\jhove"
   JAVA_HOME="C:\Program Files\java\jre1.6.0_10"
   JAVA=%JAVA_HOME%\bin\java
   ```

{: #using-jhove-gui .section-heading}
## 3 Using the JHOVE GUI Interface

a. There are two ways to launch the JHOVE GUI interface:  

   i. From a DOS command line navigate to the JHOVE folder:  
   `C:\Documents and Settings\USERNAME\jhove`  
   and issue the following command:  
   ```
   java –jar bin/JhoveView.jar
   ```

   ii. Double-click the "My computer" icon on the desktop, then type the folder name:  
   `C:\Documents and Settings\USERNAME\jhove\bin`  
   in the Address box, then double-click on `JhoveView.jar` icon to launch JHOVE.

Please refer to the [documentation](/documentation/) for further information on using JHOVE.
