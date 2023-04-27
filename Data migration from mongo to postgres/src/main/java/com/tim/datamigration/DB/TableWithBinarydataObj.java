package com.tim.datamigration.DB;

/**
 * <Description>
 * TableWithBinarydataObj: table name, binary data columns,
 * binaryDataColumns = null if no binary data column
 */
public class TableWithBinarydataObj {
    String tableName;
    String[] binaryDataColumns;

    /**
     * <Description>
     * get table name
     *
     * @return String
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * <Description>
     * set table name
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * <Description>
     * get binary data columns
     *
     * @return String[]
     */
    public String[] getBinaryDataColumns() {
        return binaryDataColumns;
    }

    /**
     * <Description>
     * set binary data columns
     *
     * @param binaryDataColumns
     */
    public void setBinaryDataColumns(String[] binaryDataColumns) {
        this.binaryDataColumns = binaryDataColumns;
    }

}
