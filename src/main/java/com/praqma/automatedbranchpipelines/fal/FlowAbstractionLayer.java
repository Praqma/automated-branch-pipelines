package com.praqma.automatedbranchpipelines.fal;

import java.io.IOException;
import java.util.Objects;

import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.scm.ScmEventHandler;

/**
 * Handles the software development flow operations, like creating and
 * deleting branches.
 */
public class FlowAbstractionLayer implements ScmEventHandler {

  private final Ci ci;

  public FlowAbstractionLayer(Ci ci) {
    this.ci = Objects.requireNonNull(ci, "ci was null");
  }

  @Override
  public boolean onBranchCreated() {
    System.out.println("onBranchCreated event received");
    try {
      int ciResponse = ci.createPipeline();
      System.out.println("CI response: " + ciResponse);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public boolean onBranchDeleted() {
    throw new UnsupportedOperationException("onBranchDeleted not implemented");
  }

}
