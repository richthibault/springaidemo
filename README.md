# Spring AI Demo

A Spring Boot application demonstrating integration with Azure OpenAI ChatGPT, Perplexity.ai, and Pinecone for Retrieval Augmented Generation (RAG).

## Features

- ✅ Spring Boot 3.5+.
- ✅ Spring AI Integration
- ✅ Azure OpenAI ChatGPT 4.1
- ✅ Pinecone for RAG (Retrieval Augmented Generation)
- ✅ Thymeleaf Templates
- ✅ Ajax-based Chat Interface
- ✅ RESTful API

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Azure OpenAI account, with ChatGPT 4.1 deployment and text-embedding-3-large deployment, plus API credentials
- Perplexity.ai account, plus API credentials
- Pinecone account with index, plus API credentials

## Configuration

Configure your secret credentials as environment variables:

```bash
# Azure OpenAI
export AZURE_OPENAI_ENDPOINT=https://<your-endpoint>.openai.azure.com/
export AZURE_OPENAI_API_KEY=your-secret-key
export AZURE_OPENAI_DEPLOYMENT_NAME=gpt-4.1

# Perplexity
export PERPLEXITY_BASE_URL=https://api.perplexity.ai
export PERPLEXITY_API_KEY=your-secret-key
export PERPLEXITY_MODEL=sonar-pro

# Pinecone - Vector DB
export PINECONE_API_KEY=your-secret-key
export PINECONE_INDEX_NAME=your-pinecone-index

```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## License

This project is licensed under the terms of the MIT license (see LICENSE.md).
