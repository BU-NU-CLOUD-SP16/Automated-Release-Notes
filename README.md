# Automated-Release-Notes 
This is an Open Source Project done by the students of Boston University.Releasing software with a Continuous Integration (CI) Pipeline can greatly increase the speed with which code gets released.This is great for developers to determine the source of a bug in a particular build, but the source control does not hold much value when it comes to communicating the business content of the build. When a build gets deployed, the business wants to know what the content of the build is, so that they know what to test, as well as what can be released to production. This often means a manual step in the CI process is necessary: the developer must look up the work items in the work item tracking software, and enter them manually into the release tracking software. The goal of this project is to eliminate that manual step of entering release notes by integrating the build pipeline with Work Item Tracking software. 

# Features 
Plugin is an efficient tool to eliminate the manual step of entering release notes by integrating the build pipeline(Teamcity) with work item tracking software(Visual Studio). This plugin is able to parse commit messages from Source Control for work items and get data from Work Item Tracking Software(VSTS) to output the work item information to a text file. 

# Installation
* Download the plugin build (binaries) from https://github.com/BU-NU-CLOUD-SP16/Automated-Release-Notes/releases/download/ARN1.0/ARNPlugin.zip
* Make sure downloaded .zip file is not corrupted
* Put the downloaded plugin .zip file into <TeamCity Data Directory>/plugins folder
* Restart the TeamCity Server
* Open Administration | Plugins and check you see the plugin listed

For more details, there is [documentation](http://confluence.jetbrains.net/display/TCD7/Installing+Additional+Plugins)

# Building
 * Download project files from the download zip button of this repository,you will have a downloaded file: Automated-Release-Notes-master.zip . 
 * Or use git clone: 
```console
git clone https://github.com/BU-NU-CLOUD-SP16/Automated-Release-Notes
```
 * Extract the zip file and open the project in IntelliJ IDEA.
 * Add TeamCity distribution path variable, with Name:TeamCityDistribution and Path:Teamcity home directory.
 * To build the plugin build plugin-zip which you will find here : Build -> Build Artifacts -> plugin-zip
 * Once plugin-zip artifact is built, you will have a zip file generated in the folder where you extracted the Automated-Release-Notes-master.zip  in Automated-Release-Notes-master\out\artifacts\plugin_zip .
 * Upload the ARNPlugin.zip file in Teamcity as a plugin, logout and restart Teamcity server .
 
# Configuration
 * Once Teamcity server is restarted,go to your project in teamcity then go to Edit Project settings and click on Build inside the Build Configurations. The Build configuration settings will now open, click on Build steps and then add build step.
 * Choose ARN runner as your 'Runner Type', which is an ANT type runner.  
 * For 'File Path relative to Checkout Directory' choose a Folder name where you want the release notes to be generated.This file will be generated relative to the [checkout directory](https://confluence.jetbrains.com/display/TCD9/Build+Checkout+Directory).
 * Provide appropriate VSTS url,username and password.
 * Select the release notes file format ie. doc,pdf or text type. Enter format string, for more information on format string click on the button 'information about format string', disable ad blocker or pop-up blocker if the button doesn't function. If the textbox is left empty you will have default parameters generated in your text file.
 * Save the build step configuration and run the build.
  The build step configuration should look like this:
 ![Build step Configuration](https://github.com/BU-NU-CLOUD-SP16/Automated-Release-Notes/blob/master/docs/BuildSteppng.png)

# In this repo you will find 
* TeamCity server and agent plugin bundle.
* pre-configured IDEA settings to support references to TeamCity
* Uses $TeamCityDistribution$ IDEA path variable as path to TeamCity home (unpacked .tar.gz or .exe distribution)
* Bunch of libraries for most recent needed TeamCity APIs

Supported Versions
==================

* Plugin is tested to work with TeamCity 9.1.x 
* Agent and server are expected to run JRE 1.7

