package com.company.jargontranslator.controller;

import com.company.jargontranslator.model.TranslationRequest;
import com.company.jargontranslator.model.TranslationResponse;
import com.company.jargontranslator.service.TranslationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/translations")
@CrossOrigin(origins = "http://localhost:5173")
public class TranslationController {
    private final TranslationService service;
    public TranslationController(TranslationService service) { this.service = service; }
    @PostMapping public ResponseEntity<TranslationResponse> translate(@Valid @RequestBody TranslationRequest request) { return ResponseEntity.ok(service.translate(request.text())); }
}
