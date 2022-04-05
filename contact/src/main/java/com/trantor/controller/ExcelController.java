package com.trantor.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.trantor.entity.Contact;
import com.trantor.repository.ContactRepository;
import com.trantor.service.ExcelExporter;
import com.trantor.service.ExcelService;

@Controller
public class ExcelController {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ExcelService excelService;

	@GetMapping("/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=contact.xlsx";
		response.setHeader(headerKey, headerValue);

		List<Contact> contacts = contactRepository.findAll();

		ExcelExporter excelExporter = new ExcelExporter(contacts);

		excelExporter.export(response);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
		if (UploadController.checkExcelFormat(file)) {
			this.excelService.saveHelper(file);
			return ResponseEntity.ok(Map.of("message", "File is uploaded & Data is Saved in your Database."));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file.");
	}
}
