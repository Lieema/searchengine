package com.epita.tfidf.model;

import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Nullable;
import com.epita.utils.annotation.Pure;

import java.util.List;

public class Document {

    @NotNull public String link;
    @Nullable public String html;
    @Nullable public String text;
    @NotNull public List<Token> tokens;

    @Pure
    @Override
    public String toString() {
        return "Document{" +
                "link='" + link + '\'' +
                '}';
    }
}
