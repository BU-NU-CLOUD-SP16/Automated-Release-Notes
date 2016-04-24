# Automated-Release-Notes 
This is an Open Source Project done by the students of Boston University.Releasing software with a Continuous Integration (CI) Pipeline can greatly increase the speed with which code gets released.This is great for developers to determine the source of a bug in a particular build, but the source control does not hold much value when it comes to communicating the business content of the build. When a build gets deployed, the business wants to know what the content of the build is, so that they know what to test, as well as what can be released to production. This often means a manual step in the CI process is necessary: the developer must look up the work items in the work item tracking software, and enter them manually into the release tracking software. The goal of this project is to eliminate that manual step of entering release notes by integrating the build pipeline with Work Item Tracking software. 

# Features 
Plugin is an efficient tool to eliminate the manual step of entering release notes by integrating the build pipeline(Teamcity) with work item tracking software(Visual Studio). 

# Installation
* Download the plugin build (binaries) from https://docs.google.com/a/bu.edu/uc?authuser=0&id=0B2FiyfgYqIm8SjU3eFh5eWh4aFU&export=download
* Make sure downloaded .zip file is not corrupted
* Put the downloaded plugin .zip file into <TeamCity Data Directory>/plugins folder
* Restart the TeamCity Server
* Open Administration | Plugins and check you see the plugin listed

For more details, there is [documentation](http://confluence.jetbrains.net/display/TCD7/Installing+Additional+Plugins)

# Building
 * Download project files from the download zip button of this repository,you will have a downloaded file: Automated-Release-Notes-master.zip .
 * Extract the zip file and open the project in IntelliJ IDEA.
 * Add TeamCity distribution path variable, with Name:TeamCityDistribution and Path:Teamcity home directory.
 * Build all artifacts.
 * Once all artifacts are built, you will have a zip file generated in the folder where you extracted the Automated-Release-Notes-master.zip  in Automated-Release-Notes-master\out\artifacts\plugin_zip .
 * Upload the ARNPlugin.zip file in Teamcity as a plugin, add a build step with ARNRunner and run the build.

# In this repo you will find 
** 



