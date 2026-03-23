package com.fz.patentmgn.service;

import com.fz.patentmgn.exception.CaseNotFoundException;
import com.fz.patentmgn.model.PatentCase;
import com.fz.patentmgn.repository.JsonPatentCaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatentCaseService {

    private final JsonPatentCaseRepository repository;

    public PatentCaseService(JsonPatentCaseRepository repository) {
        this.repository = repository;
    }

    public List<PatentCase> getAllCases() {
        return repository.findAll();
    }

    public PatentCase getCaseByEventNo(String eventNo) {
        return repository.findById(eventNo)
                .orElseThrow(() -> new CaseNotFoundException("找不到對應的案件：事件編號 " + eventNo));
    }

    public void saveCase(PatentCase patentCase) {
        repository.save(patentCase);
    }

    public void deleteCase(String eventNo) {
        repository.deleteById(eventNo);
    }

    public List<PatentCase> search(String keyword, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return repository.findAll().stream()
                .filter(c -> {
                    boolean keywordMatch = true;
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String lowerKeyword = keyword.toLowerCase();
                        keywordMatch = (c.getContractNo() != null && c.getContractNo().toLowerCase().contains(lowerKeyword)) ||
                                (c.getApplicant() != null && c.getApplicant().toLowerCase().contains(lowerKeyword)) ||
                                (c.getAppName() != null && c.getAppName().toLowerCase().contains(lowerKeyword)) ||
                                (c.getAssignee() != null && c.getAssignee().toLowerCase().contains(lowerKeyword)) ||
                                (c.getCaseFileNo() != null && c.getCaseFileNo().toLowerCase().contains(lowerKeyword));
                    }
                    
                    boolean dateMatch = true;
                    if (startDate != null || endDate != null) {
                        dateMatch = false; 
                        java.time.LocalDate d1 = c.getAssignmentDate();
                        java.time.LocalDate d2 = c.getExpectedCompletionDate();
                        java.time.LocalDate d3 = c.getActualCompletionDate();
                        
                        if (isDateInRange(d1, startDate, endDate) || 
                            isDateInRange(d2, startDate, endDate) || 
                            isDateInRange(d3, startDate, endDate)) {
                            dateMatch = true;
                        }
                    }
                    
                    return keywordMatch && dateMatch;
                })
                .collect(Collectors.toList());
    }

    private boolean isDateInRange(java.time.LocalDate date, java.time.LocalDate start, java.time.LocalDate end) {
        if (date == null) return false;
        if (start != null && date.isBefore(start)) return false;
        if (end != null && date.isAfter(end)) return false;
        return true;
    }

    public List<Map<String, Object>> getOverviewGroupedByName() {
        // 分組前先過濾掉沒有合約編號的資料
        Map<String, List<PatentCase>> grouped = repository.findAll().stream()
                .filter(c -> c.getContractNo() != null && !c.getContractNo().trim().isEmpty())
                .collect(Collectors.groupingBy(PatentCase::getContractNo));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String contractNo = entry.getKey();
                    List<PatentCase> cases = entry.getValue();

                    String appName = cases.get(0).getAppName();
                    BigDecimal contractAmount = cases.get(0).getContractAmount() != null ? cases.get(0).getContractAmount() : BigDecimal.ZERO;

                    BigDecimal sumActualHours = cases.stream()
                            .map(c -> c.getActualHours() != null ? c.getActualHours() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal sumTotalFee = cases.stream()
                            .map(c -> c.getTotalFee() != null ? c.getTotalFee() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                            
                    BigDecimal contractBalance = contractAmount.subtract(sumTotalFee);

                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("contractNo", contractNo);
                    map.put("appName", appName);
                    map.put("count", cases.size());
                    map.put("contractAmount", contractAmount);
                    map.put("contractBalance", contractBalance);
                    map.put("totalActualHours", sumActualHours);
                    map.put("totalFee", sumTotalFee);
                    
                    return map;
                })
                .collect(Collectors.toList());
    }
}
