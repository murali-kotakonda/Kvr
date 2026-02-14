package com.kvr.invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer serialNo;
    private String description;
    private String hsnCode;
    private Double quantity;
    private Double ratePerKg;
    private Double totalValue;
    private Double cgstPercent;
    private Double cgstAmount;
    private Double sgstPercent;
    private Double sgstAmount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;
}
