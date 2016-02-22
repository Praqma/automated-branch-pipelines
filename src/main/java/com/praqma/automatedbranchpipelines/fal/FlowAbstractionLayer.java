package com.praqma.automatedbranchpipelines.fal;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.scm.ScmEventHandler;
import com.praqma.automatedbranchpipelines.scm.ScmRequest;

/**
 * Handles the software development flow operations, like creating and
 * deleting branches.
 */
public class FlowAbstractionLayer implements ScmEventHandler {

  private static final Logger logger =
      Logger.getLogger(FlowAbstractionLayer.class.getName());

  /** The continuous integration system to call. */
  private final Ci ci;

  /** The prefix of a relevant branch, for example "feature/". */
  private final String branchPrefix;

  public FlowAbstractionLayer(Ci ci, String branchPrefix) {
    this.ci = Objects.requireNonNull(ci, "ci was null");
    this.branchPrefix = Objects.requireNonNull(branchPrefix, "branchPrefix was null");
  }

  @Override
  public void onScmRequest(ScmRequest request) {
    logger.log(Level.INFO, "SCM request received");

    String branch = request.getBranch();
    if (!isBranchRelevant(branch)) {
      logger.log(Level.INFO, "Ignoring SCM request because branch {0} is not relevant",
          branch);
      return;
    }

    if (request.isCreate()) {
      onBranchCreated(branch);
    } else if (request.isDelete()) {
      onBranchDeleted(branch);
    } else {
      logger.log(Level.INFO, "Request with action {0} ignored", request.getAction());
    }
  }

  /**
   * Determine if the branch is relevant according to the system configuration.
   */
  private boolean isBranchRelevant(String branch) {
    return branch.startsWith(branchPrefix);
  }

  private void onBranchCreated(String branch) {
    logger.log(Level.INFO, "Calling CI to create pipeline for branch {0}", branch);
    try {
      ci.createPipeline();
      logger.log(Level.INFO, "Pipeline created on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when creating pipeline", e);
    }
  }

  private void onBranchDeleted(String branch) {
    logger.log(Level.INFO, "Calling CI to delete pipeline for branch {0}", branch);
    try {
      ci.deletePipeline();
      logger.log(Level.INFO, "Pipeline deleted on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when deleting pipeline", e);
    }
  }

}
