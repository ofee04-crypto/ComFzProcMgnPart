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

    public List<PatentCase> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCases();
        }
        String lowerKeyword = keyword.toLowerCase();
        return repository.findAll().stream()
                .filter(c -> 
                    (c.getContractNo() != null && c.getContractNo().toLowerCase().contains(lowerKeyword)) ||
                    (c.getApplicant() != null && c.getApplicant().toLowerCase().contains(lowerKeyword)) ||
                    (c.getAppName() != null && c.getAppName().toLowerCase().contains(lowerKeyword)) ||
                    (c.getAssignee() != null && c.getAssignee().toLowerCase().contains(lowerKeyword)) ||
                    (c.getCaseFileNo() != null && c.getCaseFileNo().toLowerCase().contains(lowerKeyword))
                )
                .collect(Collectors.toList());
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
