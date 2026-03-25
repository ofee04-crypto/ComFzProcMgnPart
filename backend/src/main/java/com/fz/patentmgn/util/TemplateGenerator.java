package com.fz.patentmgn.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.File;

public class TemplateGenerator {
    public static void main(String[] args) throws Exception {
        String path = "src/main/resources/export_template.xlsx";
        File file = new File(path);
        if (file.exists()) {
            System.out.println("Template already exists at " + file.getAbsolutePath());
            return;
        }
        
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(file)) {
            Sheet sheet = workbook.createSheet("Cases");
            Row header = sheet.createRow(0);
            String[] columns = {"合約編號", "合約名稱", "案件編號", "任務名稱", "委派日期", "實際完成日", "實際時數", "時數費用", "事件費用", "結餘", "承辦人", "備註"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            
            workbook.write(out);
            System.out.println("Template generated successfully at " + file.getAbsolutePath());
        }
    }
}
