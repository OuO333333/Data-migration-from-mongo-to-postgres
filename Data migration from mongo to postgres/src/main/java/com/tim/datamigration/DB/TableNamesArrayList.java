package com.tim.datamigration.DB;

import java.util.HashSet;

/**
 * <Description>
 * TableNamesArrayList: insert table names into TableNamesArrayList
 */
public class TableNamesArrayList {
    private static HashSet<String> tableNamesSet = new HashSet<String>();

    /**
     * <Description>
     * add table name
     *
     * @param tableName
     */
    public static void addTable(String tableName) {
        tableNamesSet.add(tableName);
    }

    /**
     * <Description>
     * get TableNamesArrayList
     *
     * @return HashSet<String>
     */
    public static HashSet<String> getTableNamesArrayList() {
        return tableNamesSet;
    }

    /**
     * <Description>
     * check if table name exists in TableNamesArrayList
     *
     * @param tableName
     * @return boolean
     */
    public static boolean containTableName(String tableName) {
        return tableNamesSet.contains(tableName);
    }

    /**
     * <Description>
     * get size of TableNamesArrayList
     *
     * @return int
     */
    public static int sizeOfTableNamesArrayList() {
        return tableNamesSet.size();
    }

}
