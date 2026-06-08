package com.crpf.forms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EducationUIController {

    @GetMapping("/api/auth/ui/education")
    public String educationPage() {
        return "education";
    }
}
