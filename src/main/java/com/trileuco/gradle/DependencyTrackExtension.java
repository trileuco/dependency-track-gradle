package com.trileuco.gradle;

import java.io.File;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import javax.inject.Singleton;

public class DependencyTrackExtension {
  public static final String DEPENDENCY_TRACK_EXTENSION_NAME = "dependencyTrack";
  public static final String DEPENDENCY_TRACK_TASK_NAME = "dependencyTrack";

  private final Property<String> host;
  private final Property<String> realm;
  private final Property<String> apiKey;
  private final Property<String> projectName;
  private final Property<String> projectVersion;
  private final Property<String> projectId;
  private final RegularFileProperty bomFile;

  public DependencyTrackExtension(Project project, ObjectFactory objects) {
    this.host = objects.property(String.class).convention("http://localhost:8081");
    this.apiKey = objects.property(String.class);
    this.projectId = objects.property(String.class);
    this.realm = objects.property(String.class).convention("/api/v1/bom");
    this.bomFile =
        objects.fileProperty().fileValue(new File(project.getBuildDir(), "reports/bom.xml"));
    this.projectName = objects.property(String.class).convention(project.getName());
    this.projectVersion = objects.property(String.class).convention(project.getVersion().toString());
  }

  public void validate() {
    if (!host.isPresent()) {
      throw new IllegalArgumentException(
          "'host' is not set. Set the url of the dependency-track server. e.g. http://localhost:8081");
    }
    if (!apiKey.isPresent()) {
      throw new IllegalArgumentException(
          "'apiKey' is not set. Set the 'apiKey' to access the dependency-track server.");
    }
    if (bomFile.getAsFile().isPresent() && !bomFile.getAsFile().get().exists()) {
      throw new IllegalArgumentException(
          "Bom file '" + bomFile.getAsFile().get().getAbsolutePath() + "' not found.");
    }
  }

  public Property<String> getHost() {
    return host;
  }

  public Property<String> getApiKey() {
    return apiKey;
  }

  public Property<String> getRealm() {
    return realm;
  }

  public Property<String> getProjectId() {
    return projectId;
  }

  public Property<String> getProjectName() {
    return projectName;
  }

  public Property<String> getProjectVersion() {
    return projectVersion;
  }

  public RegularFileProperty getBomFile() {
    return bomFile;
  }
}
