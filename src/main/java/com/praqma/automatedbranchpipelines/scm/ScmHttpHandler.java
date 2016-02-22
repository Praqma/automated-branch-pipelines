package com.praqma.automatedbranchpipelines.scm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Accepts HTTP requests from an SCM trigger, for example a Git hook.
 */
public class ScmHttpHandler implements HttpHandler {

  private static final Logger logger = Logger.getLogger(ScmHttpHandler.class.getName());

  private final ScmEventHandler eventHandler;

  public ScmHttpHandler(ScmEventHandler eventHandler) {
    this.eventHandler = Objects.requireNonNull(eventHandler, "eventHandler was null");
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    if (!"POST".equals(method)) {
      logger.log(Level.INFO, "Ignoring request with method: {0}", method);
      writeResponse(exchange);
      return;
    }

    ScmRequest request;
    try {
      request = ScmRequestParser.parse(exchange.getRequestBody());
    } catch (ScmRequestException e) {
      logger.log(Level.SEVERE, "SCM request parse error", e);
      writeResponse(exchange);
      return;
    }

    eventHandler.onScmRequest(request);

    writeResponse(exchange);
  }

  private void writeResponse(HttpExchange exchange) throws IOException {
    String response = "";
    exchange.sendResponseHeaders(200, response.length());
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(response.getBytes());
    }
  }

}
