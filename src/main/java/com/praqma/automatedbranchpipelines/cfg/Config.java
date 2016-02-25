package com.praqma.automatedbranchpipelines.cfg;

/**
 * The configuration of the service and the external systems.
 */
public class Config {

  private Service service;
  private Ci ci;

  public void setService(Service service) {
    this.service = service;
  }

  public Service getService() {
    return service;
  }

  public void setCi(Ci ci) {
    this.ci = ci;
  }

  public Ci getCi() {
    return ci;
  }
}
