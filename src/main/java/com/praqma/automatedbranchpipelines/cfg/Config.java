package com.praqma.automatedbranchpipelines.cfg;

import java.util.Collections;
import java.util.Map;

/**
 * The configuration of the service and the external systems.
 */
public class Config {

  private Service service;
  private Map<String, Project> projects;

  public void setService(Service service) {
    this.service = service;
  }

  public Service getService() {
    return service;
  }

  public void setProjects(Map<String, Project> projects) {
    this.projects = projects;
  }

  public Map<String, Project> getProjects() {
    return Collections.unmodifiableMap(projects);
  }
}
