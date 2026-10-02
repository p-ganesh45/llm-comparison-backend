package com.example.llm_comparison.controller;

import com.example.llm_comparison.dto.ComparisonResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient geminiClient;
    private final ChatClient groqClient;

    public ChatController(
            GoogleGenAiChatModel geminiModel,
            OpenAiChatModel groqModel) {

        this.geminiClient = ChatClient.builder(geminiModel).build();
        this.groqClient = ChatClient.builder(groqModel).build();
    }

    @PostMapping("/gemini")
    public String askGemini(@RequestBody ChatRequest request) {

        return geminiClient
                .prompt()
                .user(request.message())
                .call()
                .content();
    }

    @PostMapping("/groq")
    public String askGroq(@RequestBody ChatRequest request) {

        return groqClient
                .prompt()
                .user(request.message())
                .call()
                .content();
    }

    @PostMapping("/compare")
    public ComparisonResponse compare(@RequestBody ChatRequest request) {

        // Gemini
        long geminiStart = System.currentTimeMillis();

        String geminiResponse = geminiClient
                .prompt()
                .user(request.message())
                .call()
                .content();

        long geminiTime = System.currentTimeMillis() - geminiStart;


        // Groq
        long groqStart = System.currentTimeMillis();

        String groqResponse = groqClient
                .prompt()
                .user(request.message())
                .call()
                .content();

        long groqTime = System.currentTimeMillis() - groqStart;


        return new ComparisonResponse(
                request.message(),
                geminiResponse,
                geminiTime,
                groqResponse,
                groqTime
        );
    }

    public record ChatRequest(String message) {
    }
}