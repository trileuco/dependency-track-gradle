package com.trileuco.gradle;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DependencyTrackClient {
  private static final Logger LOGGER = Logging.getLogger(DependencyTrackClient.class);

  private static final String HEADER_API_KEY_NAME = "X-API-Key";

  private final URI uri;
  private final String apiKey;
  private final HttpClient httpClient;

  public DependencyTrackClient(String host, String realm, String apiKey) {
    this.uri = buildUri(host, realm);
    this.apiKey = apiKey;
    this.httpClient = HttpClient.newHttpClient();
  }

  public void publish(String projectId, File bom) {
    if (bom == null) {
      throw new IllegalArgumentException("bom cannot be empty");
    }
    if (projectId == null || projectId.isEmpty()) {
      throw new IllegalArgumentException("projectId cannot be empty");
    }

    LOGGER.info("Sending bom to: {}", uri);
    String body = buildBody(projectId, bom);
    LOGGER.debug("Body content: {}", body);
    HttpRequest httpRequest = buildRequest(uri, body);

    try {
      HttpResponse<String> response = this.httpClient.send(httpRequest, BodyHandlers.ofString());
      String responseBody = response.body();
      LOGGER.info("Response: {}", responseBody);
    } catch (IOException | InterruptedException e) {
      LOGGER.error("Something went wrong sending the bom", e);
      Thread.currentThread().interrupt();
    }
  }

  private HttpRequest buildRequest(URI dTrackUri, String body) {
    return HttpRequest.newBuilder(dTrackUri)
        .header(HEADER_API_KEY_NAME, this.apiKey)
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(body))
        .build();
  }

  private URI buildUri(String host, String realm) {
    String sanitizedHost = sanitizeHost(host);
    return URI.create(sanitizedHost + realm);
  }

  private String sanitizeHost(String host) {
    String finalHost = host.trim();
    if (finalHost.endsWith("/")) {
      finalHost = finalHost.substring(0, finalHost.length() - 1);
    }
    return finalHost;
  }

  private String buildBody(String projectId, File bom) {
    return "{" + "\"project\": \"" + projectId + "\", \"bom\": \"" + encodeBom(bom) + "\" }";
  }

  private String encodeBom(File bom) {
    LOGGER.info("Reading bom from {}", bom.getAbsolutePath());
    try (Stream<String> stream = Files.lines(bom.toPath())) {
      String bomAsString = stream.collect(Collectors.joining("\n"));
      return Base64.getEncoder().encodeToString(bomAsString.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
