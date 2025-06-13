package com.hazavao.std.endpoint.rest.controller.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@RestController
public class HazavaoController {

    @Value("${OPEN_API_KEY}")
    private String apiKey;

    @GetMapping("/hazavao")
    public String defineWord(@RequestParam String teny) {
        String prompt = "Hazavao amin'ny teny malagasy ny hevitry ny teny hoe: " + teny;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", prompt)
        );
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", request, Map.class
        );

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Tsy afaka namaly i ChatGPT. Mety misy olana.";
        }
    }
}
