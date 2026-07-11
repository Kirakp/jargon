package com.company.jargontranslator.client;

import com.company.jargontranslator.model.DictionaryResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Component
public class WiktionaryProvider implements DictionaryProvider {
    private final RestClient client; private final String baseUrl;
    public WiktionaryProvider(RestClient client, @Value("${dictionary.wiktionary-url}") String baseUrl) { this.client = client; this.baseUrl = baseUrl; }
    public Optional<DictionaryResult> lookup(String term) {
        try {
            JsonNode root = client.get().uri(baseUrl + "{term}", term.replace(" ", "_")).retrieve().body(JsonNode.class);
            if (root == null) return Optional.empty();
            Iterator<Map.Entry<String, JsonNode>> languages = root.fields();
            while (languages.hasNext()) {
                JsonNode definitions = languages.next().getValue();
                if (definitions.isArray() && !definitions.isEmpty()) {
                    JsonNode gloss = definitions.get(0).path("definitions").path(0).path("definition");
                    if (!gloss.isMissingNode()) return Optional.of(new DictionaryResult(getProviderName(), term, gloss.asText().replaceAll("<[^>]+>", "")));
                }
            }
        } catch (Exception ignored) { }
        return Optional.empty();
    }
    public String getProviderName() { return "Wiktionary"; }
}
