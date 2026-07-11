package com.company.jargontranslator.service;

import com.company.jargontranslator.model.InputType;
import com.company.jargontranslator.model.JargonTranslation;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageSummaryService {
    public String summarize(String text, InputType inputType, List<JargonTranslation> jargon) {
        if (jargon.isEmpty()) return "No specific workplace jargon was identified in this message.";
        String actions = jargon.stream()
                .map(item -> item.plainMeaning().replaceAll("[.]$", "").toLowerCase())
                .reduce((first, next) -> first + "; " + next)
                .orElse("no specific action was identified");
        if (inputType != InputType.SENTENCE) return "In short: " + actions + ".";
        return "In short, this message is asking for: " + actions + ".";
    }
}