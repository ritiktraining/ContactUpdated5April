package com.trantor.service;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.trantor.entity.Contact;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    static String[] HEADERs = {"Contact_Id", "First_Name", "Last_Name", "Email", "Created_By", "Updated_By", "Is_Active"};
    static String SHEET = "Important_Doc";

    public static ByteArrayInputStream tutorialsToExcel(List<Contact> contacts) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            int rowIdx = 1;
            for (Contact contact : contacts) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(contact.getContactId());
                row.createCell(1).setCellValue(contact.getFirstName());
                row.createCell(2).setCellValue(contact.getLastName());
                row.createCell(3).setCellValue(contact.getEmailAddress());
                row.createCell(4).setCellValue(contact.getCreatedBy());
                row.createCell(5).setCellValue(contact.getUpdatedBy());
                row.createCell(6).setCellValue(contact.getIsActive());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}

