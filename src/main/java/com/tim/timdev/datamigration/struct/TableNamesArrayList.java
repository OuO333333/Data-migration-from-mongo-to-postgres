package com.tim.timdev.datamigration.struct;

import java.util.HashSet;
import java.util.Set;

/**
 * put table names in TableNamesArrayList
 */
public class TableNamesArrayList {
    private static HashSet<String> tableNamesSet = new HashSet<>();

    private TableNamesArrayList() {
        throw new UnsupportedOperationException();
    }

    /**
     * add table
     *
     * @param tableName
     */
    public static void addTable(String tableName) {
        tableNamesSet.add(tableName);
    }

    /**
     * get tableNamesSet
     *
     * @return HashSet<String>
     */
    public static Set<String> getTableNamesSet() {
        return tableNamesSet;
    }

    /**
     * check if table exists
     *
     * @param tableName
     * @return boolean
     */
    public static boolean containTableName(String tableName) {
        return tableNamesSet.contains(tableName);
    }

    /**
     * get size of tableNamesSet
     *
     * @return int
     */
    public static int sizeOfTableNamesArrayList() {
        return tableNamesSet.size();
    }

}
