package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class auditorController {
    @GetMapping("/auditor/dashboard")
    public String adminDashboard() {
        return "auditor/dashboard";
    }

    @GetMapping("/auditor/test")
    public String test() {
        return "auditor/test";
    }
}
