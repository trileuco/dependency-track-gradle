package com.trileuco.gradle;

import static com.trileuco.gradle.DependencyTrackExtension.DEPENDENCY_TRACK_EXTENSION_NAME;
import static com.trileuco.gradle.DependencyTrackExtension.DEPENDENCY_TRACK_TASK_NAME;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class DependencyTrackPlugin implements Plugin<Project> {
  private static final Logger LOGGER = Logging.getLogger(DependencyTrackPlugin.class);

  @Override
  public void apply(Project project) {
    LOGGER.debug("Adding '{}' extension to '{}'", DEPENDENCY_TRACK_EXTENSION_NAME, project);
    DependencyTrackExtension extension =
        project
            .getExtensions()
            .create(DEPENDENCY_TRACK_EXTENSION_NAME, DependencyTrackExtension.class);

    LOGGER.debug("Adding '{}' task to '{}'", DEPENDENCY_TRACK_TASK_NAME, project);
    DependencyTrackTask dependencyTrack =
        project.getTasks().create(DEPENDENCY_TRACK_TASK_NAME, DependencyTrackTask.class, extension);
    dependencyTrack.setDescription(
        "Publishes generated bom in " + project + " to Dependency-Track server.");
    dependencyTrack.setGroup("Reporting");
  }
}
