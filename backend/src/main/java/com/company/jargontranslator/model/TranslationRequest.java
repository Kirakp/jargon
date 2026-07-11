package com.company.jargontranslator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslationRequest(@NotBlank @Size(max = 5000) String text) {}
