package com.trileuco.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;

public class DeleteTask extends DefaultTask {
  private final DependencyTrackExtension extension;
  public static final String TASK_NAME = "delete";

  @Inject
  public DeleteTask(DependencyTrackExtension extension) {
    this.extension = extension;
  }

  @TaskAction
  public void publish() {
    extension.validate();
    DependencyTrackClient client =
        new DependencyTrackClient(
            extension.getHost().get(), extension.getRealm().get(), extension.getApiKey().get());
    client.delete(extension.getProjectName().get(), extension.getProjectVersion().get());
  }
}
