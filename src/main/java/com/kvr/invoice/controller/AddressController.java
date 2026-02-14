package com.kvr.invoice.controller;

import com.kvr.invoice.model.Address;
import com.kvr.invoice.service.AddressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    
    @PostMapping("/save")
    public String saveAddress(@ModelAttribute Address address, 
                             @RequestParam String entityType,
                             @RequestParam Long entityId,
                             @RequestParam String returnUrl,
                             HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:" + returnUrl;
        }
        
        Long userId = (Long) session.getAttribute("userId");
        address.setEntityType(entityType);
        address.setEntityId(entityId);
        addressService.saveAddress(address, userId);
        
        return "redirect:" + returnUrl;
    }
}
