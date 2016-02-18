package com.praqma.automatedbranchpipelines.scm;

/**
 * Indicates an invalid SCM request.
 */
class ScmRequestException extends Exception {

  private static final long serialVersionUID = 1L;

  ScmRequestException(String message) {
    super(message);
  }

  ScmRequestException(String message, Throwable cause) {
    super(message, cause);
  }

}
