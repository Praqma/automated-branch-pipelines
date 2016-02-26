package com.praqma.automatedbranchpipelines.fal;

import java.util.List;
import java.util.Map;

import com.praqma.automatedbranchpipelines.cfg.Project;
import com.praqma.automatedbranchpipelines.scm.ScmRequest;

/**
 * Responsible for inspecting an SCM request with respect to the project configuration.
 */
class ProjectHandler {

  /** Bitbucket/git refChange type indicating branch creation. */
  private static final String GIT_CREATE_ACTION = "ADD";

  /** Bitbucket/git refChange type indicating branch deletion. */
  private static final String GIT_DELETE_ACTION = "DELETE";

  private final ScmRequest request;

  ProjectHandler(ScmRequest request) {
    this.request = request;
  }

  /**
   * Determine if the project repository is relevant by looking up the repository name in
   * the project configuration.
   */
  Project getProject(Map<String, Project> projects) {
    String repository = request.getRepository();
    return projects.get(repository);
  }

  /**
   * Determine if the branch is relevant by checking if it matches one of the configured
   * prefixes.
   */
  String getBranchPrefix(Project project) {
    String branch = request.getBranch();
    Map<String, List<String>> pipelines = project.getPipelines();
    for (String mappedBranchPrefix : pipelines.keySet()) {
      if (branch.startsWith(mappedBranchPrefix + "/")) {
        return mappedBranchPrefix;
      }
    }
    return null;
  }

  boolean isCreateAction() {
    String action = request.getAction();
    boolean isGit = ScmRequest.SCM_GIT.equals(request.getScm());
    if (isGit) {
      return GIT_CREATE_ACTION.equals(action);
    } else {
      // Unknown SCM type
      return false;
    }
  }

  boolean isDeleteAction() {
    String action = request.getAction();
    boolean isGit = ScmRequest.SCM_GIT.equals(request.getScm());
    if (isGit) {
      return GIT_DELETE_ACTION.equals(action);
    } else {
      // Unknown SCM type
      return false;
    }
  }

  /**
   * Sanitize a branch name so it can be used in a build job:
   *
   * <ul>
   *   <li>Remove branch prefix, so the CI server does not have to deal with '/'</li>
   *   <li>Replace dashes with underscores</li>
   * </ul>
   */
  String getCiFriendlyBranchName(String branchPrefix) {
    String branch = request.getBranch();
    String result = branch.substring(branchPrefix.length() + 1);
    result = result.replace('-', '_');
    return result;
  }

  List<String> getPipeline(Project project, String branchPrefix) {
    Map<String, List<String>> pipelines = project.getPipelines();
    return pipelines.get(branchPrefix);
  }

}
