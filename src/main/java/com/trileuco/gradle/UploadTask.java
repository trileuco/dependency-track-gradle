package com.trileuco.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UploadTask extends DefaultTask {
  private final DependencyTrackExtension extension;
  public static final String TASK_NAME = "upload";

  @Inject
  public UploadTask(DependencyTrackExtension extension) {
    this.extension = extension;
  }

  @TaskAction
  public void publish() {
    DependencyTrackClient client =
        new DependencyTrackClient(
            extension.getHost().get(), extension.getRealm().get(), extension.getApiKey().get());
    client.publish(extension.getProjectName().get(),
            extension.getProjectVersion().get(),
            extension.getBomFile().get().getAsFile());
  }
}
