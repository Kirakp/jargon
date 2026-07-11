package com.company.jargontranslator.client;

import com.company.jargontranslator.model.DictionaryResult;
import java.util.Optional;
public interface DictionaryProvider {
    Optional<DictionaryResult> lookup(String term);
    String getProviderName();
}
