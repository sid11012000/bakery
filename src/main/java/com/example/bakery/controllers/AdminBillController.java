package com.example.bakery.controllers;

import com.example.bakery.service.pdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;

@Controller
public class AdminBillController {

    @Autowired
    pdfService pdfService;

    // 1. Show the Page
    @GetMapping("/admin/custom-bill")
    public String showCustomBillPage() {
        return "admin-custom-bill";
    }

    // 2. Generate the PDF
    @PostMapping("/admin/generate-custom-bill")
    public ResponseEntity<InputStreamResource> generateBill(
            @RequestParam String customerName,
            @RequestParam String phoneNumber,
            @RequestParam String[] itemNames,
            @RequestParam Double[] itemPrices,
            @RequestParam Integer[] itemQuantities) {

        // GENERATE A UNIQUE REFERENCE ID (e.g., KB-17632)
        // Using System time ensures it's always unique
        String customId = "KB-MANUAL-" + (System.currentTimeMillis() % 100000);

        ByteArrayInputStream bis = pdfService.createManualPdf(
                customerName, phoneNumber, itemNames, itemPrices, itemQuantities, customId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=custom_bill.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}