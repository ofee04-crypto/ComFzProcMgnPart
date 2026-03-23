package com.fz.patentmgn.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PatentCase {

    private String eventNo;           
    private String contractNo;        
    private String applicant;         
    private String appName;           
    private String appNameEn;         
    private String originalCaseName;  
    private String contractNotes;     
    
    private BigDecimal contractAmount;     
    private BigDecimal totalContractHours; 
    private BigDecimal hourlyRate;         

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;      
    private String eventContent;      
    private String assignee;          
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate completionDate; 
    
    private String caseFileNo;        
    private String taskName;          
    private String taskDescription;   
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate; 
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedCompletionDate; 
    private BigDecimal expectedHours;         
    
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualCompletionDate;   
    private BigDecimal actualHours;           
    private BigDecimal remainingHours;        
    private String notes;                     

    public PatentCase() {
    }

    public PatentCase(String eventNo, String contractNo, String applicant, String appName, String appNameEn, String originalCaseName, String contractNotes, BigDecimal contractAmount, BigDecimal totalContractHours, BigDecimal hourlyRate, LocalDate eventDate, String eventContent, String assignee, LocalDate completionDate, String caseFileNo, String taskName, String taskDescription, LocalDate assignmentDate, LocalDate expectedCompletionDate, BigDecimal expectedHours, LocalDate actualCompletionDate, BigDecimal actualHours, BigDecimal remainingHours, String notes) {
        this.eventNo = eventNo;
        this.contractNo = contractNo;
        this.applicant = applicant;
        this.appName = appName;
        this.appNameEn = appNameEn;
        this.originalCaseName = originalCaseName;
        this.contractNotes = contractNotes;
        this.contractAmount = contractAmount;
        this.totalContractHours = totalContractHours;
        this.hourlyRate = hourlyRate;
        this.eventDate = eventDate;
        this.eventContent = eventContent;
        this.assignee = assignee;
        this.completionDate = completionDate;
        this.caseFileNo = caseFileNo;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.assignmentDate = assignmentDate;
        this.expectedCompletionDate = expectedCompletionDate;
        this.expectedHours = expectedHours;
        this.actualCompletionDate = actualCompletionDate;
        this.actualHours = actualHours;
        this.remainingHours = remainingHours;
        this.notes = notes;
    }

    public String getEventNo() { return eventNo; }
    public void setEventNo(String eventNo) { this.eventNo = eventNo; }

    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }

    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public String getAppNameEn() { return appNameEn; }
    public void setAppNameEn(String appNameEn) { this.appNameEn = appNameEn; }

    public String getOriginalCaseName() { return originalCaseName; }
    public void setOriginalCaseName(String originalCaseName) { this.originalCaseName = originalCaseName; }

    public String getContractNotes() { return contractNotes; }
    public void setContractNotes(String contractNotes) { this.contractNotes = contractNotes; }

    public BigDecimal getContractAmount() { return contractAmount; }
    public void setContractAmount(BigDecimal contractAmount) { this.contractAmount = contractAmount; }

    public BigDecimal getTotalContractHours() { return totalContractHours; }
    public void setTotalContractHours(BigDecimal totalContractHours) { this.totalContractHours = totalContractHours; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getEventContent() { return eventContent; }
    public void setEventContent(String eventContent) { this.eventContent = eventContent; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }

    public String getCaseFileNo() { return caseFileNo; }
    public void setCaseFileNo(String caseFileNo) { this.caseFileNo = caseFileNo; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getTaskDescription() { return taskDescription; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }

    public LocalDate getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }

    public LocalDate getExpectedCompletionDate() { return expectedCompletionDate; }
    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) { this.expectedCompletionDate = expectedCompletionDate; }

    public BigDecimal getExpectedHours() { return expectedHours; }
    public void setExpectedHours(BigDecimal expectedHours) { this.expectedHours = expectedHours; }

    public LocalDate getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(LocalDate actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }

    public BigDecimal getActualHours() { return actualHours; }
    public void setActualHours(BigDecimal actualHours) { this.actualHours = actualHours; }

    public BigDecimal getRemainingHours() { return remainingHours; }
    public void setRemainingHours(BigDecimal remainingHours) { this.remainingHours = remainingHours; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

}
