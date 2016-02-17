package com.praqma.automatedbranchpipelines.cfg;

/**
 * Indicates a configuration error.
 */
public class ConfigException extends Exception {

  private ConfigException(String message) {
    this(message, null);
  }

  private ConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Indicates that a required configuration file was missing or invalid.
   */
  static ConfigException invalidFile(String fileName) {
    return new ConfigException("The configuration file: " + fileName +
        " could not be read");
  }

  /**
   * Indicates that a required configuration property was missing or invalid.
   */
  static ConfigException invalidProperty(String propertyName) {
    return ConfigException.invalidProperty(propertyName, null);
  }

  /**
   * Indicates that a required configuration property was missing or invalid.
   */
  static ConfigException invalidProperty(String propertyName, Throwable cause) {
    return new ConfigException("The configuration property: " + propertyName +
        " could not be read", cause);
  }

}
