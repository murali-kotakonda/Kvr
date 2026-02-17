package com.kvr.invoice.controller;

import com.kvr.invoice.model.Invoice;
import com.kvr.invoice.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final ClientService clientService;
    private final AddressService addressService;
    private final AuditService auditService;
    private final PdfService pdfService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        return "index";
    }
    
    @GetMapping("/invoice/new")
    public String newInvoiceForm(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("users", userService.getAllActiveUsers());
        model.addAttribute("clients", clientService.getAllActiveClients());
        return "invoice-form";
    }
    
    @GetMapping("/api/address/{entityType}/{entityId}")
    @ResponseBody
    public ResponseEntity<?> getAddress(@PathVariable String entityType, @PathVariable Long entityId) {
        return ResponseEntity.ok(addressService.getAddressByEntity(entityType, entityId));
    }
    
    @PostMapping("/api/invoice")
    @ResponseBody
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Invoice saved = invoiceService.createInvoice(invoice);
        auditService.log("INVOICE", saved.getId(), "CREATE", username, "Invoice created: " + saved.getInvoiceNumber());
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/invoice/{id}")
    public String viewInvoice(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        Invoice invoice = invoiceService.getInvoice(id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("amountInWords", com.kvr.invoice.util.NumberToWordsUtil.convert(invoice.getGrandTotal()));
        return "invoice-view";
    }
    
    @GetMapping("/api/invoice/{id}")
    @ResponseBody
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoice(id));
    }
    
    @GetMapping("/invoices")
    public String listInvoices(@RequestParam(required = false) String invoiceNumber,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) String toName,
                              HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        
        LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate) : null;
        
        List<Invoice> invoices;
        if (invoiceNumber != null || start != null || end != null || year != null || toName != null) {
            invoices = invoiceService.searchInvoices(invoiceNumber, start, end, year, toName);
        } else {
            invoices = invoiceService.getAllInvoices();
        }
        
        model.addAttribute("invoices", invoices);
        model.addAttribute("clients", clientService.getAllActiveClients());
        model.addAttribute("invoiceNumber", invoiceNumber);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("year", year);
        model.addAttribute("toName", toName);
        return "invoice-list";
    }
    
    @GetMapping("/invoice/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, HttpSession session) {
        try {
            Invoice invoice = invoiceService.getInvoice(id);
            byte[] pdf = pdfService.generateInvoicePdf(invoice);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/invoice/{id}/history")
    public String viewHistory(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        model.addAttribute("invoice", invoiceService.getInvoice(id));
        model.addAttribute("history", auditService.getEntityHistory("INVOICE", id));
        return "invoice-history";
    }
    
    @GetMapping("/api/clients/search")
    @ResponseBody
    public ResponseEntity<List<?>> searchClients(@RequestParam String query) {
        return ResponseEntity.ok(clientService.searchClients(query));
    }
    
    @GetMapping("/api/users/search")
    @ResponseBody
    public ResponseEntity<List<?>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }
}
