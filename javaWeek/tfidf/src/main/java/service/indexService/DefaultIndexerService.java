package service.indexService;

import service.dependencies.cleaner.DefaultCleaner;
import service.dependencies.tokenizer.DefaultTokenizer;
import service.dependencies.vectorizer.DefaultVectorizer;

public class DefaultIndexerService extends Indexer {

    public DefaultIndexerService() {
        cleaner = new DefaultCleaner();
        tokeniser = new DefaultTokenizer();
        vectoriser = new DefaultVectorizer();
    }
}
