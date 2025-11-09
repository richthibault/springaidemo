package com.exquisiteloop.springaidemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.exquisiteloop.springaidemo.model.AiPlatform;
import com.exquisiteloop.springaidemo.model.Company;

@Controller
public class WebController {

    @GetMapping("/")
    public String chat(ModelMap model) {
		model.addAttribute("aiPlatforms", AiPlatform.values());
		model.addAttribute("companies", Company.values());
        return "chat";
    }
}
