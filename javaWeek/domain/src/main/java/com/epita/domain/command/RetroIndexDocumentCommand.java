package com.epita.domain.command;

import model.Document;

public class RetroIndexDocumentCommand implements Command {

    public Document document;

    public RetroIndexDocumentCommand(Document document) {
        this.document = document;
    }
}
