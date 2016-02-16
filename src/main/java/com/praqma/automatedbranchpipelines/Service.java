package com.praqma.automatedbranchpipelines;

import java.net.InetSocketAddress;

import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.ci.JenkinsCi;
import com.praqma.automatedbranchpipelines.fal.FlowAbstractionLayer;
import com.praqma.automatedbranchpipelines.scm.ScmHttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * The main class of the automated branch pipelines service.
 */
class Service {

  private static final String JENKINS_URL = "http://192.168.99.100:8080/job/seed/build";
  private static final int PORT = 8181;

  public static void main(String[] args) throws Exception {
    try {
      Ci ci = new JenkinsCi(JENKINS_URL);
      FlowAbstractionLayer fal = new FlowAbstractionLayer(ci);
      ScmHttpHandler scmHandler = new ScmHttpHandler(fal);

      HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
      server.createContext("/", scmHandler);
      server.setExecutor(null); // creates a default executor
      server.start();
      System.out.println("Service started");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Service stopped");
    }
  }

}
