package com.praqma.automatedbranchpipelines.scm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Accepts HTTP requests from an SCM trigger, for example a Git hook.
 */
public class ScmHttpHandler implements HttpHandler {

  private final ScmEventHandler eventHandler;

  public ScmHttpHandler(ScmEventHandler eventHandler) {
    this.eventHandler = Objects.requireNonNull(eventHandler, "eventHandler was null");
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean eventHandlerResponse = eventHandler.onBranchCreated();

    String response = "This is the response: " + eventHandlerResponse;
    exchange.sendResponseHeaders(200, response.length());
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(response.getBytes());
    }
  }

}
