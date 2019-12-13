package service.dependencies.tokenizer;

import annotation.NotNull;
import annotation.Pure;
import logger.Logger;

import java.util.*;

public abstract class Tokenizer extends Logger {
    public abstract List<String> getStopWords();
    public abstract HashMap<String, String> getSynonyms();

    @Pure
    public List<String> getTokens(@NotNull final String text) {
        List<String> res = new ArrayList<>();
        List<String> sliced = Arrays.asList(text.split(" "));

        for (String token : sliced) {
            //Keep only word characters
            String tempToken = token.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

            //If stop words, don't save
            if (getStopWords().contains(tempToken)) {
                continue;
            }

            //Apply stemming
            tempToken = applyStemming(tempToken);

            //Get synonym and verify if null
            String tempSynonym = getSynonyms().get(tempToken);
            if (tempSynonym != null)
                tempToken = tempSynonym;

            res.add(tempToken);
        }

        return res;
    }

    @Pure
    public String applyStemming(final String word) {
        return word.replaceAll("(ing$)|(s$)", "");
    }
}
