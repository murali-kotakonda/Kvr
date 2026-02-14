package com.kvr.invoice.repository;

import com.kvr.invoice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByIsDeletedFalse();
    Optional<User> findByGstin(String gstin);
    
    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND (" +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.emailId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.gstin) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<User> searchAllFields(@Param("search") String search);
}
