package com.praqma.automatedbranchpipelines.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration from properties files.
 */
public class ConfigReader {

  private static final String RESOURCE_NAME = "automated-branch-pipelines.properties";

  /**
   * Read all configuration.
   *
   * @return The configuration
   * @throws IOException if a file read error occurs
   * @throws ConfigException if the configuration is invalid
   */
  public static Config read() throws IOException, ConfigException {
      System.out.println("Reading properties from: " + RESOURCE_NAME);
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Properties props = new Properties();
      try (InputStream stream = loader.getResourceAsStream(RESOURCE_NAME)) {
        if (stream == null) {
          throw ConfigException.invalidFile(RESOURCE_NAME);
        }
        props.load(stream);
      }
      props.list(System.out);
      return Config.getValidatedConfig(props);
  }

}
