package com.praqma.automatedbranchpipelines.fal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.praqma.automatedbranchpipelines.cfg.Project;
import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.ci.JenkinsCi;
import com.praqma.automatedbranchpipelines.scm.Branch;
import com.praqma.automatedbranchpipelines.scm.ScmEventHandler;
import com.praqma.automatedbranchpipelines.scm.ScmRequest;

/**
 * Handles the software development flow operations, like creating and
 * deleting branches.
 */
public class FlowAbstractionLayer implements ScmEventHandler {

  private static final Logger logger =
      Logger.getLogger(FlowAbstractionLayer.class.getName());

  /** Configuration of projects. */
  private final Map<String, Project> projects;

  public FlowAbstractionLayer(Map<String, Project> projects) {
    this.projects = projects;
  }

  @Override
  public void onScmRequest(ScmRequest request) {
    ProjectHandler projectHandler = new ProjectHandler(request);
    Project project = projectHandler.getProject(projects);
    boolean isRepositoryRelevant = (project != null);
    if (!isRepositoryRelevant) {
      logger.log(Level.INFO,
          "Ignoring SCM request because project repository {0} is not relevant",
          request.getRepository());
      return;
    }

    String branchPrefix = projectHandler.getBranchPrefix(project);
    boolean isBranchRelevant = (branchPrefix != null);
    if (!isBranchRelevant) {
      logger.log(Level.INFO, "Ignoring SCM request because branch {0} is not relevant",
          request.getBranch());
      return;
    }

    Ci ci = getCi(project);
    Branch branch = request.getBranch();
    List<String> pipeline = projectHandler.getPipeline(project, branchPrefix);
    if (projectHandler.isCreateAction()) {
      onBranchCreated(ci, branch, pipeline);
    } else if (projectHandler.isDeleteAction()) {
      onBranchDeleted(ci, branch, pipeline);
    } else {
      logger.log(Level.INFO, "Request with action {0} ignored", request.getAction());
    }
  }

  private void onBranchCreated(Ci ci, Branch branch, List<String> pipeline) {
    logger.log(Level.INFO, "Calling CI to create pipeline for branch {0}", branch);
    try {
      ci.createPipeline(branch, pipeline);
      logger.log(Level.INFO, "Pipeline created on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when creating pipeline", e);
    }
  }

  private void onBranchDeleted(Ci ci, Branch branch, List<String> pipeline) {
    logger.log(Level.INFO, "Calling CI to delete pipeline for branch {0}", branch);
    try {
      ci.deletePipeline(branch, pipeline);
      logger.log(Level.INFO, "Pipeline deleted on CI");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "CI error when deleting pipeline", e);
    }
  }

  private Ci getCi(Project project) {
    String url = project.getCiUrl();
    String seedJob = project.getSeedJob();
    return new JenkinsCi(url, seedJob);
  }

}
