package com.kvr.invoice.service;

import com.kvr.invoice.model.Invoice;
import com.kvr.invoice.model.InvoiceItem;
import com.kvr.invoice.repository.InvoiceRepository;
import com.kvr.invoice.util.NumberToWordsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        calculateTotals(invoice);
        for(InvoiceItem item:  invoice.getItems()){
            item.setInvoice(invoice);
        }
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        // Generate invoice number if not provided
        if (savedInvoice.getInvoiceNumber() == null || savedInvoice.getInvoiceNumber().isEmpty()) {
            savedInvoice.setInvoiceNumber("INV-" + savedInvoice.getId());
            savedInvoice = invoiceRepository.save(savedInvoice);
        }
        
        return savedInvoice;
    }
    
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
    
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    public List<Invoice> searchInvoices(String invoiceNumber, LocalDate startDate, LocalDate endDate, Integer year, String toName) {
        if (year != null) {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
        }
        return invoiceRepository.searchInvoices(invoiceNumber, startDate, endDate, toName);
    }
    
    private void calculateTotals(Invoice invoice) {
        double totalValue = 0;
        double totalCgst = 0;
        double totalSgst = 0;
        
        for (InvoiceItem item : invoice.getItems()) {
            // Calculate item total value
            double itemTotal = item.getQuantity() * item.getRatePerKg();
            item.setTotalValue(itemTotal);
            
            // Calculate CGST
            double cgst = (itemTotal * item.getCgstPercent()) / 100;
            item.setCgstAmount(cgst);
            
            // Calculate SGST
            double sgst = (itemTotal * item.getSgstPercent()) / 100;
            item.setSgstAmount(sgst);
            
            totalValue += itemTotal;
            totalCgst += cgst;
            totalSgst += sgst;
        }
        
        double grandTotal = totalValue + totalCgst + totalSgst;
        
        invoice.setTotalValue(totalValue);
        invoice.setTotalCgst(totalCgst);
        invoice.setTotalSgst(totalSgst);
        invoice.setGrandTotal(grandTotal);
    }
}
