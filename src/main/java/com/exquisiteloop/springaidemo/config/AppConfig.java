package com.exquisiteloop.springaidemo.config;

import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingOptions;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

@Configuration
public class AppConfig {	

	// a little setup is required to use the embedding model from Azure OpenAI with Pinecone vector store

	@Bean
	public OpenAIClient azureOpenAIClient(
			@Value("${spring.ai.azure.openai.endpoint}") String endpoint,
			@Value("${spring.ai.azure.openai.api-key}") String apiKey) {

		return new OpenAIClientBuilder()
				.credential(new AzureKeyCredential(apiKey))
				.endpoint(endpoint)
				.buildClient();
	}

	@Bean
	public EmbeddingModel embeddingModel(OpenAIClient azureOpenAiClient,
			@Value("${spring.ai.azure.openai.embedding.options.deployment-name}") String deploymentName) {
		var options = AzureOpenAiEmbeddingOptions.builder().deploymentName(deploymentName).dimensions(1024).build();
		return new AzureOpenAiEmbeddingModel(azureOpenAiClient, MetadataMode.EMBED, options);
	}

}
