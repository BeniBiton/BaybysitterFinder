package com.example.babysitterfinder.models;

import java.util.List;

public class TranslationResponse {
    public Data data;

    public static class Data {
        public List<Translation> translations;
    }

    public static class Translation {
        public String translatedText;
    }
}
