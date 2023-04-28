package com.tim.timdev.datamigration.struct;

import java.util.ArrayList;

/**
 * put DocumentObject in DocumentObjectArrayList
 */
public class DocumentObjectArrayList {
    private static ArrayList<DocumentObject> addrs = new ArrayList<>();

    private DocumentObjectArrayList() {
        throw new UnsupportedOperationException();
    }

    /**
     * add DocumentObject
     *
     * @param documentObject
     */
    public static void addDocumentObject(DocumentObject documentObject) {
        addrs.add(documentObject);
    }

    /**
     * get DocumentObject
     *
     * @param num
     * @return DocumentObject
     */
    public static DocumentObject getDocumentObject(int num) {
        return addrs.get(num);
    }

    /**
     * get size of DocumentObjectArrayList
     *
     * @return int
     */
    public static int sizeOfDocumentObjectArrayList() {
        return addrs.size();
    }
}
