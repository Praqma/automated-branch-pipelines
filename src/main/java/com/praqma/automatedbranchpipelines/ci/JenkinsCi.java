package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JenkinsCi implements Ci {

  private static final Logger logger = Logger.getLogger(JenkinsCi.class.getName());

  private final String url;
  private final String seedJob;

  public JenkinsCi(String url, String seedJob) {
    this.url = Objects.requireNonNull(url, "url was null");
    this.seedJob = Objects.requireNonNull(seedJob, "seedJob was null");
  }

  @Override
  public int createPipeline() throws IOException {
    String buildUrl = String.format("%s/job/%s/build", url, seedJob);
    logger.log(Level.INFO, "Triggering Jenkins job on URL: {0}", buildUrl);
    HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl).openConnection();
    connection.setDoOutput(true);
    connection.connect();
    logger.log(Level.INFO, "Job triggered");
    int code = connection.getResponseCode();
    connection.disconnect();
    return code;
  }

  @Override
  public int deletePipeline() {
    throw new UnsupportedOperationException("deletePipeline not implemented");
  }

}
