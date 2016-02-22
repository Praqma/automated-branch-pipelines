package com.praqma.automatedbranchpipelines.cfg;

import java.util.Properties;

/**
 * Holds the configuration used for running.
 */
public class Config {

  private final String branchPrefix;
  private final String jenkinsUrl;
  private final String jenkinsSeedJob;
  private final int servicePort;

  private Config(String branchPrefix, String jenkinsUrl, String jenkinsSeedJob,
      int servicePort) {
    this.branchPrefix = branchPrefix;
    this.jenkinsUrl = jenkinsUrl;
    this.jenkinsSeedJob = jenkinsSeedJob;
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
    int servicePort = Config.getIntProperty("service.port", properties);
    return new Config(branchPrefix, jenkinsUrl, jenkinsSeedJob, servicePort);
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

}
