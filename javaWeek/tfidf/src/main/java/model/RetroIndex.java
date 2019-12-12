package model;

import java.util.HashMap;
import java.util.List;

public class RetroIndex {
    private HashMap<String, List<Document>> indexHashMap;
    private Integer documentNumber = 0;

    public RetroIndex() {
        indexHashMap = new HashMap<>();
    }

    public Integer getDocumentNumber() {
        return documentNumber;
    }

    public void countDocument() {
        ++documentNumber;
    }

    public HashMap<String, List<Document>> getIndexHashMap() {
        return indexHashMap;
    }
}
