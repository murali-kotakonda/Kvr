package com.kvr.invoice.controller;

import com.kvr.invoice.model.Invoice;
import com.kvr.invoice.service.InvoiceService;
import com.kvr.invoice.service.UserService;
import com.kvr.invoice.service.ClientService;
import com.kvr.invoice.service.AddressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice saved = invoiceService.createInvoice(invoice);
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
}
