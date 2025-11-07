package com.exquisiteloop.springaidemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exquisiteloop.springaidemo.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private static final Logger logger = LoggerFactory.getLogger(ChatRestController.class);

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Handle chat requests
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        logger.debug("Received chat request: {}", request);
        
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(createResponse("Please provide a message"));
        }
        
        try {
            String response = chatService.chat(message);
            return ResponseEntity.ok(createResponse(response));
        } catch (Exception e) {
            logger.error("Error processing chat request", e);
            return ResponseEntity.internalServerError()
                    .body(createResponse("An error occurred: " + e.getMessage()));
        }
    }

    /**
     * Handle chat requests with RAG (Retrieval Augmented Generation)
     */
    // @PostMapping("/rag")
    // public ResponseEntity<Map<String, String>> chatWithRag(@RequestBody Map<String, String> request) {
    //     logger.debug("Received RAG chat request: {}", request);
        
    //     String message = request.get("message");
    //     if (message == null || message.trim().isEmpty()) {
    //         return ResponseEntity.badRequest()
    //                 .body(createResponse("Please provide a message"));
    //     }
        
    //     try {
    //         String response = chatService.chatWithRag(message);
    //         return ResponseEntity.ok(createResponse(response));
    //     } catch (Exception e) {
    //         logger.error("Error processing RAG chat request", e);
    //         return ResponseEntity.internalServerError()
    //                 .body(createResponse("An error occurred: " + e.getMessage()));
    //     }
    // }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("response", message);
        return response;
    }
}
