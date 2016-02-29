package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.util.List;

import com.praqma.automatedbranchpipelines.scm.Branch;

/**
 * The interface to a continuous integration server, such as Jenkins.
 * Used for triggering the creation and deletion of build pipeline jobs.
 */
public interface Ci {

  /**
   * Requests pipeline creation.
   *
   * @param branch Branch used to identify the pipeline jobs
   * @param pipeline List of job prefixes
   * @throws IOException if a communication error occurs
   */
  void createPipeline(Branch branch, List<String> pipeline) throws IOException;

  /**
   * Requests pipeline deletion.
   *
   * @param branch Branch used to identify the pipeline jobs
   * @param pipeline List of job prefixes
   * @throws IOException if a communication error occurs
   */
  void deletePipeline(Branch branch, List<String> pipeline) throws IOException;

}
