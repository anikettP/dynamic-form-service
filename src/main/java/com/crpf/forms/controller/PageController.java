package com.crpf.forms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String home() {
        return "login";
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