package com.company.jargontranslator.service;

import com.company.jargontranslator.client.DictionaryProvider;
import com.company.jargontranslator.model.DictionaryResult;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictionaryLookupService {
    private final List<DictionaryProvider> providers;
    public DictionaryLookupService(List<DictionaryProvider> providers) { this.providers = providers; }
    public List<DictionaryResult> lookup(String term) {
        for (DictionaryProvider provider : providers) {
            var result = provider.lookup(term);
            if (result.isPresent()) return List.of(result.get());
        }
        return List.of();
    }
}
