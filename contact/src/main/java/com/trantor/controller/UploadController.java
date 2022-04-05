package com.trantor.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.trantor.entity.Contact;

public class UploadController {
    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
    }

    //convert excel to list
    public static List<Contact> convertExcelToListOfProduct(InputStream is) {
        List<Contact> list = new ArrayList<>();

        try {
                XSSFWorkbook workbook = new XSSFWorkbook(is);

                XSSFSheet sheet = workbook.getSheet("Important_Doc");

                int rowNumber = 0;
                Iterator<Row> iterator = sheet.iterator();

                while (iterator.hasNext()) {
                    Row row = iterator.next();

                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }

                    Iterator<Cell> cells = row.iterator();
                    int cid = 0;
                    Contact contact = new Contact();
                    
                    

                    while (cells.hasNext()) {
                        Cell cell = cells.next();

                        switch (cid) {
                            case 0:
                                contact.setContactId((long)cell.getNumericCellValue());
                                break;
                            case 1:
                                contact.setFirstName(cell.getStringCellValue());
                                break;
                            case 2:
                                contact.setLastName(cell.getStringCellValue());
                                break;
                            case 3:
                                contact.setEmailAddress(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        cid++;
                    }
                    list.add(contact);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

