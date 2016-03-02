# TODO List

Ideas for future improvement.

## Documentation
* Describe Job DSL requirements, for example that job names need to match
  `${jobPrefix}_${branchPrefix}_${branchName}`
* Document interface from SCM to the branch pipeline service

## Functional
* Make it possible to pass extra seed job parameters in the Bitbucket hook POST data.
  Otherwise users are forced to create a seed job with exactly the build parameters
  used by the branch pipeline service.

## Technical
* Create a CD pipeline for this project
* Run as standalone jar, i.e., without Gradle
* Script to check consistency between Job DSL seed job and YAML configuration

## Demo
* Jenkins init.groovy script: Create seed job in code instead of from XML
* Add upstream build triggers to Job DSL, taking into account which jobs are part of a
  pipeline for a branch
