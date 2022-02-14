package com.trileuco.gradle;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class DependencyTrackTask extends DefaultTask {
  private final DependencyTrackExtension extension;

  @Inject
  public DependencyTrackTask(DependencyTrackExtension extension) {
    this.extension = extension;
  }

  @TaskAction
  public void publish() {
    extension.validate();
    DependencyTrackClient client =
        new DependencyTrackClient(
            extension.getHost().get(), extension.getRealm().get(), extension.getApiKey().get());
    client.publish(extension.getProjectId().get(), extension.getBomFile().get().getAsFile());
  }
}
