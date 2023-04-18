package com.tim.datamigration.DB;

import java.util.ArrayList;

public class ProductArrayList {
    private static ArrayList<DocumentObject> addrs = new ArrayList<DocumentObject>();

    public static void addproduct(DocumentObject product) {
        addrs.add(product);
    }

    public static DocumentObject getProduct(int num) {
        return addrs.get(num);
    }

    public static int sizeOfProductArrayList() {
        return addrs.size();
    }
}
