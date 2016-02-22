#!/bin/bash
# Build the docker image
source common.sh
docker build -t $PRAQMA_ABS_JENKINS_DOCKER_TAG .
