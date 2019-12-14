package com.epita.tfidf.service.dependencies.tokenizer;

import com.epita.utils.annotation.Mutate;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DefaultTokenizer extends Tokenizer {
    @NotNull
    private List<String> stopWords;

    @NotNull
    private HashMap<String, String> synonyms;

    public DefaultTokenizer() {
        initStopWords();
        initSynonyms();
    }

    @Mutate
    private void initStopWords() {
        ClassLoader cl = getClass().getClassLoader();
        //File file = new File(cl.getResource("stopwords.txt").getFile());

        List<String> res = new ArrayList<>();

        try {
            InputStream is = cl.getResourceAsStream("stopwords.txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String line = buffer.readLine();
            res.add(line);
            while (line != null) {
                res.add(line);
                line = buffer.readLine();
            }
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopWords = res;
    }

    @Mutate
    private void initSynonyms() {
        ClassLoader cl = getClass().getClassLoader();
        //File file = new File(cl.getResource("synonyms.csv").getFile());

        HashMap<String, String> res = new HashMap<>();

        try {
            InputStream is = cl.getResourceAsStream("synonyms.csv");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String line = "";

            do {
                line = buffer.readLine();

                if (line == null)
                    break;

                List<String> actualSplit = Arrays.asList(line.split(", "));
                for (String word :
                        actualSplit) {
                    res.putIfAbsent(word, actualSplit.get(0));
                }
            } while (line != null);

            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        synonyms = res;
    }

    @Pure
    @Override
    public List<String> getStopWords() {
        return stopWords;
    }

    @Pure
    @Override
    public HashMap<String, String> getSynonyms() {
        return synonyms;
    }
}
