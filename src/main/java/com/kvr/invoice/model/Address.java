package com.kvr.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String addressType = "home";
    
    @Column(nullable = false)
    private String entityType;
    
    @Column(nullable = false)
    private Long entityId;
    
    private String houseNo;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    
    private Long createdBy;
    private Long updatedBy;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTms;
    
    private LocalDateTime updatedTms;
    
    @PrePersist
    protected void onCreate() {
        createdTms = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedTms = LocalDateTime.now();
    }
}
