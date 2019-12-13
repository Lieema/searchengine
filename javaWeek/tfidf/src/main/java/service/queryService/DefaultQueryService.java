package service.queryService;

import service.dependencies.cleaner.DefaultCleaner;
import service.dependencies.tokenizer.DefaultTokenizer;
import service.dependencies.vectorizer.DefaultVectorizer;


public class DefaultQueryService extends Query {

    public DefaultQueryService() {
        cleaner = new DefaultCleaner();
        tokeniser = new DefaultTokenizer();
        vectoriser = new DefaultVectorizer();
    }
}
