package com.praqma.automatedbranchpipelines.fal;

import java.util.List;
import java.util.Map;

import com.praqma.automatedbranchpipelines.cfg.Project;
import com.praqma.automatedbranchpipelines.scm.Action;
import com.praqma.automatedbranchpipelines.scm.Branch;
import com.praqma.automatedbranchpipelines.scm.ScmRequest;

/**
 * Responsible for inspecting an SCM request with respect to the project configuration.
 */
class ProjectHandler {

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
    Branch branch = request.getBranch();
    String branchPrefix = branch.getPrefix();
    Map<String, List<String>> pipelines = project.getPipelines();
    for (String mappedBranchPrefix : pipelines.keySet()) {
      if (branchPrefix.equals(mappedBranchPrefix)) {
        return mappedBranchPrefix;
      }
    }
    // The branch prefix in the SCM request is not present in the configuration
    return null;
  }

  boolean isCreateAction() {
    Action action = request.getAction();
    return Action.CREATE.equals(action);
  }

  boolean isDeleteAction() {
    Action action = request.getAction();
    return Action.DELETE.equals(action);
  }

  List<String> getPipeline(Project project, String branchPrefix) {
    Map<String, List<String>> pipelines = project.getPipelines();
    return pipelines.get(branchPrefix);
  }

}
