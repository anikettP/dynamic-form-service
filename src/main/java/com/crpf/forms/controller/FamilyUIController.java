package com.crpf.forms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FamilyUIController {

    @GetMapping("/api/auth/ui/family")
    public String familyPage() {
        return "family";
    }
}
