package com.praqma.automatedbranchpipelines.ci;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class JenkinsCi implements Ci {

  private final String url;

  public JenkinsCi(String url) {
    this.url = Objects.requireNonNull(url, "url was null");
  }

  @Override
  public int createPipeline() throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setDoOutput(true);
    connection.connect();
    System.out.println("Job triggered");
    int code = connection.getResponseCode();
    connection.disconnect();
    return code;
  }

  @Override
  public int deletePipeline() {
    throw new UnsupportedOperationException("deletePipeline not implemented");
  }

}
