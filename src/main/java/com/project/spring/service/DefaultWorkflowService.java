package com.project.spring.service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultWorkflowService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultWorkflowService.class);
    private String scriptsDir = "/home/saima/eclipse-workspace/"
        + "Biopipe-Workflow-Editor-Project/scripts";
    
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Transactional
    public List<String> getAlignerNames() {
        Session session = this.sessionFactory.getCurrentSession();     
        List<String> alignerNames = session.createQuery
            ("select toolName from Tool where toolType = 'Alignment'").list();
        return alignerNames;
    }
    
    @Transactional
    public List<String> getVariantCallerNames() {
        Session session = this.sessionFactory.getCurrentSession();     
        List<String> variantCallerNames = session.createQuery
            ("select toolName from Tool where toolType = 'Variant Calling'").list();
        return variantCallerNames;
    }
    
    @Transactional
    public String getDockerImageName(String toolName) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery
            ("select toolDockerImage from Tool where toolName = :toolName");
        query.setParameter("toolName", toolName);
        String dockerImage = (String)query.list().get(0);
        return dockerImage;
    }
    
    public String createXmlFileForVariantCalling(List<String> selectedAligners,
        List<String> selectedVariantCallers) {
        String workflowFileForDownload = "";
        try {
            JAXBContext contextObj = JAXBContext.newInstance(Workflow.class);
            Marshaller marshallerObj = contextObj.createMarshaller(); 
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            int stepId = 1;
            List<WorkflowStep> workflowSteps = new ArrayList<WorkflowStep>();
            for (String alignerName:selectedAligners) {
                WorkflowStep workflowStep = new WorkflowStep();
                workflowStep.setId(stepId);               
                workflowStep.setStepType("Alignment");
                workflowStep.setToolName(alignerName);
                workflowStep.setDockerImageName(getDockerImageName(alignerName));
                List<Integer> inputSteps = new ArrayList<Integer>();
                inputSteps.add(0);
                workflowStep.setInputSteps(inputSteps);
                workflowSteps.add(workflowStep);
            }
            
            stepId++;
            for (String variantCallerName:selectedVariantCallers) {
                WorkflowStep workflowStep = new WorkflowStep();
                workflowStep.setId(stepId);               
                workflowStep.setStepType("Variant Calling");
                workflowStep.setToolName(variantCallerName);
                workflowStep.setDockerImageName(getDockerImageName(variantCallerName));
                List<Integer> inputSteps = new ArrayList<Integer>();
                inputSteps.add(stepId-1);
                workflowStep.setInputSteps(inputSteps);
                workflowSteps.add(workflowStep);
            }
            Workflow workflow = new Workflow();
            workflow.setType("variant-calling");
            workflow.setWorkflowSteps(workflowSteps);
            
            workflowFileForDownload = scriptsDir + 
                "/workflow-" + System.currentTimeMillis() + ".xml";
            logger.info(workflowFileForDownload);
            marshallerObj.marshal(workflow, new FileOutputStream(workflowFileForDownload));
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return workflowFileForDownload;
    }
    
    @Transactional
    public List<String> getRnaseqAlignerNames() {
        Session session = this.sessionFactory.getCurrentSession();     
        List<String> rnaseqAlignerNames = session.createQuery
            ("select toolName from Tool where toolType = 'RNA-Seq-Alignment'").list();
        return rnaseqAlignerNames;
    }
    
    @Transactional
    public List<String> getRnaseqAssemblerNames() {
        Session session = this.sessionFactory.getCurrentSession();     
        List<String> rnaseqAssemblerNames = session.createQuery
            ("select toolName from Tool where toolType = 'RNA-Seq-Assembly'").list();
        return rnaseqAssemblerNames;
    }
    
    public String createXmlFileForRnaSeq(List<String> selectedRnaseqAligners, 
        List<String> selectedRnaseqAssemblers) {
        String workflowFileForDownload = "";
        try {
            JAXBContext contextObj = JAXBContext.newInstance(Workflow.class);
            Marshaller marshallerObj = contextObj.createMarshaller(); 
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            int stepId = 1;
            List<WorkflowStep> workflowSteps = new ArrayList<WorkflowStep>();
            for (String rnaseqAlignerName:selectedRnaseqAligners) {
                WorkflowStep workflowStep = new WorkflowStep();
                workflowStep.setId(stepId);               
                workflowStep.setStepType("RNA-Seq-Alignment");
                workflowStep.setToolName(rnaseqAlignerName);
                workflowStep.setDockerImageName(getDockerImageName(rnaseqAlignerName));
                List<Integer> inputSteps = new ArrayList<Integer>();
                inputSteps.add(0);
                workflowStep.setInputSteps(inputSteps);
                workflowSteps.add(workflowStep);
            }
            
            stepId++;
            for (String rnaseqAssemblerName:selectedRnaseqAssemblers) {
                WorkflowStep workflowStep = new WorkflowStep();
                workflowStep.setId(stepId);               
                workflowStep.setStepType("RNA-Seq-Assembly");
                workflowStep.setToolName(rnaseqAssemblerName);
                workflowStep.setDockerImageName(getDockerImageName(rnaseqAssemblerName));
                List<Integer> inputSteps = new ArrayList<Integer>();
                inputSteps.add(stepId-1);
                workflowStep.setInputSteps(inputSteps);
                workflowSteps.add(workflowStep);
            }
            Workflow workflow = new Workflow();
            workflow.setType("rna-seq");
            workflow.setWorkflowSteps(workflowSteps);
            
            workflowFileForDownload = scriptsDir + 
                "/workflow-" + System.currentTimeMillis() + ".xml";
            logger.info(workflowFileForDownload);
            marshallerObj.marshal(workflow, new FileOutputStream(workflowFileForDownload));
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return workflowFileForDownload;
    }
}
