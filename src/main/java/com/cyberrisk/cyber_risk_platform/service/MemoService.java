package com.cyberrisk.cyber_risk_platform.service;

import com.cyberrisk.cyber_risk_platform.model.Company;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MemoService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RiskScoringService riskScoringService;

    public MemoService(RiskScoringService riskScoringService) {
        this.riskScoringService = riskScoringService;
    }

    public String generateMemo(Company company) throws Exception {
        String riskLevel = riskScoringService.getRiskLevel(company.getRiskScore());

        String prompt = String.format(
            "You are a senior cyber insurance underwriter. Write a professional underwriting memo for the following company:\n\n" +
            "Company: %s\nIndustry: %s\nEmployees: %d\nCountry: %s\nRisk Score: %d/100 (%s RISK)\n\n" +
            "Security Profile:\n- Firewall: %s\n- Antivirus: %s\n- Data Encryption: %s\n" +
            "- Incident Response Plan: %s\n- Security Training: %s\n- Previous Breaches: %d\n\n" +
            "Write a 3-paragraph professional memo covering:\n" +
            "1. Executive summary of the risk assessment\n" +
            "2. Key security strengths and vulnerabilities found\n" +
            "3. Underwriting recommendation and any conditions",
            company.getName(),
            company.getIndustry(),
            company.getEmployeeCount(),
            company.getCountry(),
            company.getRiskScore(),
            riskLevel,
            company.getHasFirewall() ? "Yes" : "No",
            company.getHasAntiVirus() ? "Yes" : "No",
            company.getEncryptsData() ? "Yes" : "No",
            company.getHasIncidentResponsePlan() ? "Yes" : "No",
            company.getHasSecurityTraining() ? "Yes" : "No",
            company.getPreviousBreaches()
        );

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(
            mapper.createObjectNode()
                .put("model", "llama-3.3-70b-versatile")
                .put("max_tokens", 1000)
                .set("messages", mapper.createArrayNode()
                    .add(mapper.createObjectNode()
                        .put("role", "user")
                        .put("content", prompt)))
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());
        System.out.println("GROQ RESPONSE: " + response.body());
        return root.path("choices").get(0).path("message").path("content").asText();
    }
}