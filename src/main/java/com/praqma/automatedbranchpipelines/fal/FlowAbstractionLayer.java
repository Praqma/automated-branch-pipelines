package com.praqma.automatedbranchpipelines.fal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.praqma.automatedbranchpipelines.ci.CiServer;
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
  private final CiServer ci;

  /** The mapping of branch prefixes to pipeline jobs. */
  private final Map<String, List<String>> pipelines;

  public FlowAbstractionLayer(CiServer ci, Map<String, List<String>> pipelines) {
    this.ci = ci;
    this.pipelines = pipelines;
  }

  @Override
  public void onScmRequest(ScmRequest request) {
    logger.log(Level.INFO, "SCM request received");

    BranchMapper branchMapper = new BranchMapper(request.getBranch(), pipelines);
    String branch = request.getBranch();

    if (!branchMapper.isBranchRelevant()) {
      logger.log(Level.INFO, "Ignoring SCM request because branch {0} is not relevant",
          branch);
      return;
    }

    String ciFriendlyBranchName = branchMapper.getCiFriendlyBranchName();
    if (request.isCreate()) {
      List<String> pipeline = branchMapper.getPipeline();
      onBranchCreated(ciFriendlyBranchName, pipeline);
    } else if (request.isDelete()) {
      List<String> pipeline = branchMapper.getPipeline();
      onBranchDeleted(ciFriendlyBranchName, pipeline);
    } else {
      logger.log(Level.INFO, "Request with action {0} ignored", request.getAction());
    }
  }

  private void onBranchCreated(String branch, List<String> pipeline) {
    logger.log(Level.INFO, "Calling CI to create pipeline for branch {0}", branch);
    try {
      ci.createPipeline(branch, pipeline);
      logger.log(Level.INFO, "Pipeline created on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when creating pipeline", e);
    }
  }

  private void onBranchDeleted(String branch, List<String> pipeline) {
    logger.log(Level.INFO, "Calling CI to delete pipeline for branch {0}", branch);
    try {
      ci.deletePipeline(branch, pipeline);
      logger.log(Level.INFO, "Pipeline deleted on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when deleting pipeline", e);
    }
  }

}
