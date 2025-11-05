package com.example.springaidemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Autowired
    public ChatService(ChatClient.Builder chatClientBuilder, 
                      @Autowired(required = false) VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    /**
     * Generate a chat response using Azure OpenAI
     */
    public String chat(String userMessage) {
        logger.debug("Processing chat message: {}", userMessage);
        
        try {
            String response = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();
            
            logger.debug("Generated response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error generating chat response", e);
            return "Sorry, I encountered an error processing your request: " + e.getMessage();
        }
    }

    /**
     * Generate a chat response with RAG (Retrieval Augmented Generation)
     * using Azure AI Search for context retrieval
     */
    public String chatWithRAG(String userMessage) {
        logger.debug("Processing chat message with RAG: {}", userMessage);
        
        try {
            // Retrieve relevant documents from Azure AI Search
            String context = retrieveContext(userMessage);
            
            // Construct prompt with context
            String augmentedPrompt = buildRAGPrompt(context, userMessage);
            
            // Generate response
            String response = chatClient.prompt()
                    .user(augmentedPrompt)
                    .call()
                    .content();
            
            logger.debug("Generated RAG response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error generating RAG response", e);
            // Fallback to regular chat if RAG fails
            return chat(userMessage);
        }
    }

    /**
     * Retrieve relevant context from Azure AI Search
     */
    private String retrieveContext(String query) {
        if (vectorStore == null) {
            logger.warn("VectorStore not available, skipping context retrieval");
            return "";
        }
        
        try {
            List<Document> documents = vectorStore.similaritySearch(query);
            
            if (documents.isEmpty()) {
                logger.debug("No relevant documents found");
                return "";
            }
            
            String context = documents.stream()
                    .map(Document::getContent)
                    .collect(Collectors.joining("\n\n"));
            
            logger.debug("Retrieved {} documents for context", documents.size());
            return context;
        } catch (Exception e) {
            logger.error("Error retrieving context from vector store", e);
            return "";
        }
    }

    /**
     * Build a prompt that includes retrieved context
     */
    private String buildRAGPrompt(String context, String userMessage) {
        if (context.isEmpty()) {
            return userMessage;
        }
        
        return String.format(
                "Use the following context to answer the question. " +
                "If the context doesn't contain relevant information, " +
                "answer based on your general knowledge.\n\n" +
                "Context:\n%s\n\n" +
                "Question: %s",
                context,
                userMessage
        );
    }
}
