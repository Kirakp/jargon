package com.company.jargontranslator.client;

import com.company.jargontranslator.model.DictionaryResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Optional;

@Component
public class FreeDictionaryProvider implements DictionaryProvider {
    private final RestClient client; private final String baseUrl;
    public FreeDictionaryProvider(RestClient client, @Value("${dictionary.free-url}") String baseUrl) { this.client = client; this.baseUrl = baseUrl; }
    public Optional<DictionaryResult> lookup(String term) {
        try {
            JsonNode root = client.get().uri(baseUrl + "{term}", term).retrieve().body(JsonNode.class);
            JsonNode definition = root == null ? null : root.at("/0/meanings/0/definitions/0/definition");
            return definition == null || definition.isMissingNode() ? Optional.empty() : Optional.of(new DictionaryResult(getProviderName(), term, definition.asText()));
        } catch (Exception ignored) { return Optional.empty(); }
    }
    public String getProviderName() { return "Free Dictionary API"; }
}
