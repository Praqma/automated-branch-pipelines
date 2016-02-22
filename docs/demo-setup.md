# Demo Setup

Describes how to build and run the service.


## Functionality
* The service accepts HTTP requests on a configurable port. This is the URL that
  Bitbucket should POST to.
* The service triggers a Jenkins job when receiving a request signaling branch creation.
  This should be a Job DSL seed job that can create a pipeline.
* The service calls Jenkins to delete pipeline jobs when receiving a request signaling
  branch deletion.


## Prerequisites
The source code is built using the Gradle wrapper, so the very first build will download
Gradle stuff. You do not need to have Gradle installed.

The demo assumes that Jenkins and Bitbucket are available. This document provides Docker
examples on setting up these systems. All Docker examples assume that the `docker` command
is ready. That is, you should have a running Docker machine set up something like this:

```sh
$ docker-machine start default
$ eval $(docker-machine env default)
```

## Set Up Jenkins
The folder `docker/jenkins` contains scripts to build and run a customized Jenkins as a
Docker container.
Build and run it using these scripts:

```sh
$ docker/jenkins/build.sh
$ docker/jenkins/run.sh
```

This will start a Jenkins instance containing a seed job that uses Job DSL to create a
pipeline.

These Jenkins properties are specified in the service configuration:

* Jenkins URL
* The name of the Jenkins job to trigger
* The names of the jobs that constitute a pipeline


## Set Up Bitbucket Server / Stash
Start Bitbucket Server in Docker using the script in `docker/bitbucket`:

```sh
$ docker/bitbucket/run.sh
```

See also [docker-atlassian-bitbucket-server](https://bitbucket.org/atlassian/docker-atlassian-bitbucket-server)

Initialize Bitbucket:
* Access Bitbucket on port `7990`, for example `http://192.168.99.100:7990`
* Set up database and license
* Create a project with a repository
* Install and configure the hook add-on as described in [bitbucket-hook-config](bitbucket-hook-config.md)


## Configure Before Build
Currently, the service reads configuration from an embedded properties file.
This file must be modified before building the code:

```sh
src/main/resources/automated-branch-pipelines.properties
```

## Build
Build the service as a jar file using Gradle:

```sh
$ ./gradlew jar
```

## Run Demo
Assuming that Jenkins and Bitbucket Server are both running, the service is started like
this:

```sh
./gradlew run
```

Notice that this task does not finish until you stop the server with Ctrl+C.


## Demo Workflow
This workflow shows the service in action:

* Clone the Bitbucket repository somewhere local
* Add/edit a file and commit it
* Push to Git. This should trigger:
  * A log message in the service that a request has been received from the Bitbucket hook
  * A build of the seed job on Jenkins


## Automated Acceptance Tests
The automated tests are located in [src/test](src/test).

They assume that Jenkins, the Java service and Bitbucket are all running and configured.

The tests can be run using Gradle:

```sh
$ ./gradlew acceptanceTest
```
