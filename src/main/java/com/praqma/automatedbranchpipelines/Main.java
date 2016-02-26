package com.praqma.automatedbranchpipelines;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import com.praqma.automatedbranchpipelines.cfg.Config;
import com.praqma.automatedbranchpipelines.cfg.Project;
import com.praqma.automatedbranchpipelines.cfg.Service;
import com.praqma.automatedbranchpipelines.cfg.ConfigReader;
import com.praqma.automatedbranchpipelines.fal.FlowAbstractionLayer;
import com.praqma.automatedbranchpipelines.scm.ScmHttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * The main class of the automated branch pipelines service.
 */
class Main {

  private static final Logger logger = Logger.getLogger(Service.class.getName());

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      logger.log(Level.SEVERE, "Missing YAML configuration file argument");
      return;
    }
    String configurationFilePath = args[0];

    try {
      Config config = ConfigReader.read(configurationFilePath);
      ScmHttpHandler scmHandler = initialize(config);
      startHttpServer(config, scmHandler);

      logger.log(Level.INFO, "Service started");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Service stopped", e);
    }
  }

  private static ScmHttpHandler initialize(Config config) {
    Map<String, Project> projects = config.getProjects();
    FlowAbstractionLayer fal = new FlowAbstractionLayer(projects);
    return new ScmHttpHandler(fal);
  }

  private static void startHttpServer(Config config, ScmHttpHandler scmHandler)
      throws IOException {
    Service serviceConfig = config.getService();
    HttpServer server = HttpServer.create(
        new InetSocketAddress(serviceConfig.getPort()), 0);
    server.createContext("/", scmHandler);
    server.setExecutor(null); // creates a default executor
    server.start();
  }

}
