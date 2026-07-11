package com.company.jargontranslator.model;

import java.util.List;

public record JargonTranslation(
        String term,
        String plainMeaning,
        String professionalExplanation,
        String suggestedReply,
        List<DictionaryResult> dictionaryResults) {}