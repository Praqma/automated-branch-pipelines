package com.praqma.automatedbranchpipelines.scm;

/**
 * Model for the values in an SCM request.
 */
public class ScmRequest {

  private final String repository;

  private final Branch branch;

  private final Action action;

  ScmRequest(String repository, Branch branch, Action action) {
    this.repository = repository;
    this.branch = branch;
    this.action = action;
  }

  public String getRepository() {
    return repository;
  }

  /**
   * The branch prefix and name, for example "feature/1337-coolfeature".
   */
  public Branch getBranch() {
    return branch;
  }

  public Action getAction() {
    return action;
  }

  @Override
  public String toString() {
    return String.format("repository=%s, branch=%s, action=%s",
        repository, branch.toString(), action);
  }

}
