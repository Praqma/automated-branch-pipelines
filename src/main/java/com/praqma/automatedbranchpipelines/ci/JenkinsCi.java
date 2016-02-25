package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JenkinsCi implements CiServer {

  private static final Logger logger = Logger.getLogger(JenkinsCi.class.getName());

  private final String url;
  private final String seedJob;
  private final List<String> pipeline;

  public JenkinsCi(String url, String seedJob, List<String> pipeline) {
    this.url = Objects.requireNonNull(url, "url was null");
    this.seedJob = Objects.requireNonNull(seedJob, "seedJob was null");
    this.pipeline = Objects.requireNonNull(pipeline, "pipeline was null");
  }

  @Override
  public void createPipeline(String branch) throws IOException {
    String buildUrl = getBuildUrl(branch);
    logger.log(Level.INFO, "Triggering Jenkins job on URL: {0}", buildUrl);
    HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl).openConnection();
    connection.setRequestMethod("POST");
    connection.connect();
    logger.log(Level.INFO, "Build job triggered, response code: {0}",
        connection.getResponseCode());
    connection.disconnect();
  }

  private String getBuildUrl(String branch) {
    String buildUrl = String.format("%s/job/%s/buildWithParameters?BRANCH=%s",
        url, seedJob, branch);
    return buildUrl;
  }

  @Override
  public void deletePipeline(String branch) throws IOException {
    for (String jobPrefix : pipeline) {
      String jobToDelete = jobPrefix + "_" + branch;
      deleteJob(jobToDelete);
    }
  }

  private void deleteJob(String jobToDelete) throws IOException {
    String deleteUrl = String.format("%s/job/%s/doDelete", url, jobToDelete);
    logger.log(Level.INFO, "Triggering Jenkins job delete on URL: {0}", deleteUrl);
    HttpURLConnection connection = (HttpURLConnection) new URL(deleteUrl).openConnection();
    connection.setRequestMethod("POST");
    connection.connect();
    logger.log(Level.INFO, "Delete job triggered, response code: {0}",
        connection.getResponseCode());
    connection.disconnect();
  }

}
