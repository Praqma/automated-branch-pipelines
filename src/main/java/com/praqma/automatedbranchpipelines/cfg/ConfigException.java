package com.praqma.automatedbranchpipelines.cfg;

/**
 * Indicates a configuration error.
 */
public class ConfigException extends Exception {

  private static final long serialVersionUID = 1L;

  private ConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Indicates that a required configuration file was missing or invalid.
   */
  static ConfigException invalidFile(String fileName, Throwable cause) {
    return new ConfigException("The configuration file: " + fileName +
        " could not be read", cause);
  }

}
