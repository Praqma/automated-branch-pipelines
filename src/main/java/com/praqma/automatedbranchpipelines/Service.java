package com.praqma.automatedbranchpipelines;

import java.net.InetSocketAddress;

import com.praqma.automatedbranchpipelines.cfg.Config;
import com.praqma.automatedbranchpipelines.cfg.ConfigReader;
import com.praqma.automatedbranchpipelines.ci.Ci;
import com.praqma.automatedbranchpipelines.ci.JenkinsCi;
import com.praqma.automatedbranchpipelines.fal.FlowAbstractionLayer;
import com.praqma.automatedbranchpipelines.scm.ScmHttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * The main class of the automated branch pipelines service.
 */
class Service {

  public static void main(String[] args) throws Exception {
    try {
      Config config = ConfigReader.read();

      Ci ci = new JenkinsCi(config.getJenkinsUrl(), config.getJenkinsSeedJob());
      FlowAbstractionLayer fal = new FlowAbstractionLayer(ci);
      ScmHttpHandler scmHandler = new ScmHttpHandler(fal);

      HttpServer server = HttpServer.create(
          new InetSocketAddress(config.getServicePort()), 0);
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
