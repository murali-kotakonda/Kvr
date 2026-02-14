package com.kvr.invoice.service;

import com.kvr.invoice.model.User;
import com.kvr.invoice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(String username, String firstName, String lastName, String password, String emailId, String gstin, String userType) {
        if (gstin != null && !gstin.isEmpty()) {
            userRepository.findByGstin(gstin).ifPresent(u -> {
                throw new RuntimeException("GSTIN already exists");
            });
        }
        User user = new User();
        user.setUsername(username);
        user.setEmailId(emailId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setGstin(gstin);
        user.setUserType(userType != null ? userType : "user");
        user.setIsDeleted(false);
        return userRepository.save(user);
    }
    
    @Transactional
    public boolean validateLogin(String username, String password) {
        return userRepository.findByUsername(username)
            .map(user -> {
                if (Objects.equals(password, user.getPassword())) {
                    user.setFailCount(0);
                    userRepository.save(user);
                    return true;
                } else {
                    user.setFailCount(user.getFailCount() + 1);
                    userRepository.save(user);
                    return false;
                }
            })
            .orElse(false);
    }
    
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPassword(newPassword);
            userRepository.save(user);
        });
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    public List<User> getAllActiveUsers() {
        return userRepository.findByIsDeletedFalse();
    }
    
    public List<User> searchUsers(String search) {
        if (search == null || search.trim().isEmpty()) {
            return getAllActiveUsers();
        }
        return userRepository.searchAllFields(search);
    }
    
    @Transactional
    public User updateUser(Long id, String username, String firstName, String lastName, String emailId, String gstin, String userType) {
        return userRepository.findById(id).map(user -> {
            if (gstin != null && !gstin.isEmpty()) {
                userRepository.findByGstin(gstin).ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new RuntimeException("GSTIN already exists");
                    }
                });
            }
            user.setUsername(username);
            user.setEmailId(emailId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setGstin(gstin);
            if (userType != null) {
                user.setUserType(userType);
            }
            return userRepository.save(user);
        }).orElse(null);
    }
    
    @Transactional
    public void softDeleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setIsDeleted(true);
            userRepository.save(user);
        });
    }
}
