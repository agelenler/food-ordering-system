package com.food.ordering.system.order.service.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

    @Bean("orderNoteInterpreterChatClient")
    @ConditionalOnProperty(prefix = "order.ai", name = "provider", havingValue = "openai", matchIfMissing = true)
    ChatClient openAIChatClient(OpenAiChatModel openAiChatModel, SimpleLoggerAdvisor simpleLoggerAdvisor) {
        return ChatClient.builder(openAiChatModel).defaultAdvisors(simpleLoggerAdvisor).build();
    }

    @Bean("orderNoteInterpreterChatClient")
    @ConditionalOnProperty(prefix = "order.ai", name = "provider", havingValue = "ollama")
    ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel, SimpleLoggerAdvisor simpleLoggerAdvisor) {
        return ChatClient.builder(ollamaChatModel).defaultAdvisors(simpleLoggerAdvisor).build();
    }

    @Bean
    SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }


}
