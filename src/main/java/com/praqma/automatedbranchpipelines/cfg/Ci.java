package com.praqma.automatedbranchpipelines.cfg;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration of a CI server.
 */
public final class Ci {

  private String url;
  private String seedJob;
  private List<String> pipeline;

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setSeedJob(String seedJob) {
    this.seedJob = seedJob;
  }

  public String getSeedJob() {
    return seedJob;
  }

  public void setPipeline(List<String> pipeline) {
    this.pipeline = pipeline;
  }

  public List<String> getPipeline() {
    return pipeline;
  }

  @Override
  public String toString() {
    return String.format("URL=%s; Seed job=%s; Pipeline=%s", url, seedJob,
      pipeline.stream().collect(Collectors.joining(", ")));
  }
}
