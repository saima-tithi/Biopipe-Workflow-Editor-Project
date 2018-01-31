package com.project.spring.service;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="Workflow")
public class Workflow {
    private String type;
    private List<WorkflowStep> workflowSteps;
    
    @XmlAttribute
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @XmlElement(name="Step")
    public List<WorkflowStep> getWorkflowSteps() {
        return workflowSteps;
    }
    
    public void setWorkflowSteps(List<WorkflowStep> workflowSteps) {
        this.workflowSteps = workflowSteps;
    }
}
