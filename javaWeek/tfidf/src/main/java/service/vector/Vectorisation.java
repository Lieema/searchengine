package service.vector;

import model.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Vectorisation {
    public List<Token> convert(List<String> tokens) {
        List<Token> res = new ArrayList<>();

        int index = 0;

        for (String str :
                tokens) {
            Boolean isAlreadyIn = false;

            for (Token actuToken :
                    res) {
                if (actuToken.toString() != null && actuToken.toString().equals(str)) {
                    actuToken.getPositions().add(index);
                    isAlreadyIn = true;
                }
            }

            if (isAlreadyIn) {
                continue;
            }

            Token tok = new Token(str);
            tok.setFrequency((double)Collections.frequency(tokens, str) / (double)tokens.size());

            //get position, add the token if not already in, otherwise add it's position to existing token
            tok.getPositions().add(index);

            res.add(tok);
            ++index;
        }

        return res;
    }
}
