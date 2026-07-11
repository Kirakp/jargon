package com.company.jargontranslator.classifier;

import com.company.jargontranslator.model.InputType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputClassifierTest {
    private final InputClassifier classifier = new InputClassifier();
    @Test void classifiesInputs() {
        assertEquals(InputType.WORD, classifier.classify("synergy"));
        assertEquals(InputType.PHRASE, classifier.classify("circle back"));
        assertEquals(InputType.SENTENCE, classifier.classify("Let's circle back tomorrow."));
    }
}
