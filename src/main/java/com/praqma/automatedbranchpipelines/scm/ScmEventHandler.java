package com.praqma.automatedbranchpipelines.scm;

/**
 * Interface used to handle SCM events.
 */
public interface ScmEventHandler {

  /**
   * Raised when an SCM request is received.
   */
  void onScmRequest(ScmRequest request);

}
