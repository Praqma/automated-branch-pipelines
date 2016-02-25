package com.praqma.automatedbranchpipelines.fal;

import java.util.List;
import java.util.Map;

/**
 * Responsible for inspecting the mapping of branch prefixes to build pipelines.
 */
class BranchMapper {

  private final String branch;
  private final Map<String, List<String>> pipelines;

  private final String branchPrefix;

  BranchMapper(String branch, Map<String, List<String>> pipelines) {
    this.branch = branch;
    this.pipelines = pipelines;
    this.branchPrefix = getBranchPrefix();
  }

  /**
   * Determine if the branch is relevant by checking if it matches one of the configured
   * prefixes.
   */
  boolean isBranchRelevant() {
    return branchPrefix != null;
  }

  /**
   * Sanitize a branch name so it can be used in a build job:
   *
   * <ul>
   *   <li>Remove branch prefix, so the CI server does not have to deal with '/'</li>
   *   <li>Replace dashes with underscores</li>
   * </ul>
   */
  String getCiFriendlyBranchName() {
    if (!isBranchRelevant()) {
      // Internal error as this should have been checked
      throw new IllegalStateException("Branch is not relevant");
    }
    String result = branch.substring(branchPrefix.length() + 1);
    result = result.replace('-', '_');
    return result;
  }

  List<String> getPipeline() {
    if (!isBranchRelevant()) {
      // Internal error as this should have been checked
      throw new IllegalStateException("Branch is not relevant");
    }
    return pipelines.get(branchPrefix);
  }

  private String getBranchPrefix() {
    for (String mappedBranchPrefix : pipelines.keySet()) {
      if (branch.startsWith(mappedBranchPrefix + "/")) {
        return mappedBranchPrefix;
      }
    }
    return null;
  }

}
