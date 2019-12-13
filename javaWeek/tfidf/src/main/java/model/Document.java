package model;

import java.util.List;

public class Document {
    public String link;
    public String html;
    public String text;
    public List<Token> tokens;

    @Override
    public String toString() {
        return "Document{" +
                "link='" + link + '\'' +
                '}';
    }
}
