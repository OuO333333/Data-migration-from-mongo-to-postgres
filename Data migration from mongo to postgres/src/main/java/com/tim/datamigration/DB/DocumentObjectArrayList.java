package com.tim.datamigration.DB;

import java.util.ArrayList;

/**
 * <Description>
 * ProductArrayList: insert DocumentObject into ProductArrayList
 */
public class DocumentObjectArrayList {
    private static ArrayList<DocumentObject> addrs = new ArrayList<DocumentObject>();

    /**
     * <Description>
     * add documentObject into DocumentObjectArrayList
     *
     * @param documentObject
     */
    public static void addDocumentObject(DocumentObject documentObject) {
        addrs.add(documentObject);
    }

    /**
     * <Description>
     * get DocumentObject from DocumentObjectArrayList
     *
     * @param num
     * @return DocumentObject
     */
    public static DocumentObject getDocumentObject(int num) {
        return addrs.get(num);
    }

    /**
     * <Description>
     * get size of DocumentObjectArrayList
     *
     * @return int
     */
    public static int sizeOfDocumentObjectArrayList() {
        return addrs.size();
    }
}
