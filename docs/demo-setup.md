# Demo Setup

Describes how to build and run the service.


## Functionality
* The service accepts HTTP requests on a configurable port. This is the URL that
  Bitbucket should POST to.
* The service triggers a Jenkins job when receiving a request. This should be a Job DSL
  seed job that can create a pipeline.

Restrictions:
* Deleting jobs is not supported. The service currently treats all requests as creation,
  that is, the service triggers a Jenkins seed job


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
Start Jenkins in Docker using a command like this:

```sh
$ docker run -p 8080:8080 -p 50000:50000 -v /some/local/folder:/var/jenkins_home jenkins
```

See also [Jenkins on Docker Hub](https://hub.docker.com/_/jenkins/)

Jenkins must have a job that the service can trigger.
These Jenkins properties are specified in the service configuration:

* Jenkins URL
* The name of the Jenkins job to trigger


## Set Up Bitbucket Server / Stash
Start Bitbucket Server in Docker using commands like these:

```sh
$ docker run -u root -v /data/bitbucket:/var/atlassian/application-data/bitbucket \
         atlassian/bitbucket-server chown -R daemon  /var/atlassian/application-data/bitbucket
$ docker run -v /data/bitbucket:/var/atlassian/application-data/bitbucket \
         --name="bitbucket" -d -p 7990:7990 -p 7999:7999 atlassian/bitbucket-server
```

See also [docker-atlassian-bitbucket-server](https://bitbucket.org/atlassian/docker-atlassian-bitbucket-server)

Initialize Bitbucket:
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
java -jar build/libs/automated-branch-pipelines-0.1.0.jar
```


## Demo Workflow
This workflow shows the service in action:

* Clone the Bitbucket repository somewhere local
* Add/edit a file and commit it
* Push to Git. This should trigger:
  * A log message in the service that a request has been received from the Bitbucket hook
  * A build of the seed job on Jenkins


## Automated Acceptance Test
The automated test is in this file:

```sh
src/test/bash/acceptanceTest.sh
```

It assumes that:
* Jenkins is running
* The service is running on a specific URL

The test can be run using Gradle:

```sh
$ ./gradlew build
```

The test uses `curl` to POST a request to the service and the service response is shown
in the console.
