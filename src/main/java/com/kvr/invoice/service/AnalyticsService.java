package com.kvr.invoice.service;

import com.kvr.invoice.model.Invoice;
import com.kvr.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final InvoiceRepository invoiceRepository;
    
    public Map<String, Double> getMonthlyRevenue(int year) {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
            .filter(inv -> inv.getInvoiceDate().getYear() == year)
            .collect(Collectors.groupingBy(
                inv -> inv.getInvoiceDate().getMonth().toString(),
                Collectors.summingDouble(Invoice::getGrandTotal)
            ));
    }
    
    public Map<Integer, Double> getAnnualRevenue() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
            .collect(Collectors.groupingBy(
                inv -> inv.getInvoiceDate().getYear(),
                Collectors.summingDouble(Invoice::getGrandTotal)
            ));
    }
    
    public Map<String, Object> getGSTSummary(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = invoiceRepository.findAll().stream()
            .filter(inv -> !inv.getInvoiceDate().isBefore(startDate) && !inv.getInvoiceDate().isAfter(endDate))
            .toList();
        
        double totalCGST = invoices.stream().mapToDouble(Invoice::getTotalCgst).sum();
        double totalSGST = invoices.stream().mapToDouble(Invoice::getTotalSgst).sum();
        double totalGST = totalCGST + totalSGST;
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCGST", totalCGST);
        summary.put("totalSGST", totalSGST);
        summary.put("totalGST", totalGST);
        summary.put("invoiceCount", invoices.size());
        return summary;
    }
    
    public List<Map<String, Object>> getTopClients(int limit) {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
            .collect(Collectors.groupingBy(
                Invoice::getToName,
                Collectors.summingDouble(Invoice::getGrandTotal)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(limit)
            .map(entry -> {
                Map<String, Object> client = new HashMap<>();
                client.put("name", entry.getKey());
                client.put("totalValue", entry.getValue());
                return client;
            })
            .collect(Collectors.toList());
    }
}
