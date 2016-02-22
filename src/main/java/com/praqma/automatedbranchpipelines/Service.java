package com.praqma.automatedbranchpipelines;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

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

  private static final Logger logger = Logger.getLogger(Service.class.getName());

  public static void main(String[] args) throws Exception {
    try {
      Config config = ConfigReader.read();

      Ci ci = new JenkinsCi(config.getJenkinsUrl(), config.getJenkinsSeedJob(),
          config.getJenkinsPipeline());
      FlowAbstractionLayer fal = new FlowAbstractionLayer(ci, config.getBranchPrefix());
      ScmHttpHandler scmHandler = new ScmHttpHandler(fal);

      HttpServer server = HttpServer.create(
          new InetSocketAddress(config.getServicePort()), 0);
      server.createContext("/", scmHandler);
      server.setExecutor(null); // creates a default executor
      server.start();

      logger.log(Level.INFO, "Service started");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Service stopped", e);
    }
  }

}
