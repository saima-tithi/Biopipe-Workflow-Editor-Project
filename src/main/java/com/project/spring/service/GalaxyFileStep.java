package com.project.spring.service;

import java.util.ArrayList;
import java.util.List;

public class GalaxyFileStep {
    private int number;
    private String name;
    private String toolName;
    private String toolOwner;
    private String toolVersion;
    private List<Integer> inputStepList;
    private boolean isInput;
    private String inputType;
    private List<String> inputStepTypeList;
    
    public GalaxyFileStep(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolOwner() {
        return toolOwner;
    }

    public void setToolOwner(String toolOwner) {
        this.toolOwner = toolOwner;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }
    
    public void addInputStep(int step) {
        if(inputStepList == null)
            inputStepList = new ArrayList<Integer>();
        inputStepList.add(step);
    }
    
    public List<Integer> getInputStepList() {
        return inputStepList;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean isInput) {
        this.isInput = isInput;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    
    public void addInputStepType(String inputType) {
        if(inputStepTypeList == null)
            inputStepTypeList = new ArrayList<String>();
        inputStepTypeList.add(inputType);
    }
    
    public List<String> getInputStepTypeList() {
        return inputStepTypeList;
    }
}
