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

JENKINS_URL=$(grep "ciUrl:" config/branch-pipeline.yml | cut -d " " -f 6)

# A feature branch created and deleted by this test
BRANCH_PREFIX=feature/
FEATURE_NAME=1337-coolfeature
FEATURE_BRANCH=$BRANCH_PREFIX$FEATURE_NAME

# The branch name is sanitized by the service
CI_FRIENDLY_FEATURE_NAME=$(echo $FEATURE_NAME | tr - _)
# Job URL that is polled to assert that pipeline has been created or deleted
JOB_URL=$JENKINS_URL/job/commit_$CI_FRIENDLY_FEATURE_NAME/

# Test dir is only used by this test
TEST_DIR=/tmp/automated-branch-pipelines/acceptTest
# Modify these settings to match the Bitbucket setup
# Repository settings matches a project with key "TA" (like "Test Abs")
PROJECT_KEY=ta
REPO=test-abs
BITBUCKET_USER=admin
BITBUCKET_IP=$(docker-machine ip default)
BITBUCKET_CLONE_URL=http://$BITBUCKET_USER@$BITBUCKET_IP:7990/scm/$PROJECT_KEY/$REPO.git

echo Creating new test directory $TEST_DIR
rmdir -rf $TEST_DIR
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
git checkout -b $FEATURE_BRANCH
echo hi feature > file1
git add file1
git commit -m "update file on feature branch"
git push -u origin $FEATURE_BRANCH

# Poll Jenkins to see that jobs were created
for i in `seq 1 10`;
do
  echo Polling for created job at $JOB_URL
  JOB_STATUS=$(curl -I $JOB_URL 2>/dev/null | head -n 1 | cut -d ' ' -f 2)
  echo $JOB_STATUS
  if [ $JOB_STATUS -eq "200" ]
    then break
  fi
  sleep 2
done

# Assert that job was in fact created
if [ $JOB_STATUS -ne "200" ]
  then
    echo Error: Job status should be 200, but was: $JOB_STATUS
    exit 1
fi

echo Merging feature branch to master
# Should not trigger Jenkins
git checkout master
git pull
git pull origin $FEATURE_BRANCH
git push origin master

echo Deleting feature branch
# Should trigger Jenkins
git push origin :$FEATURE_BRANCH
git branch -d $FEATURE_BRANCH

echo Deleting test directory $TEST_DIR
rm -rf $TEST_DIR

# Assert that jobs were deleted
for i in `seq 1 10`;
do
  echo Polling for deleted job at $JOB_URL
  JOB_STATUS=$(curl -I $JOB_URL 2>/dev/null | head -n 1 | cut -d ' ' -f 2)
  echo $JOB_STATUS
  if [ $JOB_STATUS -eq "404" ]
    then break
  fi
  sleep 2
done
if [ $JOB_STATUS -ne "404" ]
  then
    echo Error: Job should have been deleted, but response code was: $JOB_STATUS
    exit 1
fi

echo Done
