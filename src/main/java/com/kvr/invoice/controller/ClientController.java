package com.kvr.invoice.controller;

import com.kvr.invoice.model.Address;
import com.kvr.invoice.model.Client;
import com.kvr.invoice.service.ClientService;
import com.kvr.invoice.service.AddressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final AddressService addressService;
    
    @GetMapping
    public String listClients(@RequestParam(required = false) String search, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        
        model.addAttribute("clients", search != null ? clientService.searchClients(search) : clientService.getAllActiveClients());
        model.addAttribute("search", search);
        return "clients-list";
    }
    
    @GetMapping("/new")
    public String newClientForm(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        model.addAttribute("client", new Client());
        model.addAttribute("address", new Address());
        return "client-form";
    }
    
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        model.addAttribute("client", clientService.getClientById(id));
        model.addAttribute("address", addressService.getAddressByEntity("CLIENT", id));
        return "client-form";
    }
    
    @PostMapping("/save")
    public String saveClient(@ModelAttribute Client client, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        clientService.saveClient(client);
        return "redirect:/clients";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        clientService.softDeleteClient(id);
        return "redirect:/clients";
    }
    
    @GetMapping("/view/{id}")
    public String viewClient(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        
        model.addAttribute("client", clientService.getClientById(id));
        model.addAttribute("address", addressService.getAddressByEntity("CLIENT", id));
        return "client-view";
    }
}
