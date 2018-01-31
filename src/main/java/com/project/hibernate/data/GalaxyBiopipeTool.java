package com.project.hibernate.data;

public class GalaxyBiopipeTool {
    private long tableId;
    private String galaxyToolName;
    private String biopipeToolName;
    
    public long getTableId() {
        return tableId;
    }
    public void setTableId(long tableId) {
        this.tableId = tableId;
    }
    public String getGalaxyToolName() {
        return galaxyToolName;
    }
    public void setGalaxyToolName(String galaxyToolName) {
        this.galaxyToolName = galaxyToolName;
    }
    public String getBiopipeToolName() {
        return biopipeToolName;
    }
    public void setBiopipeToolName(String biopipeToolName) {
        this.biopipeToolName = biopipeToolName;
    }
}
