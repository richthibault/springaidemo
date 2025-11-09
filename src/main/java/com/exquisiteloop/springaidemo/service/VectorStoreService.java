package com.exquisiteloop.springaidemo.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VectorStoreService {

	private final VectorStore vectorStore;
	private final ObjectMapper objectMapper;

	public VectorStoreService(VectorStore vectorStore, ObjectMapper objectMapper) {
		this.vectorStore = vectorStore;
		this.objectMapper = objectMapper;
	}

	// Clear and reload our vector store
	// Uncomment the EventListener to run at startup, or just run the unit test
	//@EventListener(ApplicationReadyEvent.class)
	public void loadDemoData() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:data/*.json");

            List<Document> documents = new ArrayList<>();

            for (Resource resource : resources) {
                String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                JsonNode node = objectMapper.readTree(json);

                // Extract the text and metadata
                String text = node.path("text").asText();
                JsonNode metadataNode = node.path("metadata");
				// Convert metadata node into a simple Map<String, Object>
				Map<String, Object> metadata = objectMapper.convertValue(metadataNode, new TypeReference<Map<String, Object>>() {});

                // Add an import timestamp for traceability
                metadata.putIfAbsent("imported_at", OffsetDateTime.now().toString());

                Document doc = new Document(text, metadata);
                documents.add(doc);

                System.out.println("Prepared document: " + metadata.getOrDefault("company", "Unknown Company"));
            }

            if (!documents.isEmpty()) {
				// CAUTION! Deletes everything before repopulating
				deleteAllDocuments();
                vectorStore.add(documents);
                System.out.println("✅ Successfully added " + documents.size() + " documents to vector index.");
            } else {
                System.out.println("⚠️ No JSON files found in classpath: data/");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load demo JSON documents", e);
        }
    }

	public void deleteAllDocuments() {
		var idList = vectorStore.similaritySearch(SearchRequest.builder().query("*").topK(1000).build()).stream()
			//.filter(doc -> companyId.equals(doc.getMetadata().get("company_id")))
			.map(Document::getId)
			.toList();
		if(!idList.isEmpty())
			vectorStore.delete(idList);
	}

}
