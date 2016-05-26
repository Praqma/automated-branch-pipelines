# Research on issue #8 - Project based configuration

There is a requirement that the part of the configuration that belongs to a project is
not coupled to the ABP service. Thus, it should live in a configuration file in the
project's repository.

Moving this configuration will achieve a few things:

* The responsibility of the project configuration is on the project members
* Projects will not interfere with each other and the ABP service does not need to be
  restarted on project changes
* There will be VCS history of the configuration
* The service configuration file does not grow very large


## Background

Project specific configuration includes:

* CI server URL
* Jenkins seed job name
* Definition of pipelines: Map from branch prefix to a list of job names


## Questions

* How important is the abstraction of CI server / build system? This would be pretty
  easy to solve if we focused on Jenkins only. But if we focused on Jenkins, the ABP
  service becomes obsolete.
* If we want to keep the ABP service and the build system abstraction, is it worth
  considering a Jenkins specific solution using the same library code? It might be an
  easier setup to use for some customers.


## Approach 1: Add configuration to Bitbucket REST hook

The SCM (Bitbucket) is the system sending requests to the ABP service. It currently
passes JSON containing things like repository and branch name. It could be extended to
include the project configuration.

Then the JSON payload would in essence become the project configuration file stored in
SCM.

Pros:
* Easy to adjust ABP service to handle this

Cons:
* Harder to setup. Even if the Bitbucket hook can be configured programmatically (via
  Bitbucket's REST API), project's would need a script to send the configuration to
  Bitbucket once for every configuration change. Otherwise, this would have to be done
  manually in the Bitbucket UI.


## Approach 2: The ABP service uses SCM to read the configuration

Even though the configuration is project specific, it is interpreted and used by the ABP
service. Thus, the ABP service could be extended to read from a project's SCM repository.

Pros:
* There is no issue with how to pass the information to the ABP service.

Cons:
* Git/SCM client has to be installed on the system running the ABP service, or
  implemented in Java by the ABP service.
* SCM access: The system running the ABP service needs network access to each project's
  repository. This may not be a problem in practice, since the build system also needs
  this access.
* SCM configuration: This will add to the service configuration a list of
  projects that it can handle and the projects repository details. We would also have to
  decide on a naming convention for the project configuration file (like how Jenkins 2.0
  uses the `Jenkinsfile`).


## Approach 3: The configuration is read when running the job

Since the configuration is in SCM, it could just be read when running the build job on
Jenkins, using Job DSL.

Pros:
* Easy to setup using Jenkins. The system architecture becomes simpler because the
  ABP service component is no longer needed.

Cons:
* The ABP service would become obsolete. Its functionality would be implemented in Job
  DSL or a Jenkins plugin instead.
* The solution will only be able to handle Jenkins as build system.


## Conclusion

If we want to keep the build system abstraction, the choice is either the 1st or 2nd
approach.

The 1st approach requires few implementation changes, but introduces a manual
setup step that has to be carried out whenever a project's pipeline changes.

The 2nd approach still requires a bit of project configuration in the service, namely how
to access the SCM to read the configuration. However, this should be a one-time setup.
Otherwise, it requires little from the projects using the ABP service, just that the
configuration file follows a naming convention. It does require a bit of implementation
changes in the ABP service.

If the top priority is to satisfy a customer that is fine with locking in to Jenkins,
then the 3rd approach provides the simplest user experience. It could be combined with
still having the ABP service for other use cases, by sharing some of the Java code in a
library.
