---
title: Getting started
layout: page
---

# JHOVE Installation Guide for Windows XP

{: .note}
[Contributed 2006-08-22 by Michael Stuart, UCSD]</span>

{: #download-install-jre .section-heading}
## 1 Download and install a J2SE 6 Java Runtime Environment (JRE)

1. The Sun J2SE 6 SDK (including a JRE and all code packages necessary for re-compilation) is available [here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html).

2. Exectute the downloaded file, installing the SDK in the folder

    `C:\Program Files\Java`

{: #download-install-jhove .section-heading}
## 2 Download and install JHOVE

1. JHOVE is available [here](https://github.com/openpreserve/jhove).

2.Extract the ZIP file to a new folder named

          C:\Documents and Settings\USERNAME\jhove
    
    where _USERNAME_ is the user name of your Windows XP account on the installation machine. Note the name of the installation directory. In this example, we use C:\\Program Files\\java\\/jre1.6.0\_10.
    
3. Edit the JHOVE configuration file

    C:\\Documents and Settings\\_USERNAME_\\jhove\\conf\\jhove.conf
    
    and
    
    1. Change the value of the `<jhoveHome>` tag so that it reads as follows:
        
                  <jhoveHome>"C:\Documents and Settings\USERNAME\jhove"</jhoveHome>
                
        
        Make sure to include the quotation marks at the beginning and end of the tag value.
        
    2. Change the value of the `<tempDirectory>` tag so that it reads as follows:
        
        `<tempDirectory>C:\temp</tempDirectory>`
        
    
    where _USERNAME_ is the user name of your Windows XP account on the installation machine.
    
4. Edit the JHOVE batch file

    C:\\Documents and Settings\\_USERNAME_\\jhove\\jhove.bat
    
    and change the following parameters to these values (making sure to include the quotation marks, and adjusting JAVA\_HOME to match your downloaded JRE):
    
          JHOVE_HOME="C:\Documents and Settings\USERNAME\jhove"
          JAVA_HOME="C:\Program Files\java\/jre1.6.0_10"
          JAVA=%JAVA_HOME%\bin\java
        
    
    where _USERNAME_ is the user name of your Windows XP account on the installation machine.

{: #using-jhove-gui .section-heading}
## 3 Using the JHOVE GUI Interface

1.There are two ways to launch the JHOVE GUI interface:

    1. From a DOS command line navigate to the JHOVE folder
        
        `C:\Documents and Settings\USERNAME\jhove`
        
        and issue the following command:
        
        `java –jar bin/JhoveView.jar`
        
        where _USERNAME_ is the user name of your Windows XP account on the installation machine.
        
    2. Double-click the “My computer” icon on the desktop, then type the folder name
        
        `C:\Documents and Settings\USERNAME_\jhove\bin`
        
        in the Address box,then double-Click on `JhoveView.jar` icon to launch JHOVE.
        

Please refer to the [documentation](/documentation/) for further information on using JHOVE.
