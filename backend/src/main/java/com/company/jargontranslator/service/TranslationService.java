package com.company.jargontranslator.service;

import com.company.jargontranslator.classifier.InputClassifier;
import com.company.jargontranslator.model.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TranslationService {
    private final InputClassifier classifier; private final DictionaryLookupService dictionaries; private final ContextualTranslationService contextual; private final MessageSummaryService summaries;
    private final ConcurrentHashMap<String, Cached> cache = new ConcurrentHashMap<>();
    public TranslationService(InputClassifier classifier, DictionaryLookupService dictionaries, ContextualTranslationService contextual, MessageSummaryService summaries) { this.classifier = classifier; this.dictionaries = dictionaries; this.contextual = contextual; this.summaries = summaries; }
    public TranslationResponse translate(String input) {
        String text = input.trim(); Cached cached = cache.get(text.toLowerCase());
        if (cached != null && cached.created().plusSeconds(300).isAfter(Instant.now())) return withCached(cached.response(), true);
        InputType type = classifier.classify(text);
        List<JargonTranslation> translations = classifier.extractJargonTerms(text, type).stream().map(term -> {
            var results = dictionaries.lookup(term);
            var meaning = contextual.interpret(term, InputType.PHRASE, results);
            return new JargonTranslation(term, meaning.plainMeaning(), meaning.professionalExplanation(), meaning.suggestedReply(), results);
        }).toList();
        var response = buildResponse(text, type, translations);
        cache.put(text.toLowerCase(), new Cached(response, Instant.now())); return response;
    }
    private TranslationResponse buildResponse(String text, InputType type, List<JargonTranslation> translations) {
        JargonTranslation first = translations.getFirst(); String summary = summaries.summarize(text, type, translations);
        if (translations.size() == 1) return new TranslationResponse(text, type, summary, first.plainMeaning(), first.professionalExplanation(), first.suggestedReply(), first.dictionaryResults(), translations, false);
        List<DictionaryResult> allDefinitions = translations.stream().flatMap(item -> item.dictionaryResults().stream()).toList();
        return new TranslationResponse(text, type, summary, "This message contains " + translations.size() + " workplace-jargon expressions.", "Each expression is translated below in the order it appears in the message.", "Use the suggested replies selectively, based on the action you want to take.", allDefinitions, translations, false);
    }
    private TranslationResponse withCached(TranslationResponse r, boolean cached) { return new TranslationResponse(r.originalText(), r.inputType(), r.summary(), r.plainMeaning(), r.professionalExplanation(), r.suggestedReply(), r.dictionaryResults(), r.jargonTranslations(), cached); }
    private record Cached(TranslationResponse response, Instant created) {}
}