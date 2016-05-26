# Research on existing work

Notes about existing work in the area of automatically creating build jobs.
This is for Jenkins only.

## Conclusion
If we want the design to support multiple CI systems, we cannot rely on
Jenkins-specific solutions.

The Multi-Branch Project plugin achieves part of what we want, namely the
automatic scheduling of jobs when new branches are created, and deleting jobs
tied to dead branches. However, it is Jenkins-specific. It also lacks support
for having different pipelines based on branch name patterns.

## Multi-Branch Project Plugin
A Jenkins plugin to set up common build steps for a set of branches in
the same repository. Thus, it can define a pipeline to be reused across branches.

Project options, build triggers, environment, steps are applied to each
sub-project.

One multi branch job only defines one job. This could be a Job DSL seed job, which
means one multi branch job can define one pipeline.

But we still have to be able to differentiate the pipeline based on the branch
name. A multi branch job can use patterns for branches to include and to exclude.
So we could create one type of multi branch job for, say, "feature/*", and have
it create one kind of pipeline. The contents of that pipeline could be defined
by some external configuration.

What would we gain?

The alternative is to call our own seed job, which would build a pipeline based
on external configuration.
Both ways would allow the common definition of jobs across branches.

A benefit of the multi branch project is that it would listen to SCM updates
and trigger jobs for new branches automatically.
It is also able to discard dead branches (although the definition of a dead
branch may not be what we want).
As such, there would be no need for our service in between to listen for
branch creation and deletion.

However, this is a Jenkins-specific solution to listen for SCM updates.
Further, we need to apply some configuration to describe a pipeline based on
branch name patterns. We would probably use Job DSL for that regardless of
whether we use the multi-branch project or not.

The plugin is quite new (as they also state on the wiki). It could evolve in the
future, and we could even contribute to it. But it would still be
Jenkins-specific.

## CloudBees CD Workflow/Pipeline
Adds pipeline features such as execution, forking and parallel.
Jenkins specific.

## Jenkins Build Per Branch
The functionality and idea looks similar. But it is only for Jenkins and Git.
It also looks pretty dev oriented, for example requiring a Git clone of the
"build per branch" project repo.

## Jenkins Autojobs
Again specific to Jenkins, but supports multiple SCMs.
Does not use Job DSL.
Not sure if its API will meet our needs.

## Jenkins Job Builders
Looks like a good way to scale Job DSL.
Not sure if it makes sense to use for us.

## Jenkins Build Pipeline Plugin
It would be cool to have auto-created jobs linked together in a build pipeline
if the plugin is installed.
