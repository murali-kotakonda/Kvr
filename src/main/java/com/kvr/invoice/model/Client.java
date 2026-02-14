package com.kvr.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String clientName;
    
    private String emailId;
    private String phoneNumber;
    private String mobileNumber;
    private String gstin;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime insertTms;
    
    private LocalDateTime updatedTms;
    
    @PrePersist
    protected void onCreate() {
        insertTms = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedTms = LocalDateTime.now();
    }
}
