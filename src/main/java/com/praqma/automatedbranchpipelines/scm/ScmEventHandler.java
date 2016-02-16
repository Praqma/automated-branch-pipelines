package com.praqma.automatedbranchpipelines.scm;

/**
 * Interface used to handle SCM events.
 */
public interface ScmEventHandler {

  /**
   * Raised when a branch is created.
   *
   * @return Whether or not the event was handled
   */
  boolean onBranchCreated();

  /**
   * Raised when a branch is deleted.
   *
   * @return Whether or not the event was handled
   */
  boolean onBranchDeleted();

}
