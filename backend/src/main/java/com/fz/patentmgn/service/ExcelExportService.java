package com.fz.patentmgn.service;

import com.fz.patentmgn.model.PatentCase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportCases(List<PatentCase> cases) {
        try (Workbook workbook = loadTemplate();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 數據從第二列開始 (Row index 1)
            int rowIndex = 1;
            
            for (PatentCase pc : cases) {
                Row row = sheet.createRow(rowIndex++);
                
                row.createCell(0).setCellValue(pc.getContractNo() != null ? pc.getContractNo() : "");
                row.createCell(1).setCellValue(pc.getAppName() != null ? pc.getAppName() : "");
                row.createCell(2).setCellValue(pc.getCaseFileNo() != null ? pc.getCaseFileNo() : "");
                row.createCell(3).setCellValue(pc.getTaskName() != null ? pc.getTaskName() : "");
                row.createCell(4).setCellValue(pc.getTaskDescription() != null ? pc.getTaskDescription() : "");
                row.createCell(5).setCellValue(pc.getAssignmentDate() != null ? pc.getAssignmentDate().toString() : "");
                row.createCell(6).setCellValue(pc.getExpectedCompletionDate() != null ? pc.getExpectedCompletionDate().toString() : "");
                
                double expectedHours = pc.getExpectedHours() != null ? pc.getExpectedHours().doubleValue() : 0.0;
                row.createCell(7).setCellValue(expectedHours);

                row.createCell(8).setCellValue(pc.getActualCompletionDate() != null ? pc.getActualCompletionDate().toString() : "");
                
                double actualHours = pc.getActualHours() != null ? pc.getActualHours().doubleValue() : 0.0;
                row.createCell(9).setCellValue(actualHours);
                
                double hoursFee = pc.getHoursFee() != null ? pc.getHoursFee().doubleValue() : 0.0;
                row.createCell(10).setCellValue(hoursFee);
                
                double eventFee = pc.getEventFee() != null ? pc.getEventFee().doubleValue() : 0.0;
                row.createCell(11).setCellValue(eventFee);
                
                double balance = pc.getBalanceAfter() != null ? pc.getBalanceAfter().doubleValue() : 0.0;
                row.createCell(12).setCellValue(balance);
                
                row.createCell(13).setCellValue(pc.getAssignee() != null ? pc.getAssignee() : "");
                row.createCell(14).setCellValue(pc.getNotes() != null ? pc.getNotes() : "");
            }
            
            // 強制重新計算公式
            workbook.setForceFormulaRecalculation(true);
            
            // 自動調整欄寬
            for (int i = 0; i < 15; i++) {
                sheet.autoSizeColumn(i);
            }

            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
            
        } catch (Exception e) {
            throw new RuntimeException("Excel 匯出失敗: " + e.getMessage(), e);
        }
    }

    private Workbook loadTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("export_template.xlsx");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return new XSSFWorkbook(is);
                }
            }
        } catch (Exception e) {
            // 讀取失敗則建立預設樣板
        }
        
        // 建立預設樣板
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cases");
        Row header = sheet.createRow(0);
        String[] columns = {"合約編號", "合約名稱", "案件編號", "任務名稱", "工作項目簡述", "委派日期", "預計完成日", "預計使用時數", "實際完成日", "實際時數", "時數費用", "事件費用", "結餘", "承辦人", "備註"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        return workbook;
    }
}
