package com.epita.domain.command;

import model.Document;

public class IndexDocumentCommand implements Command {

    public Document document;

    public IndexDocumentCommand(Document document) {
        this.document = document;
    }
}
