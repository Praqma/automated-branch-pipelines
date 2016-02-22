package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;

/**
 * The interface to a continuous integration server, such as Jenkins.
 * Used for triggering the creation and deletion of build pipeline jobs.
 */
public interface Ci {

  /**
   * Requests pipeline creation.
   *
   * @throws IOException if a communication error occurs
   */
  void createPipeline() throws IOException;

  /**
   * Requests pipeline deletion.
   *
   * @throws IOException if a communication error occurs
   */
  void deletePipeline() throws IOException;

}
