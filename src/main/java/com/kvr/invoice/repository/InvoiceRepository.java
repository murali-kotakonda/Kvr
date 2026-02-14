package com.kvr.invoice.repository;

import com.kvr.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    @Query("SELECT i FROM Invoice i WHERE " +
           "(:invoiceNumber IS NULL OR :invoiceNumber = '' OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :invoiceNumber, '%'))) AND " +
           "(:startDate IS NULL OR i.invoiceDate >= :startDate) AND " +
           "(:endDate IS NULL OR i.invoiceDate <= :endDate) AND " +
           "(:toName IS NULL OR :toName = '' OR i.toName = :toName)")
    List<Invoice> searchInvoices(@Param("invoiceNumber") String invoiceNumber,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("toName") String toName);
}
