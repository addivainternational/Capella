# Introduction 
Repository contents:
- nalabs_plugin
  - Capella Studio workspace
    - Nalabs
      - Capella Addin fro NALABS integration with Capella Requirements
    - NalabsFeature
      - Capella feature project for the Nalabs plugin
    - UpdateSite
      - Update site project for the plugin, to build and publish JAR from for install in Capella
- NalabsNativeLib
  - Natively compiled interop library
- nalabs_shim
  - JAVA wrapper for the se.addiva.nalabs.interop library, to shield the plugin from Java Interop code.

Also see the nalabs_refactor repo and the Mdu.Nalabs project, that is the NALABS .NET implementation used by NalabsNativeLib.

See [NALABS Java Interop.drawio](doc/NALABS%20Java%20Interop.drawio) for a schematic over the component interaction of the system.

# Pre-requisites
* [.NET 8.0](https://dotnet.microsoft.com/en-us/download/dotnet/8.0): Either the SDK or the Runtime.
* Java JDK 19
  * The Java interop functions used will only run on JDK 19 and newer, but Capella can only be coaxed to use JRE 19.
  * Download JDK 19 from here: [JDK 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html). 
    Use the 19 version, not 19.0.1 or 19.0.2
* Jextract
  *[jextract v 19](https://jdk.java.net/jextract/19/)
* Capella Studio
  * [Capella Studio 7.0.0](https://www.eclipse.org/downloads/download.php?file=/capella/studio/products/releases/7.0.0)
* Capella
  * [Capella 7.0](https://mbse-capella.org/download.html)
    * Capella comes with JRE 17 installed in the install folder/jre. Remove or rename it to force Capella to start with the current JRE (which should be JRE 19)
    * Edit the capella.ini file, in the capella folder:
      * Change this setting to be: -Dpde.jreProfile=JavaSE-19
      * Add the following to the end of the ini:
      ```
      --enable-preview 
      --enable-native-access=ALL-UNNAMED
      ```
      to enable the Java preview functionality to execute (needed by the interop code)
  * [Capella Requirement Plugin](https://download.eclipse.org/capella/addons/requirements/updates/releases/0.14.0)

# System Components (How To Build)

![](doc/NALABS%20Java%20Interop.png)

1. NalabsNativeLib.
The NalabNativesLib component is the interop interface for Java to .NET, and is in fact a natively compiled library. It uses the Mdu.Nalabs .NET assembly which contain the NALABS Metrics that does the analysis of the text.
The Mdu.Nalabs is built as a NuGet package using the NALABS.sln that is located in the nalabs_refactor repo.
Put the Mdu.Nalabs.x.nuget package in a place on your local disc that the NuGet package manager can find it. The currently used version should be checked in in the NalabsNativeLib/pkg folder, so you can point a NuGet source to that folder.

    1.1. The NalabsNativeLib is built in several steps, to produce all the code that is needed for tha Capella plugin.
    First the native library has to be published and compiled for the target platform, run the following in a command prompt in the 
    NalabsNativeLib project folder:
    ```
    dotnet publish --runtime win-x64 --configuration release --self-contained false --force
    ```
    The resulting dll is placed in the bin\release\net8.0-windows10.0.17763.0\win-x64\publish folder of the NalabsNativeLib project folder.
    Copy NalabNativeLib.dll to a folder in somwhere in the system PATH.

    1.2. se.addiva.nalabs.interop.jar.
    This is the Java interop library for the NalabsNativeLib, and is built using jextract, javac and jar.
    Run the following commands in a command prompt in the NalabNativeLib's project folder:
    ```
    "c:\Program Files\jextract-19\bin\jextract.bat" --source --output jsrc --target-package se.addiva.nalabs.interop -l NalabsNativelib --header-class-name NalabsLib nalabslib.h

    "C:\Program Files\Java\jdk-19\bin\javac.exe" --release 19 --enable-preview .\jsrc\se\addiva\nalabs\interop\*.java

    "C:\Program Files\Java\jdk-19\bin\jar.exe" -c --file se.addiva.nalabs.interop.jar -C jsrc/ .
    ```

    The resulting se.addiva.nalabs.interop.jar is used by the se.addiva.nalabs.jar library and should be copied to the nalabs_shim folder of the capella repo.

    1.3. se.addiva.nalas.jar (capella/nalabs_shim folder).
    This is a "shim" for removing any dependencies to the JDK interop code from the Capella Plugin. It wraps all the interop code in a "pure" Java interface.
    Build it from the capella/nalabs_shim folder using the following commands in a command prompt:
    ```
    "C:\Program Files\Java\jdk-19\bin\javac.exe"  --enable-preview --release 19 -cp .;se.addiva.nalabs.interop.jar se/addiva/nalabs/RequirementAnalyzer.java se/addiva/nalabs/Requirement.java

    "C:\Program Files\Java\jdk-19\bin\jar.exe" -c --file se.addiva.nalabs.jar @se.addiva.nalabs.jarlist
    ```
1. Capella Plugin.
    The capella NALABS plugin is located in the capella/nalabs_plugin folder, open this folder as a workspace in Capella studio.
    The plugin is built by selecting the UpdateSIte/site.xml file and "Build All". This will produce the plugin feature and plugin in the UpdateSite folder. This folder can be used as an update site from Capella to install the plugin.

    The plugin is dependant on the se.addiva.nalabs.interop.jar and se.addiva.nalabs.jar librarys, and they should be placed in the   nalabs_plugin/Nalabs/lib folder, if rebuilt. The currently used versions should allready be in the folder as included in the repo.

# Installation process
To install and run the plug in in Capella (only tested on WIndows x64)
1. Copy the NalabsNativeLib.dll to a folder that is in the system PATH.
2. Copy the UpdateSite to some folder on the computer.
3. Start Capella.
4. Install the Capella Requirements add in.
5. You need to ha a Capella Type for your requirements
   1. Add a String attribute to your Requirement type.
      1. Long Name: Nalabs Analysis
      2. Type: String
   2. Open a Mass Editing View on the attribute and set:
      1. ReqIFIdentifier: NALABS
6. Open the "Install Software Dialog" (Help -> Install New Software)
   1. Click the Add button to add a new site to search for software
      1. Select Local, and select the UpdateSite folder.
      2. Select that site to "work with"
      3. It should show the "NALABS Requirement Analyis" feature, select that and press Next, and then Finish once loading completed.
      4. In the "Trust" dialog that pops up, select the "Allways trust all content" checkbox, and "Yes I accept the risk" in the following dialog, and then click the "Trust selected" button.
      5. Restart Capella when prompted to.
7. You should now have a NALABS menu in the menu bar.
   1. Select one or more Requirements and select the NALABS -> Analyze Requirement command.
   2. The "NALABS Analysis" atribute should now appear on the analyzed requirements, with the analysis result.
