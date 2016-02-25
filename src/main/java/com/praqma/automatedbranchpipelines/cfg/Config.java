package com.praqma.automatedbranchpipelines.cfg;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Holds the configuration used for running.
 */
public class Config {

  private final String branchPrefix;
  private final String jenkinsUrl;
  private final String jenkinsSeedJob;
  private final List<String> jenkinsPipeline;
  private final int servicePort;

  private Config(String branchPrefix, String jenkinsUrl, String jenkinsSeedJob,
      List<String> jenkinsPipeline, int servicePort) {
    this.branchPrefix = branchPrefix;
    this.jenkinsUrl = jenkinsUrl;
    this.jenkinsSeedJob = jenkinsSeedJob;
    this.jenkinsPipeline = jenkinsPipeline;
    this.servicePort = servicePort;
  }

  /**
   * Get the branch name prefix.
   */
  public String getBranchPrefix() {
    return branchPrefix;
  }

  /**
   * Get the URL to the Jenkins CI.
   */
  public String getJenkinsUrl() {
    return jenkinsUrl;
  }

  /**
   * Get the name of the Jenkins seed job.
   */
  public String getJenkinsSeedJob() {
    return jenkinsSeedJob;
  }

  /**
   * Get the name prefixes of jobs in a pipeline.
   */
  public List<String> getJenkinsPipeline() {
    return jenkinsPipeline;
  }

  /**
   * Get the port number that the service should accept requests on.
   */
  public int getServicePort() {
    return servicePort;
  }

  /**
   * Validate that required configuration is defined.
   *
   * @return The configuration
   * @throws ConfigException if the configuration is invalid
   */
  static Config getValidatedConfig(Properties properties) throws ConfigException {
    String branchPrefix = Config.getStringProperty("branch.prefix", properties);
    String jenkinsUrl = Config.getStringProperty("jenkins.url", properties);
    String jenkinsSeedJob = Config.getStringProperty("jenkins.seed.job", properties);
    List<String> jenkinsPipeline = Config.getListProperty("jenkins.pipeline", properties);
    int servicePort = Config.getIntProperty("service.port", properties);
    return new Config(branchPrefix, jenkinsUrl, jenkinsSeedJob, jenkinsPipeline,
        servicePort);
  }

  private static String getStringProperty(String key, Properties properties) throws ConfigException {
    String value = properties.getProperty(key);
    if (value == null || value.isEmpty()) {
      throw ConfigException.invalidProperty(key);
    }
    return value;
  }

  private static int getIntProperty(String key, Properties properties) throws ConfigException {
    String value = Config.getStringProperty(key, properties);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw ConfigException.invalidProperty(key, e);
    }
  }

  private static List<String> getListProperty(String key, Properties properties) throws ConfigException {
    String value = Config.getStringProperty(key, properties);
    String[] parts = value.split(",");
    List<String> list = Arrays.stream(parts).map(str -> str.trim()).collect(Collectors.toList());
    return list;
  }

}
