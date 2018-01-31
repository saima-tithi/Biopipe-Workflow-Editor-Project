package com.project.spring.service;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primefaces.model.UploadedFile;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParseWorkflowService {
    private List<GalaxyFileStep> galaxyFileSteps = null;
    
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void readGaFile(UploadedFile gaFile) {
        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(gaFile.getInputstream()));
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals("steps")) {
                    readSteps(jsonReader);
                }
                else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //get 1st tool
        GalaxyFileStep firstTool = null;
        for(GalaxyFileStep step: galaxyFileSteps) {
            if(!step.isInput()) {
                firstTool = step;
                break;
            }
        }
        List<Integer> inputSteps = firstTool.getInputStepList();
        List<String> inputTypes = firstTool.getInputStepTypeList();
        for(int i = 0; i < inputSteps.size(); i++) {
            int stepNum = inputSteps.get(i);
            for(GalaxyFileStep step:galaxyFileSteps) {
                if(step.getNumber() == stepNum) {
                    step.setInputType(inputTypes.get(i));
                    break;
                }
            }
        }
    }
    
    public void readSteps(JsonReader jsonReader) throws Exception {
        galaxyFileSteps = new ArrayList<GalaxyFileStep>();
        jsonReader.beginObject();
        int index = 0;
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            GalaxyFileStep step = new GalaxyFileStep(index);
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name1 = jsonReader.nextName();
                if(name1.equals("name")) {
                    step.setName(jsonReader.nextString());
                    if(step.getName().equals("Input dataset")) {
                        step.setInput(true);
                    }
                }
                else if (name1.equals("tool_version") && jsonReader.peek() != JsonToken.NULL) {                
                    step.setToolVersion(jsonReader.nextString());
                }
                else if (name1.equals("tool_shed_repository")) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String name2 = jsonReader.nextName();
                        if (name2.equals("name")) {
                            step.setToolName(jsonReader.nextString());
                        }
                        else if (name2.equals("owner")) {
                            step.setToolOwner(jsonReader.nextString());
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                }
                else if (name1.equals("input_connections")) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String name3 = jsonReader.nextName();
                        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()) {
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()) {
                                    String name4 = jsonReader.nextName();
                                    if(name4.equals("id")) {
                                        int stepNum = jsonReader.nextInt();
                                        step.addInputStep(stepNum);
                                    }
                                    else {
                                        jsonReader.skipValue();
                                    }                                   
                                }
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                        }
                        else if (jsonReader.peek() == JsonToken.BEGIN_OBJECT){
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String name4 = jsonReader.nextName();
                                if(name4.equals("id")) {
                                    int stepNum = jsonReader.nextInt();
                                    step.addInputStep(stepNum);
                                }
                                else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                        }                           
                    }
                    jsonReader.endObject();
                }
                else if (name1.equals("inputs")) {
                    if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
                        jsonReader.beginArray();
                        while(jsonReader.hasNext()) {
                            jsonReader.beginObject();
                            while(jsonReader.hasNext()) {
                                String name2 = jsonReader.nextName();
                                if(name2.equals("name")) {
                                    step.addInputStepType(jsonReader.nextString());
                                }
                                else {
                                    jsonReader.skipValue();
                                }                               
                            }
                            jsonReader.endObject();
                        }
                        jsonReader.endArray();
                    }
                }
                else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            galaxyFileSteps.add(step);
            index++;
        }
        jsonReader.endObject();
    }
    
    public List<String> getSelectedAlignersFromGa(List<String> aligners) {
        List<String> galaxyTools = new ArrayList<String>();
        List<String> selectedAligners = new ArrayList<String>();
        
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        List<String> biopipeTools = 
            convertGalaxyToolToBiopipeTool(galaxyTools);
        for (String tool:biopipeTools) {
            if(aligners.contains(tool)) {
                selectedAligners.add(tool);
            }
        }
        return selectedAligners;
    }
    
    @Transactional
    public List<String> convertGalaxyToolToBiopipeTool(
        List<String> galaxySelectedTools) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Query query = session.createQuery("select biopipeToolName FROM GalaxyBiopipeTool WHERE galaxyToolName IN (:galaxySelectedTools)");
        query.setParameterList("galaxySelectedTools", galaxySelectedTools);
        @SuppressWarnings("unchecked")
        List<String> biopipeSelectedTools = query.list();
        return biopipeSelectedTools;
    }
    
    public List<String> getSelectedVariantCallersFromGa(List<String> variantCallers) {
        List<String> galaxyTools = new ArrayList<String>();
        List<String> selectedVariantCallers = new ArrayList<String>();
        
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        List<String> biopipeTools = 
            convertGalaxyToolToBiopipeTool(galaxyTools);
        for (String tool:biopipeTools) {
            if(variantCallers.contains(tool)) {
                selectedVariantCallers.add(tool);
            }
        }
        return selectedVariantCallers;
    }
    
    public Workflow readXmlFile(UploadedFile xmlFile) {
        Workflow workflow = new Workflow();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Workflow.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            workflow = (Workflow) jaxbUnmarshaller.unmarshal
                (new InputStreamReader(xmlFile.getInputstream()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return workflow;
    }
    
    public boolean isVarCallingWorkflow() {
        List<String> galaxyTools = new ArrayList<String>();
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        if (galaxyTools.contains("bwa") || galaxyTools.contains("bowtie2"))
            return true;
        else
            return false;
    }
    
    public boolean isRnaseqWorkflow() {
        List<String> galaxyTools = new ArrayList<String>();
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        if (galaxyTools.contains("tophat2_with_gene_annotations") 
            || galaxyTools.contains("rgrnastar"))
            return true;
        else
            return false;
    }
    
    public List<String> getSelectedRnaseqAlignersFromGa(List<String> rnaseqAligners) {
        List<String> galaxyTools = new ArrayList<String>();
        List<String> selectedRnaseqAligners = new ArrayList<String>();
        
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        List<String> biopipeTools = 
            convertGalaxyToolToBiopipeTool(galaxyTools);
        for (String tool:biopipeTools) {
            if(rnaseqAligners.contains(tool)) {
                selectedRnaseqAligners.add(tool);
            }
        }
        return selectedRnaseqAligners;
    }
    
    public List<String> getSelectedRnaseqAssemblersFromGa(List<String> rnaseqAssemblers) {
        List<String> galaxyTools = new ArrayList<String>();
        List<String> selectedRnaseqAssemblers = new ArrayList<String>();
        
        for (GalaxyFileStep galaxyFileStep: galaxyFileSteps) {
            galaxyTools.add(galaxyFileStep.getToolName());
        }
        List<String> biopipeTools = 
            convertGalaxyToolToBiopipeTool(galaxyTools);
        for (String tool:biopipeTools) {
            if(rnaseqAssemblers.contains(tool)) {
                selectedRnaseqAssemblers.add(tool);
            }
        }
        return selectedRnaseqAssemblers;
    }
}
