package com.kvr.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String invoiceNumber;
    private LocalDate invoiceDate;
    
    // From Address
    private String fromName;
    private String fromAddress;
    private String fromGst;
    
    // To Address
    private String toName;
    private String toAddress;
    private String toGst;
    
    @Column(name = "state_code")
    private String stateCode;
    
    @Column(name = "vehicle_number")
    private String vehicleNumber;
    
    // Calculated totals
    private Double totalValue;
    private Double totalCgst;
    private Double totalSgst;
    private Double grandTotal;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();
    
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }
}
