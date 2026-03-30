package com.fz.patentmgn.model;

import java.math.BigDecimal;
import java.time.LocalDate;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class PatentCase {

    private String eventNo;
    private String contractNo;
    private String applicant;
    private String appName;
    private String appNameEn;
    private String originalCaseName;
    private String contractNotes;
    private BigDecimal contractUnitPrice;
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

    private BigDecimal hoursFee;
    private BigDecimal eventFee;
    private BigDecimal totalFee;
    private String feeDescription;

    private BigDecimal serviceFee = BigDecimal.ZERO; // 服務費
    private BigDecimal pageFee = BigDecimal.ZERO; // 超頁費
    private BigDecimal itemFee = BigDecimal.ZERO; // 超項費
    private BigDecimal customFee = BigDecimal.ZERO; // 自定義費

    private String taxType = "無"; // 稅額種類
    private BigDecimal taxRate = BigDecimal.ZERO; // 稅額% (例如 0.05 代表 5%)
    private BigDecimal withholdingTax = BigDecimal.ZERO; // 預扣稅額
    private BigDecimal salesTax = BigDecimal.ZERO; // 營業稅
    private BigDecimal preTaxAmount = BigDecimal.ZERO; // 未稅金額
    private String isWithheld = "否"; // 已預扣 (是/否, 預設 否)

    private BigDecimal totalInvoiced; // 請款合計
    private String currency = "TWD"; // 幣別
    private BigDecimal exchangeRate; // 兌NT
    private BigDecimal totalInNtd; // 合計NT
    private String isForeignCurrencyReceived = "否"; // 外幣收款 (是/否, 預設 否)

    private String invoiceRequestNo; // 請款單號
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate; // 收據日期
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate; // 發票日期
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentReceivedDate; // 收款日期
    private String invoiceNo; // 發票號碼
    private String voucherNo; // 立帳傳票號碼
    private BigDecimal actualReceived; // 實際收款
    private String invoiceNote; // 請款備註

    private String notes;
    private BigDecimal contractBalance; // 合約餘額 (暫存計算)
    private BigDecimal balanceAfter; // 結餘 (合約餘額 - 總費用)

    private BigDecimal usedBonusHours = BigDecimal.ZERO; // 本案使用贈送時數
    private BigDecimal bonusHoursBalance = BigDecimal.ZERO; // 贈送時數餘額 (本案後結餘)
    private BigDecimal startingBonusHours = BigDecimal.ZERO; // 本案開始時可用贈送時數

    public PatentCase() {
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppNameEn() {
        return appNameEn;
    }

    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn;
    }

    public String getOriginalCaseName() {
        return originalCaseName;
    }

    public void setOriginalCaseName(String originalCaseName) {
        this.originalCaseName = originalCaseName;
    }

    public String getContractNotes() {
        return contractNotes;
    }

    public void setContractNotes(String contractNotes) {
        this.contractNotes = contractNotes;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getTotalContractHours() {
        return totalContractHours;
    }

    public void setTotalContractHours(BigDecimal totalContractHours) {
        this.totalContractHours = totalContractHours;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getCaseFileNo() {
        return caseFileNo;
    }

    public void setCaseFileNo(String caseFileNo) {
        this.caseFileNo = caseFileNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public LocalDate getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public BigDecimal getExpectedHours() {
        return expectedHours;
    }

    public void setExpectedHours(BigDecimal expectedHours) {
        this.expectedHours = expectedHours;
    }

    public LocalDate getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(LocalDate actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public BigDecimal getActualHours() {
        return actualHours;
    }

    public void setActualHours(BigDecimal actualHours) {
        this.actualHours = actualHours;
    }

    public BigDecimal getHoursFee() {
        return hoursFee;
    }

    public void setHoursFee(BigDecimal hoursFee) {
        this.hoursFee = hoursFee;
    }

    public BigDecimal getEventFee() {
        return eventFee;
    }

    public void setEventFee(BigDecimal eventFee) {
        this.eventFee = eventFee;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getPageFee() {
        return pageFee;
    }

    public void setPageFee(BigDecimal pageFee) {
        this.pageFee = pageFee;
    }

    public BigDecimal getItemFee() {
        return itemFee;
    }

    public void setItemFee(BigDecimal itemFee) {
        this.itemFee = itemFee;
    }

    public BigDecimal getCustomFee() {
        return customFee;
    }

    public void setCustomFee(BigDecimal customFee) {
        this.customFee = customFee;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }

    public BigDecimal getPreTaxAmount() {
        return preTaxAmount;
    }

    public void setPreTaxAmount(BigDecimal preTaxAmount) {
        this.preTaxAmount = preTaxAmount;
    }

    public String getIsWithheld() {
        return isWithheld;
    }

    public void setIsWithheld(String isWithheld) {
        this.isWithheld = isWithheld;
    }

    public BigDecimal getTotalInvoiced() {
        return totalInvoiced;
    }

    public void setTotalInvoiced(BigDecimal totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalInNtd() {
        return totalInNtd;
    }

    public void setTotalInNtd(BigDecimal totalInNtd) {
        this.totalInNtd = totalInNtd;
    }

    public String getIsForeignCurrencyReceived() {
        return isForeignCurrencyReceived;
    }

    public void setIsForeignCurrencyReceived(String isForeignCurrencyReceived) {
        this.isForeignCurrencyReceived = isForeignCurrencyReceived;
    }

    public String getInvoiceRequestNo() {
        return invoiceRequestNo;
    }

    public void setInvoiceRequestNo(String invoiceRequestNo) {
        this.invoiceRequestNo = invoiceRequestNo;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getPaymentReceivedDate() {
        return paymentReceivedDate;
    }

    public void setPaymentReceivedDate(LocalDate paymentReceivedDate) {
        this.paymentReceivedDate = paymentReceivedDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimal getActualReceived() {
        return actualReceived;
    }

    public void setActualReceived(BigDecimal actualReceived) {
        this.actualReceived = actualReceived;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public BigDecimal getContractBalance() {
        return contractBalance;
    }

    public void setContractBalance(BigDecimal contractBalance) {
        this.contractBalance = contractBalance;
    }

    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getUsedBonusHours() {
        return usedBonusHours;
    }

    public void setUsedBonusHours(BigDecimal usedBonusHours) {
        this.usedBonusHours = usedBonusHours;
    }

    public BigDecimal getBonusHoursBalance() {
        return bonusHoursBalance;
    }

    public void setBonusHoursBalance(BigDecimal bonusHoursBalance) {
        this.bonusHoursBalance = bonusHoursBalance;
    }

    public BigDecimal getStartingBonusHours() {
        return startingBonusHours;
    }

    public void setStartingBonusHours(BigDecimal startingBonusHours) {
        this.startingBonusHours = startingBonusHours;
    }
}
