package com.tim.datamigration.DB;

import java.util.HashSet;

public class TableNamesArrayList {
    private static HashSet<String> tableNamesSet = new HashSet<String>();

    public static void addTable(String tableName) {
        tableNamesSet.add(tableName);
    }

    public static HashSet<String> getHashSet() {
        return tableNamesSet;
    }

    public static boolean containTableName(String tableName) {
        return tableNamesSet.contains(tableName);
    }

    public static int sizeOfTableNamesArrayList() {
        return tableNamesSet.size();
    }

}
