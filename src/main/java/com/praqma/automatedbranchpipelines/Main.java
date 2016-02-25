package com.praqma.automatedbranchpipelines;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.praqma.automatedbranchpipelines.cfg.Ci;
import com.praqma.automatedbranchpipelines.cfg.Config;
import com.praqma.automatedbranchpipelines.cfg.Scm;
import com.praqma.automatedbranchpipelines.cfg.Service;
import com.praqma.automatedbranchpipelines.cfg.ConfigReader;
import com.praqma.automatedbranchpipelines.ci.CiServer;
import com.praqma.automatedbranchpipelines.ci.JenkinsCi;
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
    Scm scmConfig = config.getScm();
    Ci ciConfig = config.getCi();

    CiServer ciServer = new JenkinsCi(ciConfig.getUrl(), ciConfig.getSeedJob(),
        ciConfig.getPipeline());
    FlowAbstractionLayer fal = new FlowAbstractionLayer(ciServer,
        scmConfig.getBranchPrefix());
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
