package com.fz.patentmgn.controller;

import com.fz.patentmgn.model.PatentCase;
import com.fz.patentmgn.service.LogService;
import com.fz.patentmgn.service.PatentCaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PatentCaseController {

    private final PatentCaseService service;
    private final LogService logService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    
    @Autowired
    private ApplicationContext context;

    public PatentCaseController(PatentCaseService service, LogService logService, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.service = service;
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String index(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
        model.addAttribute("cases", service.search(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "index";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        model.addAttribute("patentCase", new PatentCase());
        model.addAttribute("isEdit", false);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        
        try {
            java.util.Map<String, PatentCase> uniqueContracts = new java.util.HashMap<>();
            for (PatentCase pc : service.getAllCases()) {
                if (pc.getContractNo() != null && !pc.getContractNo().trim().isEmpty()) {
                    uniqueContracts.put(pc.getContractNo(), pc);
                }
            }
            String contractsJson = objectMapper.writeValueAsString(uniqueContracts);
            model.addAttribute("contractsJson", contractsJson);
        } catch (Exception e) {
            model.addAttribute("contractsJson", "{}");
        }
        
        return "form";
    }

    @PostMapping("/save")
    public String saveCase(@ModelAttribute PatentCase patentCase, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        boolean isNew = false;
        if (patentCase.getEventNo() == null || patentCase.getEventNo().trim().isEmpty()) {
            patentCase.setEventNo("EVT-" + System.currentTimeMillis());
            isNew = true;
        }

        // Auto-calculate hourly rate: ContractAmount / TotalContractHours (rounded to 1 decimal point)
        java.math.BigDecimal hourlyRate = patentCase.getHourlyRate();
        if (patentCase.getContractAmount() != null && patentCase.getTotalContractHours() != null
                && patentCase.getTotalContractHours().compareTo(java.math.BigDecimal.ZERO) != 0) {
            hourlyRate = patentCase.getContractAmount()
                    .divide(patentCase.getTotalContractHours(), 1, java.math.RoundingMode.HALF_UP);
            patentCase.setHourlyRate(hourlyRate);
        }

        // Auto-calculate hoursFee: actualHours * hourlyRate
        java.math.BigDecimal hoursFee = java.math.BigDecimal.ZERO;
        if (patentCase.getActualHours() != null && hourlyRate != null) {
            hoursFee = patentCase.getActualHours().multiply(hourlyRate);
            patentCase.setHoursFee(hoursFee);
        } else {
            patentCase.setHoursFee(null);
        }

        // Auto-calculate totalFee: hoursFee + eventFee
        java.math.BigDecimal total = hoursFee;
        if (patentCase.getEventFee() != null) {
            total = total.add(patentCase.getEventFee());
        }
        if (patentCase.getHoursFee() != null || patentCase.getEventFee() != null) {
            patentCase.setTotalFee(total);
        } else {
            patentCase.setTotalFee(null);
        }

        service.saveCase(patentCase);
        
        if (isNew) {
            logService.recordLog(username, "建立新案件: " + patentCase.getEventNo());
        } else {
            logService.recordLog(username, "更新案件: " + patentCase.getEventNo());
        }
        
        redirectAttributes.addFlashAttribute("message", "案件儲存成功");
        return "redirect:/";
    }

    @GetMapping("/edit/{eventNo}")
    public String editForm(@PathVariable String eventNo, Model model, HttpSession session) {
        model.addAttribute("patentCase", service.getCaseByEventNo(eventNo));
        model.addAttribute("isEdit", true);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "form";
    }

    @PostMapping("/delete/{eventNo}")
    public String deleteCase(@PathVariable String eventNo, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        service.deleteCase(eventNo);
        logService.recordLog(username, "刪除案件: " + eventNo);
        redirectAttributes.addFlashAttribute("message", "案件已刪除");
        return "redirect:/";
    }

    @GetMapping("/overview")
    public String overview(Model model, HttpSession session) {
        model.addAttribute("cases", service.getAllCases());
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "overview";
    }

    @GetMapping("/grouped")
    public String groupedOverview(Model model, HttpSession session) {
        model.addAttribute("groupedStats", service.getOverviewGroupedByName());
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "grouped";
    }

    @GetMapping("/logs")
    public String viewLogs(Model model, HttpSession session) {
        model.addAttribute("logs", logService.getAllLogs());
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "logs";
    }

    @PostMapping("/api/system/shutdown")
    @ResponseBody
    public String shutdown(HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        logService.recordLog(username, "關閉地端系統");
        
        new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            SpringApplication.exit(context, () -> 0);
        }).start();
        return "System shutting down...";
    }
}
