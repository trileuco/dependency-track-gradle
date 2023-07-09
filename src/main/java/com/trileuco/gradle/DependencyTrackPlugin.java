package com.trileuco.gradle;

import static com.trileuco.gradle.DependencyTrackExtension.DEPENDENCY_TRACK_EXTENSION_NAME;
import static com.trileuco.gradle.DependencyTrackExtension.DEPENDENCY_TRACK_TASK_NAME;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskContainer;

public class DependencyTrackPlugin implements Plugin<Project> {
  private static final Logger LOGGER = Logging.getLogger(DependencyTrackPlugin.class);
  private static final String DEFAULT_GROUP = "dependency-track";

  @Override
  public void apply(Project project) {
    LOGGER.debug("Adding '{}' extension to '{}'", DEPENDENCY_TRACK_EXTENSION_NAME, project);
    DependencyTrackExtension extension =
        project
            .getExtensions()
            .create(DEPENDENCY_TRACK_EXTENSION_NAME, DependencyTrackExtension.class);

    LOGGER.debug("Adding '{}' task to '{}'", DEPENDENCY_TRACK_TASK_NAME, project);
    TaskContainer projectTasks = project.getTasks();

    UploadTask uploadTask =
            projectTasks.create(UploadTask.TASK_NAME, UploadTask.class, extension);
    uploadTask.setGroup(DEFAULT_GROUP);
    uploadTask.setDescription("Upload bom to Dependency-Track server.");

    DeleteTask deleteTask =
            projectTasks.create(DeleteTask.TASK_NAME, DeleteTask.class, extension);
    deleteTask.setGroup(DEFAULT_GROUP);
    deleteTask.setDescription("Delete project from Dependency-Track server.");

  }
}
