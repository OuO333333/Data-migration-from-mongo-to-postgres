package com.tim.datamigration.DB;

public class TableWithBinarydataObj {
    String tableName;
    String[] binaryDataColumns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getBinaryDataColumns() {
        return binaryDataColumns;
    }

    public void setBinaryDataColumns(String[] binaryDataColumns) {
        this.binaryDataColumns = binaryDataColumns;
    }

}
