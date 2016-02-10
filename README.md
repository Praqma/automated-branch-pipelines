# automated-branch-pipelines
Makes feature branching and continuous delivery play well together by automatically applying parts of your build and continuous delivery pipeline to your branches.

__Features:__
* Automatically create temporary pipeline for a new branch in your SCM
* Automatically clean up the temporary pipeline when branch is merged
* Configure which parts of the ordinary pipeline will be applied for branches

## Motivation
If you are using (git) scm development workflows that propose feature branches, like [The Automated git-flow](http://www.josra.org/blog/An-automated-git-branching-strategy.html),  and you have a mature automated process in place with established build pipelines, you will most likely also wish to reuse part of the process temporarily on your branches until they are delivered.

Usual situations where you are isolated and want parts of the build pipeline and automation applied could be features branches for the second next upcoming release.

Another common use-case is that there is some limited resource, e.g. a test bench or test robot, that has been assigned to your build system. If developers need to use these resources they have to go through the build system. Following for example a simple naming convention for branches applies the test job to those on the build systems and utilized the resources for testing.

## General prerequisites
* a scm supporting branches
* a repository server supporting triggers on creation/deletion of branches
* a continuous delivery and build pipeline described as code that can be provisioned

## Design
Currently our design idea focuses on reusability across different build systems and SCMs so they are abstracted by a service. This will help getting a larger userbase and share development effort in the future. It also make the solution more tool agnostic.

* __Repository trigger__: Triggers for the repositories supported that can invoke the branch pipeline service with the needed information.

* __Branch pipeline service__: A generic service running that the triggers will invoke. The service will create, delete and maintain the temporary build pipeline (a dockerized service accepting JSON could be a choice). This service abstracts the differences between SCMs and build systems, and they way the build flow is described. It offers a generic interface to the trigger, and understand the different build systems using SCM enabler and build systems enablers.

* __Branch pipeline configuration file__: A general purpose configuration file, human readable and easy to merge (YML could be a one such choice). This is not the build pipeline and workflow description used, e.g Jenkins Job DSL.

![Overview of initial design idea](/docs/images/automated-branch-pipeline-simple-design-idea.png)

### Misc design considerations
* FAL - Flow Abstraction Layer. It is supposed to abstract software development flow descriptions, like build pipelines etc. It is a future idea but we might plan for it already in our design. It could also be visualized in the future. The branch pipeline service could end up being the FAL.
* Praqma have through [Josra](http://www.josra.org) an initiative around traceability in software development, using a message queue and producers and consumers of messages. The above design could be seen to also fit a consumer/producer approach in such a setup though it is not within scope of this project. The “moving” part will be almost the same.
* The configuration file for branch patterns and how to trigger the creation of a new build flow should be version controlled in the projects. So either the trigger or service need to able to read it. 
* Describing a proper front-end design is definitely worth spending time on. If we want this to turn out to be a flexible solution spanning multiple build and SCM systems, we have to make sure its configuration has a recognizable vocabulary, yet isn’t biased towards certain technologies.

## Proof-of-concept - 1st implementation
A limited implementation using specific technologies to explore the workflow and use-case.

It will be using Git and Stash as SCM, Jenkins as build system, Jenkins Job DSL to describe the continuous delivery and build pipeline. Scripting will be groovy as this is already heavily used in those technologies.


### Features and workflows supported
* When a branch with a certain naming convention (eg. feature/* or auto/*) is pushed to Stash, a new temporary build pipeline for that branch is created.
* A configuration will describe, pr. branch naming convention, which part of the continuous delivery and build pipeline is applied to the branch.
* When branch is successfully merged and pushed, the temporary pipeline is deleted.

## Related work
* Jenkins Workflow from Cloudbees
* [Jenkins Multi Branch Project Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Multi-Branch+Project+Plugin)
* [Jenkins build pr. branch](http://entagen.github.io/jenkins-build-per-branch/)
* [Jenkins auto-jobs](https://github.com/gvalkov/jenkins-autojobs)
* [Jekins job builders](https://github.com/hmrc/jenkins-job-builders)

## Other references

* http://zeroturnaround.com/rebellabs/things-to-consider-when-featuring-branching-with-continuous-integration/
