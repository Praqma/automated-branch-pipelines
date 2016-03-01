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
* Mac OS or Linux, because scripts are written in `bash`.
* Java JRE 8, for running the service.

The source code is built using the Gradle wrapper, so the very first build will download
Gradle stuff. You do not need to have Gradle installed.

The demo assumes that Jenkins and Bitbucket are available. This document provides Docker
examples on setting up these systems. All Docker examples assume that the `docker` command
is ready. That is, you should have a running Docker machine set up something like this:

```sh
$ docker-machine start default
$ eval $(docker-machine env default)
```


## Demo Steps
The branch pipeline service is configured with settings about the CI server (Jenkins) and
the SCM server (Bitbucket), so the order of things is:

* Set up Jenkins
* Set up Bitbucket
* Configure and start the branch pipeline service

Once all components are running, you can execute manual or automated tests.


## Set Up Jenkins
The folder `docker/jenkins` contains scripts to build and run a customized Jenkins as a
Docker container.
Build and run it using these scripts:

```sh
$ ./docker/jenkins/build.sh
$ ./docker/jenkins/run.sh
```

This will start a Jenkins instance containing a seed job that uses Job DSL to create a
pipeline.

These Jenkins properties are specified in the service configuration:

* Jenkins URL
* The name of the Job DSL seed job to trigger when creating a pipeline
* The names of the jobs that constitute a pipeline


## Set Up Bitbucket Server
Bitbucket needs to be set up with a project and a repository, and have an add-on
installed for posting HTTP requests to the branch pipeline service.

This document describes the settings needed for running the automated tests.

Start Bitbucket in Docker using the script in `docker/bitbucket`:

```sh
$ ./docker/bitbucket/run.sh
```

See also [docker-atlassian-bitbucket-server](https://bitbucket.org/atlassian/docker-atlassian-bitbucket-server)

Initialize Bitbucket:
* Access Bitbucket on port `7990`, for example `http://192.168.99.100:7990`
* Set up database and license
* Create a user called `admin`
* Create a project with a repository:
  * Name the project `Test Abs`, so the project key becomes `TA`
  * Name the repository `test-abs`
* Install and configure the hook add-on as described in [bitbucket-hook-config](bitbucket-hook-config.md)


## Configure the Branch Pipeline Service
The branch pipeline service must be configured by editing this file:

```sh
config/branch-pipeline.yml
```

The configuration is read when starting the service.


## Run the Branch Pipeline Service
The service is started using Gradle:

```sh
./gradlew run
```

Notice that this task does not finish until you stop the server with `Ctrl+C`.


## Demo Workflow
This workflow shows the service in action:

* Clone the Bitbucket repository somewhere local
* Create a feature branch
* Add/edit a file and commit it
* Push to Git. This should trigger a build of the seed job on Jenkins
* Push a delete of the feature branch remote. This should trigger deletion of the
  pipeline jobs on Jenkins

If something goes wrong, take a look in the server log.


## Automated Acceptance Tests
The automated tests are located in [src/test](src/test).

They assume that Jenkins, the Java service and Bitbucket are all running and configured.
They make assumptions about the configuration, such as Docker machine names and Bitbucket
repository.

The tests can be run using Gradle:

```sh
$ ./gradlew acceptanceTest
```
