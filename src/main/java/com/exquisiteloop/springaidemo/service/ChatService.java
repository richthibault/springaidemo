package com.exquisiteloop.springaidemo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;

import com.exquisiteloop.springaidemo.model.ChatRequest;
import com.exquisiteloop.springaidemo.util.MarkdownUtils;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	private final OpenAiChatModel perplexityChatModel;
	private final AzureOpenAiChatModel azureChatModel;

    private final VectorStore vectorStore;

	private final ChatMemory chatMemory = MessageWindowChatMemory.builder()
		.maxMessages(20)
		.build();

    public ChatService(OpenAiChatModel perplexityChatModel, AzureOpenAiChatModel azureChatModel,
			VectorStore vectorStore) {
        this.perplexityChatModel = perplexityChatModel;
        this.azureChatModel = azureChatModel;
		this.vectorStore = vectorStore;
	}

	private ChatClient getChatClient(ChatRequest chatRequest) {

		ChatModel chatModel = switch (chatRequest.platform()) {
			case PERPLEXITY -> perplexityChatModel;
			default -> azureChatModel;
		};

		List<Advisor> advisors = new ArrayList<>();
		advisors.add(new SimpleLoggerAdvisor());
		advisors.add(MessageChatMemoryAdvisor.builder(chatMemory).build());

		if(chatRequest.ragCompanyId() != null && !chatRequest.ragCompanyId().isEmpty()) {

			Filter.Expression filterExpression = new Filter.Expression(
					Filter.ExpressionType.EQ,
					new Filter.Key("company_id"),
					new Filter.Value(chatRequest.ragCompanyId()));

			SearchRequest searchRequest = SearchRequest.builder()
					.filterExpression(filterExpression)
					.build();

			advisors.add(QuestionAnswerAdvisor.builder(vectorStore).searchRequest(searchRequest).build());

		}

		ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
		chatClientBuilder.defaultAdvisors(advisors);
		return chatClientBuilder.build();
	}

    /**
     * Generate a chat response from LLM
     */
    public String chat(ChatRequest chatRequest) {
        logger.debug("Processing chat message: {}", chatRequest.message());

		ChatClient chatClient = getChatClient(chatRequest);
        
        try {

			CallResponseSpec response = chatClient.prompt()
                    .user(chatRequest.message())
                    .call();

			String outMessage = MarkdownUtils.markdownToHtml(response.content());

            logger.debug("Generated response: {}", outMessage);
            return outMessage;
        } catch (Exception e) {
            logger.error("Error generating chat response", e);
            return "Sorry, I encountered an error processing your request: " + e.getMessage();
        }
    }

}
