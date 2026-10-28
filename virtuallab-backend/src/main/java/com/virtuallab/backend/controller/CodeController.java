
package com.virtuallab.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174", "http://127.0.0.1:5174"})
public class CodeController {

    private final RestClient restClient = RestClient.create();

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runCode(
            @RequestBody Map<String, Object> request) {

        try {
            Object languageId = request.get("languageId");
            Object sourceCode = request.get("sourceCode");
            Object stdin = request.get("stdin");

            if (languageId == null || sourceCode == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("message", "languageId and sourceCode are required");
                return ResponseEntity.badRequest().body(error);
            }

            Map<String, Object> submission = new HashMap<>();
            submission.put("language_id", languageId);
            submission.put("source_code", sourceCode);
            submission.put("stdin", stdin == null ? "" : stdin);

            Map<?, ?> result = restClient.post()
                    .uri("https://ce.judge0.com/submissions?base64_encoded=false&wait=true")
                    .header("Content-Type", "application/json")
                    .body(submission)
                    .retrieve()
                    .body(Map.class);

            if (result == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("message", "Judge0 returned an empty response");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
            }

            Map<String, Object> output = new HashMap<>();
            output.put("output", result.get("stdout"));
            output.put("stderr", result.get("stderr"));
            output.put("compileOutput", result.get("compile_output"));
            output.put("message", result.get("message"));
            output.put("status", result.get("status"));

            return ResponseEntity.ok(output);

        } catch (RestClientException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Code execution service error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
