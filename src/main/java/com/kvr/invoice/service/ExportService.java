package com.kvr.invoice.service;

import com.kvr.invoice.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;
    private final AddressService addressService;
    
    public byte[] exportAllDataToExcel() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Users Sheet
            Sheet usersSheet = workbook.createSheet("Users");
            Row userHeader = usersSheet.createRow(0);
            String[] userHeaders = {"ID", "Username", "Email", "First Name", "Last Name", "GSTIN", "User Type", "Created"};
            for (int i = 0; i < userHeaders.length; i++) {
                Cell cell = userHeader.createCell(i);
                cell.setCellValue(userHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            int userRow = 1;
            for (var user : userRepository.findAll()) {
                Row row = usersSheet.createRow(userRow++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmailId());
                row.createCell(3).setCellValue(user.getFirstName());
                row.createCell(4).setCellValue(user.getLastName());
                row.createCell(5).setCellValue(user.getGstin());
                row.createCell(6).setCellValue(user.getUserType());
                row.createCell(7).setCellValue(user.getInsertTms() != null ? user.getInsertTms().toString() : "");
            }
            
            // Clients Sheet
            Sheet clientsSheet = workbook.createSheet("Clients");
            Row clientHeader = clientsSheet.createRow(0);
            String[] clientHeaders = {"ID", "Client Name", "Email", "Phone", "Mobile", "GSTIN", "Created"};
            for (int i = 0; i < clientHeaders.length; i++) {
                Cell cell = clientHeader.createCell(i);
                cell.setCellValue(clientHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            int clientRow = 1;
            for (var client : clientRepository.findAll()) {
                Row row = clientsSheet.createRow(clientRow++);
                row.createCell(0).setCellValue(client.getId());
                row.createCell(1).setCellValue(client.getClientName());
                row.createCell(2).setCellValue(client.getEmailId());
                row.createCell(3).setCellValue(client.getPhoneNumber());
                row.createCell(4).setCellValue(client.getMobileNumber());
                row.createCell(5).setCellValue(client.getGstin());
                row.createCell(6).setCellValue(client.getInsertTms() != null ? client.getInsertTms().toString() : "");
            }
            
            // Invoices Sheet
            Sheet invoicesSheet = workbook.createSheet("Invoices");
            Row invoiceHeader = invoicesSheet.createRow(0);
            String[] invoiceHeaders = {"ID", "Invoice Number", "Date", "From Name", "From GST", "To Name", "To GST", "State Code", "Vehicle Number", "Total Value", "Total CGST", "Total SGST", "Grand Total"};
            for (int i = 0; i < invoiceHeaders.length; i++) {
                Cell cell = invoiceHeader.createCell(i);
                cell.setCellValue(invoiceHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            int invoiceRow = 1;
            for (var invoice : invoiceRepository.findAll()) {
                Row row = invoicesSheet.createRow(invoiceRow++);
                row.createCell(0).setCellValue(invoice.getId());
                row.createCell(1).setCellValue(invoice.getInvoiceNumber());
                row.createCell(2).setCellValue(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().toString() : "");
                row.createCell(3).setCellValue(invoice.getFromName());
                row.createCell(4).setCellValue(invoice.getFromGst());
                row.createCell(5).setCellValue(invoice.getToName());
                row.createCell(6).setCellValue(invoice.getToGst());
                row.createCell(7).setCellValue(invoice.getStateCode());
                row.createCell(8).setCellValue(invoice.getVehicleNumber());
                row.createCell(9).setCellValue(invoice.getTotalValue() != null ? invoice.getTotalValue() : 0);
                row.createCell(10).setCellValue(invoice.getTotalCgst() != null ? invoice.getTotalCgst() : 0);
                row.createCell(11).setCellValue(invoice.getTotalSgst() != null ? invoice.getTotalSgst() : 0);
                row.createCell(12).setCellValue(invoice.getGrandTotal() != null ? invoice.getGrandTotal() : 0);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
