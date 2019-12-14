package com.epita.tfidf.service.indexService;

import com.epita.tfidf.service.dependencies.cleaner.DefaultCleaner;
import com.epita.tfidf.service.dependencies.tokenizer.DefaultTokenizer;
import com.epita.tfidf.service.dependencies.vectorizer.DefaultVectorizer;

public class DefaultIndexerService extends Indexer {

    public DefaultIndexerService() {
        cleaner = new DefaultCleaner();
        tokeniser = new DefaultTokenizer();
        vectoriser = new DefaultVectorizer();
    }
}
