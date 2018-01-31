package com.project.prime.faces.beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.spring.service.DefaultWorkflowService;
import com.project.spring.service.ParseWorkflowService;
import com.project.spring.service.Workflow;
import com.project.spring.service.WorkflowStep;

@ManagedBean(name="fileUploadManagedBean")
@SessionScoped
public class FileUploadManagedBean {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadManagedBean.class);
    public static String workflowFileForDownload;
    
    @ManagedProperty("#{parseWorkflowService}")
    private ParseWorkflowService parseWorkflowService;
    
    @ManagedProperty("#{defaultWorkflowService}")
    private DefaultWorkflowService defaultWorkflowService;
    
    private String workflowType;
    private UploadedFile xmlFile;
    private UploadedFile gaFile;
    private List<String> selectedAligners;
    private List<String> aligners;
    private List<String> selectedVariantCallers;
    private List<String> variantCallers;
    private List<String> rnaseqAligners;
    private List<String> selectedRnaseqAligners;
    private List<String> rnaseqAssemblers;
    private List<String> selectedRnaseqAssemblers;

    public ParseWorkflowService getParseWorkflowService() {
        return parseWorkflowService;
    }

    public void setParseWorkflowService(ParseWorkflowService parseWorkflowService) {
        this.parseWorkflowService = parseWorkflowService;
    }

    public DefaultWorkflowService getDefaultWorkflowService() {
        return defaultWorkflowService;
    }

    public void setDefaultWorkflowService(
        DefaultWorkflowService defaultWorkflowService) {
        this.defaultWorkflowService = defaultWorkflowService;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }
    
    public UploadedFile getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(UploadedFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    public UploadedFile getGaFile() {
        return gaFile;
    }

    public void setGaFile(UploadedFile gaFile) {
        this.gaFile = gaFile;
    }

    public List<String> getAligners() {
        return aligners;
    }

    public void setAligners(List<String> aligners) {
        this.aligners = aligners;
    }

    public List<String> getVariantCallers() {
        return variantCallers;
    }

    public void setVariantCallers(List<String> variantCallers) {
        this.variantCallers = variantCallers;
    }

    public List<String> getSelectedAligners() {
        return selectedAligners;
    }

    public void setSelectedAligners(List<String> selectedAligners) {
        this.selectedAligners = selectedAligners;
    }

    public List<String> getSelectedVariantCallers() {
        return selectedVariantCallers;
    }

    public void setSelectedVariantCallers(List<String> selectedVariantCallers) {
        this.selectedVariantCallers = selectedVariantCallers;
    }
    
    public List<String> getRnaseqAligners() {
        return rnaseqAligners;
    }

    public void setRnaseqAligners(List<String> rnaseqAligners) {
        this.rnaseqAligners = rnaseqAligners;
    }

    public List<String> getSelectedRnaseqAligners() {
        return selectedRnaseqAligners;
    }

    public void setSelectedRnaseqAligners(List<String> selectedRnaseqAligners) {
        this.selectedRnaseqAligners = selectedRnaseqAligners;
    }

    public List<String> getRnaseqAssemblers() {
        return rnaseqAssemblers;
    }

    public void setRnaseqAssemblers(List<String> rnaseqAssemblers) {
        this.rnaseqAssemblers = rnaseqAssemblers;
    }

    public List<String> getSelectedRnaseqAssemblers() {
        return selectedRnaseqAssemblers;
    }

    public void setSelectedRnaseqAssemblers(List<String> selectedRnaseqAssemblers) {
        this.selectedRnaseqAssemblers = selectedRnaseqAssemblers;
    }
    
    public void initialize() {
        this.workflowType = "";
        this.xmlFile = null;
        this.gaFile = null;
    }

    public void fileUploadListenerXml(FileUploadEvent e){
        // Get uploaded file from the FileUploadEvent
        this.xmlFile= e.getFile();
        // Add message
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage("File Uploaded Successfully"));
    }
    
    public void fileUploadListenerGa(FileUploadEvent e){
        // Get uploaded file from the FileUploadEvent
        this.gaFile= e.getFile();
        // Add message
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage("File Uploaded Successfully"));
    }
	
	public String parseWorkflowFile() {
	    if (!workflowType.isEmpty()) {
	        logger.info("Inside default option");
	        if (workflowType.equals("snp-indel")) {
	            aligners = defaultWorkflowService.getAlignerNames();
	            selectedAligners = new ArrayList<String>();
	            variantCallers = defaultWorkflowService.getVariantCallerNames();
	            selectedVariantCallers = new ArrayList<String>();
	            return "editVariantCallingWorkflow";
	        }
	        else if (workflowType.equals("rnaseq")) {
	            rnaseqAligners = defaultWorkflowService.getRnaseqAlignerNames();
	            selectedRnaseqAligners = new ArrayList<String>();
	            rnaseqAssemblers = defaultWorkflowService.getRnaseqAssemblerNames();
	            selectedRnaseqAssemblers = new ArrayList<String>();
	            return "editRnaSeqWorkflow";
	        }
	    }
	    else if (!xmlFile.getFileName().isEmpty()) {
	        logger.info("Inside parse xml");
	        Workflow workflow = parseWorkflowService.readXmlFile(xmlFile);
	        if(workflow.getType().equals("variant-calling")) {	            
                aligners = defaultWorkflowService.getAlignerNames();
                selectedAligners = new ArrayList<String>();
                variantCallers = defaultWorkflowService.getVariantCallerNames();
                selectedVariantCallers = new ArrayList<String>();
                for (WorkflowStep workflowStep:workflow.getWorkflowSteps()) {
                    if(workflowStep.getStepType().equals("Alignment")) {
                        selectedAligners.add(workflowStep.getToolName());
                    }
                    else if(workflowStep.getStepType().equals("Variant Calling")) {
                        selectedVariantCallers.add(workflowStep.getToolName());
                    }
                }
                return "editVariantCallingWorkflow";
	        }
            else if (workflow.getType().equals("rna-seq")) {
                rnaseqAligners = defaultWorkflowService.getRnaseqAlignerNames();
                selectedRnaseqAligners = new ArrayList<String>();
                rnaseqAssemblers = defaultWorkflowService.getRnaseqAssemblerNames();
                selectedRnaseqAssemblers = new ArrayList<String>();
                for (WorkflowStep workflowStep:workflow.getWorkflowSteps()) {
                    if(workflowStep.getStepType().equals("RNA-Seq-Alignment")) {
                        selectedRnaseqAligners.add(workflowStep.getToolName());
                    }
                    else if(workflowStep.getStepType().equals("RNA-Seq-Assembly")) {
                        selectedRnaseqAssemblers.add(workflowStep.getToolName());
                    }
                }
                return "editRnaSeqWorkflow";
            }
            else {
                aligners = defaultWorkflowService.getAlignerNames();
                selectedAligners = new ArrayList<String>();
                variantCallers = defaultWorkflowService.getVariantCallerNames();
                selectedVariantCallers = new ArrayList<String>();
                return "editVariantCallingWorkflow";
            }	        
	    }
	    else if (!gaFile.getFileName().isEmpty()) {
            logger.info("Inside parse ga");
            parseWorkflowService.readGaFile(gaFile);
            if (parseWorkflowService.isVarCallingWorkflow()) {
                selectedAligners = new ArrayList<String>();
                selectedVariantCallers = new ArrayList<String>();
                aligners = defaultWorkflowService.getAlignerNames();
                variantCallers = defaultWorkflowService.getVariantCallerNames();           
                selectedAligners = parseWorkflowService.getSelectedAlignersFromGa(aligners);
                selectedVariantCallers = parseWorkflowService.getSelectedVariantCallersFromGa(variantCallers);
                return "editVariantCallingWorkflow";
            }
            else if (parseWorkflowService.isRnaseqWorkflow()) {                
                selectedRnaseqAligners = new ArrayList<String>();                
                selectedRnaseqAssemblers = new ArrayList<String>();
                rnaseqAligners = defaultWorkflowService.getRnaseqAlignerNames();
                rnaseqAssemblers = defaultWorkflowService.getRnaseqAssemblerNames();
                selectedRnaseqAligners = 
                    parseWorkflowService.getSelectedRnaseqAlignersFromGa(rnaseqAligners);
                selectedRnaseqAssemblers = 
                    parseWorkflowService.getSelectedRnaseqAssemblersFromGa(rnaseqAssemblers);
                return "editRnaSeqWorkflow";
            }
            else {
                aligners = defaultWorkflowService.getAlignerNames();
                selectedAligners = new ArrayList<String>();
                variantCallers = defaultWorkflowService.getVariantCallerNames();
                selectedVariantCallers = new ArrayList<String>();
                return "editVariantCallingWorkflow";
            }
	    }
        // Add message
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage("Please select a default workflow or upload a workflow file."));
        return "index";
    }
	
	public String executeVariantCallingWorkflow() {
	    workflowFileForDownload = defaultWorkflowService.createXmlFileForVariantCalling(selectedAligners, selectedVariantCallers);
	    return "downloadFiles";
	}
	
	public String executeRnaSeqWorkflow() {
        workflowFileForDownload = defaultWorkflowService.createXmlFileForRnaSeq(selectedRnaseqAligners, selectedRnaseqAssemblers);
        return "downloadFiles";
    }
	
	public String goHome() {
	    initialize();
	    return "index";
	}
}
