package com.example.llm_comparison.dto;

public record ComparisonResponse(
        String question,
        String geminiResponse,
        long geminiTime,
        String groqResponse,
        long groqTime
) {
}