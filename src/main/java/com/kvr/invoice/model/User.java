package com.kvr.invoice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    private String emailId;
    
    @Column(nullable = false)
    private String password;
    
    private String gstin;
    
    @Column(nullable = false)
    private String userType = "user";
    
    @Column(nullable = false)
    private Integer failCount = 0;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime insertTms;
    
    private LocalDateTime updatedTms;

   // add isDeleted field with @Column(nullable = false) and default false
    @Column(nullable = false)
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        insertTms = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedTms = LocalDateTime.now();
    }
}
