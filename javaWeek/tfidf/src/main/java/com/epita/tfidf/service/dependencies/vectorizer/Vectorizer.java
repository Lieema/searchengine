package com.epita.tfidf.service.dependencies.vectorizer;

import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.epita.utils.logger.Logger;
import com.epita.tfidf.model.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Vectorizer extends Logger {

    @Pure
    public List<Token> convert(@NotNull final List<String> tokens) {
        List<Token> res = new ArrayList<>();

        int index = 0;

        for (String str : tokens) {
            Boolean isAlreadyIn = false;

            for (Token actuToken : res) {
                if (actuToken.toString() != null && actuToken.toString().equals(str)) {
                    actuToken.positions.add(index);
                    isAlreadyIn = true;
                }
            }

            if (isAlreadyIn) {
                continue;
            }

            Token tok = new Token(str);
            tok.frequency = (double)Collections.frequency(tokens, str) / (double)tokens.size();

            //get position, add the tokenizer if not already in, otherwise add it's position to existing tokenizer
            tok.positions.add(index);

            res.add(tok);
            ++index;
        }

        return res;
    }
}
