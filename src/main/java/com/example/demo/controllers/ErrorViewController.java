// com.example.demo.controllers.ErrorViewController.java
package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorViewController {
    
    @GetMapping("/error/403")
    public String error403() {
        return "error/403";
    }
}
