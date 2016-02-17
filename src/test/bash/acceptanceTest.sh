#!/bin/bash

# Assumes the working dir is the project root.
# This is the case when this script is executed by Gradle.
SERVICE_PORT=$(grep "service.port" src/main/resources/automated-branch-pipelines.properties | cut -d "=" -f 2 | tr -d ' ')
SERVICE_URL=http://localhost:$SERVICE_PORT

echo POSTing request to $SERVICE_URL
curl -o- $SERVICE_URL
