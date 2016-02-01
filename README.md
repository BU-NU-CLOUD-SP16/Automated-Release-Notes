# Automated-Release-Notes

This is an Open Source Project done by the students of Boston University in the course of Cloud Computing (EC 500 A), 
under the guidance of Samuel Howes, Luke Hanscom,Orran Krieger, Peter Desnoyers and Ata Turk.

Releasing software with a Continuous Integration (CI) Pipeline can greatly increase the speed with which code gets released.
CI Pipelines can largely replace manual processes of releasing. 
When it comes to the content of a release however, most tools focus on relating each build to a set of changes in source control. 
This is great for developers to determine the source of a bug in a particular build,
but the source control does not hold much value when it comes to communicating the business content of the build.

When a build gets deployed, the business wants to know what the content of the build is, so that they know what to test, as well as what can be released to production.
This often means a manual step in the CI process is necessary: the developer must look up the work items in the work item tracking software, and enter them manually into the release tracking software.
The goal of this project is to eliminate that manual step of entering release notes by integrating the build pipeline 
with Work Item Tracking software.
