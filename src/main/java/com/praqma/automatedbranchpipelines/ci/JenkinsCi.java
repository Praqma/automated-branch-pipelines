package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JenkinsCi implements Ci {

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
  public void createPipeline() throws IOException {
    String buildUrl = String.format("%s/job/%s/build", url, seedJob);
    logger.log(Level.INFO, "Triggering Jenkins job on URL: {0}", buildUrl);
    HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl).openConnection();
    connection.setRequestMethod("POST");
    connection.connect();
    logger.log(Level.INFO, "Build job triggered, response code: {0}",
        connection.getResponseCode());
    connection.disconnect();
  }

  @Override
  public void deletePipeline() throws IOException {
    for (String jobToDelete : pipeline) {
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

}
