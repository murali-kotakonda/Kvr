package com.kvr.invoice.controller;

import com.kvr.invoice.model.Address;
import com.kvr.invoice.model.User;
import com.kvr.invoice.service.UserService;
import com.kvr.invoice.service.AddressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AddressService addressService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       HttpSession session, Model model) {
        if (userService.validateLogin(username, password)) {
            User user = userService.getUserByUsername(username);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", username);
            session.setAttribute("userType", user.getUserType());
            return "redirect:/invoices";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }
    
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/user/login";
        
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile";
    }
    
    @GetMapping("/reset-password")
    public String resetPasswordPage(HttpSession session) {
        return session.getAttribute("userId") == null ? "redirect:/user/login" : "reset-password";
    }
    
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String newPassword, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/user/login";
        
        userService.resetPassword(userId, newPassword);
        model.addAttribute("success", "Password updated successfully");
        return "reset-password";
    }
    
    @GetMapping("/add")
    public String addUserPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/user/profile";
        }
        return "add-user";
    }
    
    @GetMapping("/list")
    public String listUsers(@RequestParam(required = false) String search, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            model.addAttribute("error", "Access denied. Admin privileges required.");
            return "redirect:/user/profile";
        }
        model.addAttribute("users", search != null ? userService.searchUsers(search) : userService.getAllActiveUsers());
        model.addAttribute("search", search);
        return "users-list";
    }
    
    @PostMapping("/add")
    public String addUser(@RequestParam String username, @RequestParam String firstName,
                         @RequestParam String lastName, @RequestParam String password,
                         @RequestParam(required = false) String emailId,
                         @RequestParam(required = false) String gstin,
                         @RequestParam(required = false) String userType,
                         HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/user/profile";
        }
        userService.createUser(username, firstName, lastName, password, emailId, gstin, userType);
        return "redirect:/user/list";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        session.invalidate();
        model.addAttribute("username", username);
        return "logout";
    }
    
    @GetMapping("/edit/{id}")
    public String editUserPage(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/user/profile";
        }
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("address", addressService.getAddressByEntity("USER", id));
        return "user-edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam String username,
                            @RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam(required = false) String emailId,
                            @RequestParam(required = false) String gstin,
                            @RequestParam(required = false) String userType,
                            HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/user/profile";
        }
        userService.updateUser(id, username, firstName, lastName, emailId, gstin, userType);
        return "redirect:/user/list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/user/profile";
        }
        userService.softDeleteUser(id);
        return "redirect:/user/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        
        Long currentUserId = (Long) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");
        
        if (!"admin".equals(userType) && !currentUserId.equals(id)) {
            return "redirect:/user/profile";
        }
        
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("address", addressService.getAddressByEntity("USER", id));
        return "user-view";
    }
}
