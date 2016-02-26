package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.util.List;

/**
 * The interface to a continuous integration server, such as Jenkins.
 * Used for triggering the creation and deletion of build pipeline jobs.
 */
public interface Ci {

  /**
   * Requests pipeline creation.
   *
   * @param branch Branch name used to identify the pipeline jobs
   * @param pipeline List of job prefixes
   * @throws IOException if a communication error occurs
   */
  void createPipeline(String branch, List<String> pipeline) throws IOException;

  /**
   * Requests pipeline deletion.
   *
   * @param branch Branch name used to identify the pipeline jobs
   * @param pipeline List of job prefixes
   * @throws IOException if a communication error occurs
   */
  void deletePipeline(String branch, List<String> pipeline) throws IOException;

}
