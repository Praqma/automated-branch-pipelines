package com.praqma.automatedbranchpipelines.cfg;

/**
 * Configuration of the branch pipeline service.
 */
public final class Service {

  private int port;

  public void setPort(int port) {
    this.port = port;
  }

  public int getPort() {
    return port;
  }

  @Override
  public String toString() {
    return "Port=" + port;
  }
}
