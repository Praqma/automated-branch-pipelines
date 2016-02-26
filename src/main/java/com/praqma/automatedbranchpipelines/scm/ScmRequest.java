package com.praqma.automatedbranchpipelines.scm;

/**
 * Model for the values in an SCM request.
 */
public class ScmRequest {

  /** The SCM identifier for Git. */
  public static final String SCM_GIT = "git";

  private final String scm;

  private final String repository;

  private final String branch;

  private final String action;

  ScmRequest(String scm, String repository, String branch, String action) {
    this.scm = scm;
    this.repository = repository;
    this.branch = branch;
    this.action = action;
  }

  public String getScm() {
    return scm;
  }

  public String getRepository() {
    return repository;
  }

  public String getBranch() {
    return branch;
  }

  public String getAction() {
    return action;
  }

  @Override
  public String toString() {
    return String.format("scm=%s, repository=%s, branch=%s, action=%s",
        scm, repository, branch, action);
  }

}
