package com.epita.tfidf.service.queryService;

import com.epita.tfidf.service.dependencies.cleaner.DefaultCleaner;
import com.epita.tfidf.service.dependencies.tokenizer.DefaultTokenizer;
import com.epita.tfidf.service.dependencies.vectorizer.DefaultVectorizer;


public class DefaultQueryService extends Query {

    public DefaultQueryService() {
        cleaner = new DefaultCleaner();
        tokeniser = new DefaultTokenizer();
        vectoriser = new DefaultVectorizer();
    }
}
