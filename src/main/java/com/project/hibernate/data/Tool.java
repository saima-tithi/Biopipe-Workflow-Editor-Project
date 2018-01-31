package com.project.hibernate.data;

public class Tool {
    private long toolId;
    private String toolName;
    private String toolType;
    private String toolDockerImage;
    
    public long getToolId() {
        return toolId;
    }
    public void setToolId(long toolId) {
        this.toolId = toolId;
    }
    public String getToolName() {
        return toolName;
    }
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    public String getToolType() {
        return toolType;
    }
    public void setToolType(String toolType) {
        this.toolType = toolType;
    }
    public String getToolDockerImage() {
        return toolDockerImage;
    }
    public void setToolDockerImage(String toolDockerImage) {
        this.toolDockerImage = toolDockerImage;
    }
}
