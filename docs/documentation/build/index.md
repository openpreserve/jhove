---
layout: page
title: Building JHOVE
---
{{ page.title }}
================

Building JHOVE from Source
--------------------------
Clone this project, checkout the integration branch, and use Maven, e.g.:

    git clone git@github.com:openpreserve/jhove.git
    cd jhove
    git checkout integration
    mvn clean install

Including JHOVE from Maven
--------------------------
From v1.16 onwards all production releases of JHOVE are deployed to Maven central. Add the version of JHOVE you'd like to use as a property in your Maven
POM:

    <properties>
      ...
      <jhove.version>1.16.6</jhove.version>
    </properties>

Use this dependency for the core classes Maven module (e.g. `JhoveBase`, `Module`, `ModuleBase`, etc.):

    <dependency>
      <groupId>org.openpreservation.jhove</groupId>
      <artifactId>jhove-core</artifactId>
      <version>${jhove.version}</version>
    </dependency>

this for the JHOVE module implementations:

    <dependency>
      <groupId>org.openpreservation.jhove</groupId>
      <artifactId>jhove-modules</artifactId>
      <version>${jhove.version}</version>
    </dependency>

and this for the JHOVE applications:

    <dependency>
      <groupId>org.openpreservation.jhove</groupId>
      <artifactId>jhove-apps</artifactId>
      <version>${jhove.version}</version>
    </dependency>

If you want the latest development packages you'll need to add the [Open Preservation Foundation's Maven repository](http://artifactory.openpreservation.org/artifactory/opf-dev) to your settings file:

    <profiles>
      <profile>
        <id>opf-artifactory</id>
        <repositories>
          <repository>
            <snapshots>
              <enabled>false</enabled>
            </snapshots>
            <id>central</id>
            <name>opf-dev</name>
            <url>http://artifactory.openpreservation.org/artifactory/opf-dev</url>
          </repository>
        </repositories>
      </profile>
    </profiles>
    <activeProfiles>
      <activeProfile>opf-artifactory</activeProfile>
    </activeProfiles>

You can then follow the instructions above to include particular Maven modules,
but you can now also choose odd minor versioned development builds. At the time
of writing the latest development version could be included by using the following property:

    <properties>
      ...
      <jhove.version>1.17.1</jhove.version>
    </properties>

or even:

    <properties>
      ...
      <jhove.version>[1.17.0,1.18.0]</jhove.version>
    </properties>

to always use the latest 1.17 build.
