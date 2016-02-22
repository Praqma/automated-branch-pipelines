#!/bin/bash

# Given:
# Jenkins, the Java service and Bitbucket are all running.
# Bitbucket is configured with a project, repository and hook.
#
# When:
# A feature branch is created or deleted in the Git repository.
#
# Then:
# Jenkins is triggered via the service to create or delete a pipeline.
#
# Assumes the working dir is the project root.
# This is the case when this script is executed by Gradle.

# Test dir is only used by this test
TEST_DIR=/tmp/automated-branch-pipelines/acceptTest
# Modify these settings to match the Bitbucket setup
# Repository settings matches a project with key "TA" (like "Test Abs")
PROJECT_KEY=ta
REPO=test-abs
BITBUCKET_USER=admin
BITBUCKET_IP=$(docker-machine ip default)
BITBUCKET_CLONE_URL=http://$BITBUCKET_USER@$BITBUCKET_IP:7990/scm/$PROJECT_KEY/$REPO.git

echo Creating test directory $TEST_DIR
mkdir -p $TEST_DIR
cd $TEST_DIR

echo Cloning Git repository from Bitbucket
git clone $BITBUCKET_CLONE_URL
cd $REPO

echo Pushing a change on master
# Should not trigger Jenkins
echo hi > file1
git add file1
git commit -m "add file"
git push origin master

echo Creating a feature branch and pushing to it
# Should trigger Jenkins
git checkout -b feature/1337-coolfeature
echo hi feature > file1
git add file1
git commit -m "update file on feature branch"
git push -u origin feature/1337-coolfeature

echo Merging feature branch to master
# Should not trigger Jenkins
git checkout master
git pull
git pull origin feature/1337-coolfeature
git push origin master

echo Deleting feature branch
# Should trigger Jenkins
git push origin :feature/1337-coolfeature
git branch -d feature/1337-coolfeature

echo Deleting test directory $TEST_DIR
rm -rf $TEST_DIR

echo Done
