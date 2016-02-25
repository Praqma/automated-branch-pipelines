package com.praqma.automatedbranchpipelines.cfg;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration of a CI server.
 */
public final class Ci {

  private String url;
  private String seedJob;
  private Map<String, List<String>> pipelines;

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

  public void setPipelines(Map<String, List<String>> pipelines) {
    this.pipelines = pipelines;
  }

  public Map<String, List<String>> getPipelines() {
    return pipelines;
  }

  @Override
  public String toString() {
    StringBuilder pipelineBuilder = new StringBuilder();
    for (Map.Entry<String, List<String>> entry : pipelines.entrySet()) {
      pipelineBuilder.append(entry.getKey()).append("->");
      pipelineBuilder.append(entry.getValue().stream().collect(Collectors.joining(", ")));
    }

    return String.format("URL=%s; Seed job=%s; Pipelines=%s", url, seedJob,
        pipelineBuilder.toString());
  }
}
