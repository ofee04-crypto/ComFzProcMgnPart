package com.fz.patentmgn.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CaseNotFoundException.class)
    public String handleCaseNotFoundException(CaseNotFoundException ex, Model model) {
        log.warn("案件處理失敗: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DataWriteException.class)
    public String handleDataWriteException(DataWriteException ex, Model model) {
        log.error("資料存儲異常: ", ex);
        model.addAttribute("errorMessage", "系統無法存取資料庫檔案，請聯絡管理員。");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("系統未預期錯誤: ", ex);
        model.addAttribute("errorMessage", "發生未預期錯誤：" + ex.getMessage());
        return "error";
    }
}
