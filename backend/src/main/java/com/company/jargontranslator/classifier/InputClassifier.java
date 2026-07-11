package com.company.jargontranslator.classifier;

import com.company.jargontranslator.model.InputType;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class InputClassifier {
    public InputType classify(String text) {
        String normalized = text.trim();
        int words = normalized.isBlank() ? 0 : normalized.split("\\s+").length;
        if (words == 1 && !normalized.matches(".*[.!?].*")) return InputType.WORD;
        if (words <= 5 && !normalized.matches(".*[.!?].*")) return InputType.PHRASE;
        return InputType.SENTENCE;
    }

    public List<String> extractJargonTerms(String text, InputType type) {
        if (type != InputType.SENTENCE) return List.of(text.trim());
        String lower = text.toLowerCase();
        String[] signals = {"circle back", "touch base", "move the needle", "low-hanging fruit", "bandwidth", "deep dive", "synergy", "take this offline"};
        List<LocatedTerm> matches = new ArrayList<>();
        for (String signal : signals) {
            int index = lower.indexOf(signal);
            if (index >= 0) matches.add(new LocatedTerm(signal, index));
        }
        matches.sort(java.util.Comparator.comparingInt(LocatedTerm::index));
        if (!matches.isEmpty()) return matches.stream().map(LocatedTerm::term).toList();
        String firstWord = text.trim().split("\\s+")[0].replaceAll("[^A-Za-z-]", "");
        return firstWord.isBlank() ? List.of(text.trim()) : List.of(firstWord);
    }
    public String lookupTerm(String text, InputType type) { return extractJargonTerms(text, type).getFirst(); }
    private record LocatedTerm(String term, int index) {}
}