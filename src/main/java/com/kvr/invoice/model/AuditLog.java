package com.kvr.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String entityType; // INVOICE, USER, CLIENT
    
    @Column(nullable = false)
    private Long entityId;
    
    @Column(nullable = false)
    private String action; // CREATE, UPDATE, DELETE
    
    @Column(nullable = false)
    private String username;
    
    @Column(columnDefinition = "TEXT")
    private String changes;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
