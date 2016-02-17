package com.praqma.automatedbranchpipelines.fal;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.scm.ScmEventHandler;

/**
 * Handles the software development flow operations, like creating and
 * deleting branches.
 */
public class FlowAbstractionLayer implements ScmEventHandler {

  private static final Logger logger =
      Logger.getLogger(FlowAbstractionLayer.class.getName());

  private final Ci ci;

  public FlowAbstractionLayer(Ci ci) {
    this.ci = Objects.requireNonNull(ci, "ci was null");
  }

  @Override
  public boolean onBranchCreated() {
    logger.log(Level.INFO, "onBranchCreated event received");
    try {
      int ciResponse = ci.createPipeline();
      logger.log(Level.INFO, "CI response: {0}", ciResponse);
      return true;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error", e);
      return false;
    }
  }

  @Override
  public boolean onBranchDeleted() {
    throw new UnsupportedOperationException("onBranchDeleted not implemented");
  }

}
