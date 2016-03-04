# TODO List

Ideas for future improvement.

## Documentation
* Describe Job DSL requirements, for example that job names need to match
  `${jobPrefix}_${branch}`
* Document interface from SCM to the branch pipeline service

## Functional and Architectural
* Make it possible to pass extra seed job parameters in the Bitbucket hook POST data.
  Otherwise users are forced to create a seed job with exactly the build parameters
  used by the branch pipeline service.
* One configuration file per project. Each project pipeline may be different and the
  configuration is typically placed within the project. The solution could be to add
  a configuration file/URL to the JSON sent in the SCM request.
* Consider a use case for having the service functionality as a Jenkins plugin. This does
  raise new issues to consider, like what to trigger.
* Consider handling of deleting jobs. Today, there is a tight coupling between the Java
  service and the Job DSL in how jobs must be named. This is because the Java service
  constructs a job URL with /doDelete in order to delete the jobs.
  This could also be a problematic design. It might be better to have Job DSL delete the
  jobs. This could be done by passing an action ('create'/'delete') to Job DSL and using
  the hudson.model API to explicitly delete jobs, something like this:

```sh
import hudson.model.*
def hudson = Hudson.getInstance()
def jobNames = hudson.getJobNames()
def job = hudson.getItem(it)
job.delete()
```

## Technical
* Create a CD pipeline for this project
* Run as standalone jar, i.e., without Gradle
* Script to check consistency between Job DSL seed job and YAML configuration

## Demo
* Apply best practices from
  [JenkinsAsCodeReference](https://github.com/Praqma/JenkinsAsCodeReference), in
  particular Jenkins init.groovy script: Create seed job in code instead of from XML
* Add upstream build triggers to Job DSL, taking into account which jobs are part of a
  pipeline for a branch
