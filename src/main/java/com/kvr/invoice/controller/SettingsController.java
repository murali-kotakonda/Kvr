package com.kvr.invoice.controller;

import com.kvr.invoice.service.ExportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final ExportService exportService;
    
    @GetMapping
    public String settingsPage(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/user/profile";
        }
        return "settings";
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return ResponseEntity.status(403).build();
        }
        
        try {
            byte[] excelData = exportService.exportAllDataToExcel();
            String filename = "invoice_data_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
