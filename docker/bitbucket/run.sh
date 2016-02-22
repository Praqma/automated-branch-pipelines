#!/bin/bash
docker rm praqma_abs_bitbucket
# Set permissions for the data directory
docker run -u root -v /data/bitbucket:/var/atlassian/application-data/bitbucket \
       atlassian/bitbucket-server chown -R daemon  /var/atlassian/application-data/bitbucket
# Start the container
docker run -v /data/bitbucket:/var/atlassian/application-data/bitbucket \
       --name praqma_abs_bitbucket -d -p 7990:7990 -p 7999:7999 atlassian/bitbucket-server
