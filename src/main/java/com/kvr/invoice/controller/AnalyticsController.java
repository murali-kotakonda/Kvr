package com.kvr.invoice.controller;

import com.kvr.invoice.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

@Controller
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    
    @GetMapping
    public String dashboard(HttpSession session, Model model, 
                          @RequestParam(required = false) Integer year) {
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType) && !"accountant".equals(userType)) {
            return "redirect:/";
        }
        
        int selectedYear = year != null ? year : Year.now().getValue();
        
        Map<String, Double> monthlyRevenue = analyticsService.getMonthlyRevenue(selectedYear);
        double totalRevenue = monthlyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
        
        // Create ordered monthly data for table
        java.util.LinkedHashMap<String, Double> orderedMonthlyData = new java.util.LinkedHashMap<>();
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", 
                          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        for (String month : months) {
            orderedMonthlyData.put(month, monthlyRevenue.getOrDefault(month, 0.0));
        }
        
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("monthlyData", orderedMonthlyData);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("annualRevenue", analyticsService.getAnnualRevenue());
        model.addAttribute("topClients", analyticsService.getTopClients(10));
        model.addAttribute("selectedYear", selectedYear);
        
        return "analytics-dashboard";
    }
    
    @GetMapping("/gst-summary")
    @ResponseBody
    public Map<String, Object> getGSTSummary(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return analyticsService.getGSTSummary(
            LocalDate.parse(startDate), 
            LocalDate.parse(endDate)
        );
    }
}
