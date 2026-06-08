package com.crpf.forms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/Home")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/education")
    public String education() {
        return "education";
    }

    @GetMapping("/family")
    public String family() {
        return "family";
    }
}