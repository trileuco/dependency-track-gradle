package com.trileuco.gradle;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyTrackClient {
  private static final Logger LOGGER = Logging.getLogger(DependencyTrackClient.class);

  private static final String HEADER_API_KEY_NAME = "X-API-Key";

  private final URI uri;
  private final String apiKey;
  private final HttpClient httpClient;

  public DependencyTrackClient(String host, String realm, String apiKey) {
    this.uri = buildUri(host, realm);
    this.apiKey = apiKey;
    this.httpClient = HttpClientBuilder.create().build();
  }

  public void publish(String projectName, String projectVersion, File bom) {
    if (bom == null) {
      throw new IllegalArgumentException("bom cannot be empty");
    }
    if (projectName == null || projectName.isEmpty()) {
      throw new IllegalArgumentException("projectName cannot be empty");
    }
    if (projectVersion == null || projectVersion.isEmpty()) {
      throw new IllegalArgumentException("projectVersion cannot be empty");
    }

    LOGGER.info("Sending bom to: {}", uri);
    DTrackBody body = new DTrackBody(projectName, projectVersion, true, encodeBom(bom));
    HttpUriRequest httpRequest = buildRequest(uri, body);

    try {
      HttpResponse response = this.httpClient.execute(httpRequest);
      String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
      LOGGER.info("Response: {}", responseBody);
    } catch (Exception e) {
      LOGGER.error("Something went wrong sending the bom", e);
      Thread.currentThread().interrupt();
    }
  }

  public void delete(String projectName, String projectVersion) {

  }

  private HttpUriRequest buildRequest(URI dTrackUri, DTrackBody body) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("projectName", body.getProjectName());
    jsonObject.addProperty("autoCreate",  body.isAutoCreate());
    jsonObject.addProperty("projectVersion", body.getProjectVersion());
    jsonObject.addProperty("bom", body.getBom());
    StringEntity json = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
    return RequestBuilder.put(dTrackUri)
            .addHeader(HEADER_API_KEY_NAME, this.apiKey)
            .setEntity(json)
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
