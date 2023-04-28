package com.tim.timdev.datamigration.struct;

/**
 * table with binary data object
 */
public class TableWithBinarydataObj {
    String tableName;
    String[] binaryDataColumns;

    /**
     * get table name
     *
     * @return String
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * set table name
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * get binaryDataColumns
     *
     * @return String[]
     */
    public String[] getBinaryDataColumns() {
        return binaryDataColumns;
    }

    /**
     * set binaryDataColumns
     *
     * @param binaryDataColumns
     */
    public void setBinaryDataColumns(String[] binaryDataColumns) {
        this.binaryDataColumns = binaryDataColumns;
    }

}
