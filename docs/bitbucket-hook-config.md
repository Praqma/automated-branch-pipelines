# Bitbucket Hook Configuration

This document describes how to configure the [HTTP Request Post-Receive Hook](https://marketplace.atlassian.com/plugins/de.aeffle.stash.plugin.stash-http-get-post-receive-hook/server/overview)
 add-on for Bitbucket Server or Stash.


## Installation
* Start up Bitbucket Server with a project and a repository
* As administrator, go to "Find new add-ons", search for "HTTP request" and install the
  add-on
* Go to repository settings, workflow, hooks. Enable the HTTP request post receive hook
* Fill out the configuration as described below

This screenshot shows the configuration UI in version 4.0.0 of the add-on:

![Bitbucket hook configuration UI](/docs/images/bitbucket-http-request-addon.png)


## Method

Set the method to `POST`.


## URL
Set this to the service URL including port. For example if Bitbucket is running in a
Docker container in VirtualBox, the host URL may look like this, matching the IP of a
VirtualBox network adapter:

```sh
http://192.168.99.1:8181
```


## Post Content-Type

Set this to `application/json`.


## Post Data
The POST data should look like this, utilizing template strings from the add-on.

```sh
{
"scm" : "git",
"branch" : "${refChange.name}",
"action" : "${refChange.type}"
}
```

* The SCM type is Git
* The branch will contain the branch name
* The action represents one of the ref change types in Bitbucket/Stash: "ADD", "UPDATE"
  or "DELETE"

Here is an example of what the POST data will look like in a running system:

```sh
{
"scm" : "git",
"branch" : "feature%2F1337-coolfeature",
"action" : "ADD"
}
```

In this example the branch name is "feature/1337-coolfeature" (URL encoded) and the
action represents the creation of a branch (the initial push to a branch).
