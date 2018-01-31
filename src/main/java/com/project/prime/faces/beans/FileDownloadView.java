package com.project.prime.faces.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name="fileDownloadView")
public class FileDownloadView {
    private StreamedContent xmlFile;
    
    public StreamedContent getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(StreamedContent xmlFile) {
        this.xmlFile = xmlFile;
    }

    public FileDownloadView() {        
        try {
            InputStream stream = new FileInputStream(new File(FileUploadManagedBean.workflowFileForDownload));
            xmlFile = new DefaultStreamedContent(stream, "text/plain", "workflow.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    } 
}
