package com.trantor.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.trantor.controller.UploadController;
import com.trantor.entity.Contact;
import com.trantor.repository.ContactRepository;

@Service
public class ExcelService {

    @Autowired
    private ContactRepository contactRepo;

    public ByteArrayInputStream load() {
        List<Contact> contacts = contactRepo.findAll();
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(contacts);
        return in;
    }

    public void saveHelper(MultipartFile file){
        try {
            List<Contact> contactList = UploadController.convertExcelToListOfProduct(file.getInputStream());
            this.contactRepo.saveAll(contactList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}