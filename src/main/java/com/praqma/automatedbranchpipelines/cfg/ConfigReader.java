package com.praqma.automatedbranchpipelines.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Reads configuration files.
 */
public class ConfigReader {

  private static final Logger logger = Logger.getLogger(ConfigReader.class.getName());

  /**
   * Read all configuration.
   *
   * @param configurationFilePath Path to YAML configuration file
   * @return The configuration
   * @throws ConfigException if the configuration is invalid
   */
  public static Config read(String configurationFilePath) throws ConfigException {
    logger.log(Level.INFO, "Reading properties from: {0}", configurationFilePath);
    try (InputStream stream = Files.newInputStream(Paths.get(configurationFilePath))) {
      Yaml yaml = new Yaml();
      Config config = yaml.loadAs(stream, Config.class);
      logConfiguration(config);
      return config;
    } catch (IOException | YAMLException e) {
      throw ConfigException.invalidFile(configurationFilePath, e);
    }
  }

  private static void logConfiguration(Config config) {
    logger.log(Level.INFO, "Service configuration: {0}", config.getService().toString());
    logger.log(Level.INFO, "Projects configuration:");
    for (Map.Entry<String, Project> entry : config.getProjects().entrySet()) {
      logger.log(Level.INFO, "  Project name: {0}", entry.getKey());
      logger.log(Level.INFO, "  Project configuration: {0}", entry.getValue().toString());
    }
  }

}
