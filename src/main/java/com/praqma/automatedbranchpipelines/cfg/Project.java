package com.praqma.automatedbranchpipelines.cfg;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration of a project.
 */
public final class Project {

  private String ciUrl;
  private String seedJob;
  private Map<String, List<String>> pipelines;

  public void setCiUrl(String ciUrl) {
    this.ciUrl = ciUrl;
  }

  public String getCiUrl() {
    return ciUrl;
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
      pipelineBuilder.append("(");
      pipelineBuilder.append(entry.getKey()).append("->");
      pipelineBuilder.append(entry.getValue().stream().collect(Collectors.joining(", ")));
      pipelineBuilder.append("), ");
    }

    return String.format("CI URL=%s; Seed job=%s; Pipelines=%s", ciUrl, seedJob,
        pipelineBuilder.toString());
  }
}
