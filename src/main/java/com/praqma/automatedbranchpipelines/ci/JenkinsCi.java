package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JenkinsCi implements Ci {

  private static final Logger logger = Logger.getLogger(JenkinsCi.class.getName());

  private final String url;
  private final String seedJob;

  public JenkinsCi(String url, String seedJob) {
    this.url = url;
    this.seedJob = seedJob;
  }

  @Override
  public void createPipeline(String branch, List<String> pipeline) throws IOException {
    String buildUrl = getBuildUrl(branch, pipeline);
    logger.log(Level.INFO, "Triggering Jenkins job on URL: {0}", buildUrl);
    HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl).openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.connect();
    logger.log(Level.INFO, "Build job triggered, response code: {0}",
        connection.getResponseCode());
    connection.disconnect();
  }

  private String getBuildUrl(String branch, List<String> pipeline) {
    // %2C is the URL encoding of a comma
    String pipelineCsv = pipeline.stream().collect(Collectors.joining("%2C"));
    String buildUrl = String.format("%s/job/%s/buildWithParameters?BRANCH=%s&PIPELINE=%s",
        url, seedJob, branch, pipelineCsv);
    return buildUrl;
  }

  @Override
  public void deletePipeline(String branch, List<String> pipeline) throws IOException {
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
