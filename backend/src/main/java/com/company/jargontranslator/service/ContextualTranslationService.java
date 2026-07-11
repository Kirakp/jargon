package com.company.jargontranslator.service;

import com.company.jargontranslator.model.DictionaryResult;
import com.company.jargontranslator.model.InputType;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContextualTranslationService {
    public Interpretation interpret(String input, InputType inputType, List<DictionaryResult> definitions) {
        String lower = input.toLowerCase();
        if (lower.contains("circle back")) return new Interpretation("Discuss this again later.", "The speaker is postponing the topic and wants to revisit it at a later time.", "Sure, I’ll follow up on this tomorrow.");
        if (lower.contains("touch base")) return new Interpretation("Briefly connect or check in.", "The speaker wants a short update or alignment conversation.", "Sounds good — I’m available to connect and share an update.");
        if (lower.contains("move the needle")) return new Interpretation("Make a meaningful, measurable difference.", "The speaker is asking for work that advances an important business goal.", "I’ll focus on the actions most likely to improve the outcome.");
        if (lower.contains("low-hanging fruit")) return new Interpretation("The easiest opportunities or quick wins.", "The speaker is suggesting starting with tasks that need little effort and offer clear value.", "I’ll identify the quickest high-impact opportunities first.");
        if (lower.contains("bandwidth")) return new Interpretation("Available time and capacity.", "The speaker is referring to whether someone can take on more work.", "I can take this on after I finish my current priority.");
        if (lower.contains("deep dive")) return new Interpretation("Examine something in detail.", "The speaker wants a thorough analysis rather than a quick overview.", "I’ll review the details and share a deeper analysis.");
        if (lower.contains("take this offline")) return new Interpretation("Discuss this separately from the current meeting.", "The speaker wants to avoid using the group’s meeting time for this topic.", "Agreed — I’ll set up a separate discussion.");
        if (!definitions.isEmpty()) {
            String definition = definitions.getFirst().definition();
            return new Interpretation(definition, "Dictionary definition from " + definitions.getFirst().provider() + ".", "Thanks — I understand. I’ll follow up if needed.");
        }
        return new Interpretation("The message needs more context to translate precisely.", "No suitable public dictionary definition was found, so this POC cannot safely infer a specific workplace meaning.", "Could you clarify the intended outcome or next step?");
    }
    public record Interpretation(String plainMeaning, String professionalExplanation, String suggestedReply) {}
}
