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
- nalabs_core
  - NALABS metrics and analysis tools

# Pre-requisites
* Java JDK 19
  * Capella can only be coaxed to use JRE 19.
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

# Installation process for nalabs_core

To rebuild the JAR for nalabs_core, do as follows: In Capella, right-click on the nalabs_core project and select "Export" -> "Java" -> "JAR file". Make sure "Export generated class files and resources" is selected, and output (preferably) to the JAR file destination `nalabs_core\se.addiva.nalabs_core.jar` with options "Compress the contents of the JAR file". Select "Finish".

To use this file from nalabs_plugin, make sure the generated JAR file has been copied to `nalabs_plugin/Nalabs/lib/`.

# Installation process for nalabs_plugin
To install and run the plug-in in Capella (only tested on Windows x64)
1. Copy the UpdateSite to some folder on the computer.
2. Start Capella.
3. Install the Capella Requirements add in.
4. Right-click for example on the `Logical Architecture` box in your Capella project and select `Add Capella Element...`->`Capella Types folder`. Click on the `[Capella Types folder]` that was just generated and create `Requirement Type`.  
   1. Add a String attribute to your Requirement type.
      1. Long Name: Nalabs Analysis
      2. Type: String
   2. Open a Mass Editing View on the attribute and set:
      1. ReqIFIdentifier: NALABS
5. Make sure any Requirements created in your Capella project have the Requirement type you just created. To manually create a Requirement, do as follows:
   1. If there is no `Capella Module`, create one (`Add Capella Element...`->`Capella Module`).
   2. In the `[Capella Module]`, right-click and create a `Requirement`.
   3. Double-click the created Requirement, give it a `Long name`, and set the necessary requirement description in the `Text` tab.
   4. Finally, right-click the requirement, and select `Send to mass editing view`, where you fill in the `ReqIFIdentifier` field with some unique number.
6. Open the "Install Software Dialog" (Help -> Install New Software)
   1. Click the Add button to add a new site to search for software
      1. Select Local, and select the UpdateSite folder.
      2. Select that site to "work with"
      3. It should show the "NALABS Requirement Analyis" feature, select that and press Next, and then Finish once loading completed.
      4. In the "Trust" dialog that pops up, select the "Allways trust all content" checkbox, and "Yes I accept the risk" in the following dialog, and then click the "Trust selected" button.
      5. Restart Capella when prompted to.
7. You should now have a NALABS menu in the menu bar.
   1. Select one or more Requirements and select the NALABS -> Analyze Requirement command.
   2. The "NALABS Analysis" attribute should now appear on the analyzed requirements, with the analysis result.

# Debugging

In order to debug the nalabs_plugin, start Capella Studio, and select the `nalabs_plugin` folder as your Workspace. Next, open the nalabs_plugin project, then select `Run` in the menu and `Debug Configurations...`. Next, select `Eclipse Application` and the `Arguments` tab. In that tab, add `-data "<project_workspace>"` to `Program Arguments`, and `--enable-preview --enable-native-access=ALL-UNNAMED` to `VM Arguments`. Then select `Apply`. Now you can debug the plugin.

# Using the plugin

To use the plugin, select a number of requirements, then click `NALAB`->`Analyze Requirements` in the top bar menu. The results will show in a tab named `Requirement Smell Detector`.
