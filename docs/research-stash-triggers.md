# Research Stash Triggers

Notes about existing Stash triggers and how they relate to this project.

In the following, Stash is treated identical to Bitbucket Server, which is the
new name.

Also, triggers means repository hooks, that is, scripts that can be triggered to
run based on repository operations.

The research is focused on what is supported in Stash, not pure Git.


## Conclusion
We should use a Stash post-receive hook (as opposed to a pre-receive hook).

There are at least two existing hook plugins that look promising:
* [HTTP Request Post-Recieve Hook for Stash](https://marketplace.atlassian.com/plugins/de.aeffle.stash.plugin.stash-http-get-post-receive-hook/server/overview)
* [Bitbucket Server Web Post Hooks Plugin](https://marketplace.atlassian.com/plugins/com.atlassian.stash.plugin.stash-web-post-receive-hooks-plugin/server/overview)

They both send HTTP requests in their own format. This means our service must be
able to parse such requests and recognize them as coming from Stash SCM.

Using an existing plugin means:
* We need to recognize plugin specific requests
* Our service might stop working if the plugin request API gets breaking changes
  (but that should only be possible when actively choosing to update such a plugin).

Implementing our own Stash/Bitbucket plugin means:
* We will spend a significant amount of time on implementation and maintenance
* We are in control of the API

I recommend trying to go with the existing HTTP Request Post-Receive Hook plugin.


## Existing Stash triggers
Stash supports two kinds of hooks: pre-receive and post-receive (but not for
example update as pure git does).

A pre-receive hook can reject pushes to a repository. A post-receive hook runs
after commits have been processed and can be used to update other services.

There are existing hooks available from the Atlassian Marketplace. We can also
develop a custom hook plugin.

Links:
* https://confluence.atlassian.com/bitbucketserver/using-repository-hooks-776639836.html


### HTTP Request Post-Receive Hook for Stash
Trigger a simple HTTP request with optional authentication.

* Supports filtering on branches, for example ^feature$
* Says there is support for filtering of events, but there is not much documentation
* Supports variable URLs, for example ${user.name}
* Open source licensed under Apache, we will be able to contribute if needed

We are probably able to use this, but it requires experimentation.

Links:
* https://marketplace.atlassian.com/plugins/de.aeffle.stash.plugin.stash-http-get-post-receive-hook/server/overview
* https://bitbucket.org/aeffle/stash-http-get-post-receive-hook/src


### Bitbucket Server Web Post Hooks Plugin
POST commit data to another system when a user pushes changes.

* The body of the POST request is JSON containing information about the repository, a list of
  commits, and the user that made the push.
* The service only receives a POST when a user pushes to the repository.
* It does not seem possible to customize the request body.
* Developed by Atlassian. Free of charge, but not open source. Not formally supported

We might be able to use this, but it requires investigation and trial of what the
JSON payload looks like in our scenarios: branch creation and branch deletion.

Links:
* https://marketplace.atlassian.com/plugins/com.atlassian.stash.plugin.stash-web-post-receive-hooks-plugin/server/overview
* https://confluence.atlassian.com/bitbucketserver/post-service-webhook-for-bitbucket-server-776640367.html


### Bitbucket Webhook to Jenkins
Notify Jenkins when you have committed or merged code.
This is Jenkins-specific. It's goal is to integrate Jenkins with Stash, but it
is not useful for us.

Links:
* https://marketplace.atlassian.com/plugins/com.nerdwin15.stash-stash-webhook-jenkins/server/overview
