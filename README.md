# Spring AI Demo

A Spring Boot 3.5.7 application demonstrating integration with Azure OpenAI ChatGPT 4.1 and Azure AI Search for Retrieval Augmented Generation (RAG).

## Features

- ✅ Spring Boot 3.5.7
- ✅ Spring AI Integration
- ✅ Azure OpenAI ChatGPT 4.1
- ✅ Azure AI Search for RAG (Retrieval Augmented Generation)
- ✅ Thymeleaf Templates
- ✅ Ajax-based Chat Interface
- ✅ RESTful API

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Azure OpenAI account and API credentials
- Azure AI Search instance

## Configuration

Configure your Azure credentials in `src/main/resources/application.properties` or set them as environment variables:

### Azure OpenAI Configuration

```properties
spring.ai.azure.openai.api-key=${AZURE_OPENAI_API_KEY}
spring.ai.azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT}
spring.ai.azure.openai.chat.options.deployment-name=${AZURE_OPENAI_DEPLOYMENT_NAME}
```

Or set environment variables:

```bash
export AZURE_OPENAI_API_KEY=your-api-key
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
export AZURE_OPENAI_DEPLOYMENT_NAME=gpt-4
```

### Azure AI Search Configuration

```properties
spring.ai.azure.store.search.api-key=${AZURE_AI_SEARCH_API_KEY}
spring.ai.azure.store.search.endpoint=${AZURE_AI_SEARCH_ENDPOINT}
spring.ai.azure.store.search.index-name=${AZURE_AI_SEARCH_INDEX_NAME}
```

Or set environment variables:

```bash
export AZURE_AI_SEARCH_API_KEY=your-search-key
export AZURE_AI_SEARCH_ENDPOINT=https://your-search-service.search.windows.net
export AZURE_AI_SEARCH_INDEX_NAME=documents
```

## Building the Application

```bash
mvn clean package
```

## Running the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/springaidemo-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Usage

### Web Interface

1. Open your browser and navigate to `http://localhost:8080`
2. Click "Go to Chat" to access the chat interface
3. Choose between:
   - **Standard Chat**: Direct conversation with Azure OpenAI
   - **Chat with RAG**: Enhanced responses using Azure AI Search for context retrieval

### API Endpoints

#### Standard Chat

```bash
POST /api/chat
Content-Type: application/json

{
  "message": "Your question here"
}
```

#### RAG-Enhanced Chat

```bash
POST /api/chat/rag
Content-Type: application/json

{
  "message": "Your question here"
}
```

#### Health Check

```bash
GET /api/chat/health
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/springaidemo/
│   │   ├── SpringAiDemoApplication.java    # Main application class
│   │   ├── controller/
│   │   │   ├── ChatRestController.java     # REST API endpoints
│   │   │   └── WebController.java          # Thymeleaf page controller
│   │   └── service/
│   │       └── ChatService.java            # AI service with RAG support
│   └── resources/
│       ├── application.properties           # Configuration
│       ├── static/
│       │   ├── css/style.css               # Styles
│       │   └── js/chat.js                  # Chat JavaScript
│       └── templates/
│           ├── index.html                  # Landing page
│           └── chat.html                   # Chat interface
└── test/
    └── java/com/example/springaidemo/
        └── SpringAiDemoApplicationTests.java
```

## Technologies Used

- **Spring Boot 3.5.7**: Application framework
- **Spring AI**: AI integration framework
- **Azure OpenAI**: ChatGPT 4.1 language model
- **Azure AI Search**: Vector store for RAG
- **Thymeleaf**: Server-side template engine
- **Maven**: Build tool

## License

This project is licensed under the terms included in the repository.
