package com.example.babysitterfinder.models;

import java.util.Collections;
import java.util.List;

public class TranslationRequest {
    private List<String> q;
    private String source;
    private String target;
    private String format;

    public TranslationRequest(String text, String source, String target) {
        this.q = Collections.singletonList(text);
        this.source = source;
        this.target = target;
        this.format = "text";
    }

    public List<String> getQ() {
        return q;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getFormat() {
        return format;
    }
}
