package com.praqma.automatedbranchpipelines.scm;

/** The SCM branch action. */
public enum Action {

  /** The action is unknown or not relevant. */
  IGNORE,

  /** A branch has been created. */
  CREATE,

  /** A branch has been deleted. */
  DELETE,
}
