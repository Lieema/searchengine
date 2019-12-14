package com.epita.domain.command;

public class CrawlUrlCommand implements Command {

    public String url;

    public CrawlUrlCommand(String url) {
        this.url = url;
    }
}
