package com.project.spring.service;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"stepType", "toolName", "dockerImageName", "inputSteps"})
public class WorkflowStep {
    private int id;
    private String stepType;
    private String toolName;
    private String dockerImageName;
    private List<Integer> inputSteps;
    
    @XmlAttribute
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @XmlElement
    public String getStepType() {
        return stepType;
    }
    public void setStepType(String stepType) {
        this.stepType = stepType;
    }
    
    @XmlElement
    public String getToolName() {
        return toolName;
    }
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    
    @XmlElement
    public String getDockerImageName() {
        return dockerImageName;
    }
    public void setDockerImageName(String dockerImageName) {
        this.dockerImageName = dockerImageName;
    }
    
    @XmlElement
    public List<Integer> getInputSteps() {
        return inputSteps;
    }
    public void setInputSteps(List<Integer> inputSteps) {
        this.inputSteps = inputSteps;
    }
}
