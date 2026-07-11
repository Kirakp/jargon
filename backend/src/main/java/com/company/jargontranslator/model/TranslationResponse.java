package com.company.jargontranslator.model;

import java.util.List;
public record TranslationResponse(String originalText, InputType inputType, String plainMeaning,
                                  String professionalExplanation, String suggestedReply,
                                  List<DictionaryResult> dictionaryResults,
                                  List<JargonTranslation> jargonTranslations,
                                  boolean cached) {}