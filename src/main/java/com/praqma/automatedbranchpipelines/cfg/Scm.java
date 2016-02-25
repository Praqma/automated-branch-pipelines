package com.praqma.automatedbranchpipelines.cfg;

/**
 * Configuration of a version control system.
 */
public final class Scm {

  private String branchPrefix;

  public void setBranchPrefix(String branchPrefix) {
    this.branchPrefix = branchPrefix;
  }

  public String getBranchPrefix() {
    return branchPrefix;
  }

  @Override
  public String toString() {
    return "Branch prefix=" + branchPrefix;
  }
}
