package com.trantor.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.trantor.entity.Contact;

public class ExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Contact> contacts;

	public ExcelExporter(List<Contact> contacts) {
		this.contacts = contacts;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Customers");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Contact_ID", style);
		createCell(row, 1, "FIRST_NAME", style);
		createCell(row, 2, "LAST_NAME", style);
		createCell(row, 3, "EMAIL_ADDRESS", style);
		createCell(row, 4, "CREATED_BY", style);
		createCell(row, 5, "CREATED_DATE", style);
		createCell(row, 6, "UPDATED_BY", style);
		createCell(row, 7, "UPDATED_DATE", style);
		createCell(row, 8, "IS_ACTIVE", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle styleHeader = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		styleHeader.setFont(font);

		for (Contact contact : contacts) {
			Row row = sheet.createRow(rowCount++);
			CellStyle style = workbook.createCellStyle();
			int columnCount = 0;
			if (contact.getContactId() % 2 == 0) {

				style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
				style.setFillPattern(FillPatternType.BIG_SPOTS);
			} else {
				style.setFillBackgroundColor(IndexedColors.RED.getIndex());
				style.setFillPattern(FillPatternType.DIAMONDS);
			}


			String cid = String.valueOf(contact.getContactId());
			createCell(row, columnCount++, cid, style);
			createCell(row, columnCount++, contact.getFirstName(), style);
			createCell(row, columnCount++, contact.getLastName(), style);
			createCell(row, columnCount++, contact.getEmailAddress(), style);
			createCell(row, columnCount++, contact.getCreatedBy(), style);
			createCell(row, columnCount++, contact.getCreatedDate().toString(), style);
			createCell(row, columnCount++, contact.getUpdatedBy(), style);
			createCell(row, columnCount++, contact.getUpdatedDate().toString(), style);
			createCell(row, columnCount++, contact.getIsActive(), style);

		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}
}
