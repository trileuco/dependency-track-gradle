package com.trileuco.gradle;

/**
 * Description:
 * Author: meijinye
 * Date: 2023/7/8
 * Time: 22:27
 */
public class DTrackBody {
    private String projectId;
    private String projectName;
    private String projectVersion;
    private boolean autoCreate;
    private String bom;

    public DTrackBody(String projectId) {
        this.projectId = projectId;
    }

    public DTrackBody(String projectName, String projectVersion, boolean autoCreate, String bom) {
        this.projectName = projectName;
        this.projectVersion = projectVersion;
        this.autoCreate = autoCreate;
        this.bom = bom;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public String getBom() {
        return bom;
    }

}
