package com.example.springaidemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    /**
     * Serve the main chat page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Serve the chat page
     */
    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
