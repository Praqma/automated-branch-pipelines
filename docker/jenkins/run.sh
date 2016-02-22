#!/bin/bash
# Run the docker image
source common.sh
docker rm praqma_abs_jenkins
docker run -p 8080:8080 -p 50000:50000 --name praqma_abs_jenkins $PRAQMA_ABS_JENKINS_DOCKER_TAG
