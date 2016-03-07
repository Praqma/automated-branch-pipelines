FROM jenkins:1.642.2
MAINTAINER "martin.amdisen@praqma.com"

# Install Jenkins plugins
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

# Prepare for creation of seed job:
# The job is created by running a custom script in /usr/share/jenkins/init.groovy.d
# It uses an XML file to define the seed job.
COPY seed-job/config.xml /usr/share/jenkins/seed-job-config.xml
COPY seed-job/seed-dsl.groovy /usr/share/jenkins/seed-dsl.groovy
COPY seed-job/create-seed-job.groovy /usr/share/jenkins/ref/init.groovy.d/
