package com.kvr.invoice.repository;

import com.kvr.invoice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByIsDeletedFalse();
    
    @Query("SELECT c FROM Client c WHERE c.isDeleted = false AND " +
           "LOWER(c.clientName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Client> searchClients(@Param("search") String search);
    
    Optional<Client> findByGstin(String gstin);
    
    @Query("SELECT c FROM Client c WHERE c.isDeleted = false AND (" +
           "LOWER(c.clientName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.emailId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.gstin) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Client> searchAllFields(@Param("search") String search);
}
