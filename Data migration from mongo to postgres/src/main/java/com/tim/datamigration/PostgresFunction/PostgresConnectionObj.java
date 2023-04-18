package com.tim.datamigration.PostgresFunction;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.bson.Document;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tim.datamigration.DB.ProductArrayList;
import com.tim.datamigration.DB.TableNamesArrayList;
import com.tim.datamigration.DB.TableWithBinarydataObj;
import com.tim.datamigration.FetchConnectionInf.ConnectionFunctionImplement;
import com.tim.datamigration.FetchConnectionInf.ConnectionInf;

public class PostgresConnectionObj {
    public PostgresConnectionObj() {
        // set postgresConnectionInfFilePath
        String postgresConnectionInfFilePath = new String(
                "C:\\timgit\\data migration from mongo to postgres\\data migration from mongo to postgres\\src\\main\\java\\com\\tim\\datamigration\\FetchConnectionInf\\PostgresConnectionInf.json");
        // fetch ConnectionInf
        ConnectionFunctionImplement connectionFunctionImplement = new ConnectionFunctionImplement();
        ConnectionInf connectionInf = new ConnectionInf();
        try {
            connectionInf = connectionFunctionImplement.fetchConnectionInf(postgresConnectionInfFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        // set conn
        this.setConn(connectionInf);
    }

    public PostgresConnectionObj(String postgresConnectionInfFilePath) {
        // fetch ConnectionInf
        ConnectionFunctionImplement connectionFunctionImplement = new ConnectionFunctionImplement();
        ConnectionInf connectionInf = new ConnectionInf();
        try {
            connectionInf = connectionFunctionImplement.fetchConnectionInf(postgresConnectionInfFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        // set conn
        this.setConn(connectionInf);
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    // set conn
    public void setConn(ConnectionInf postgresConnectionInf) {
        try {
            String urlStr = "jdbc:postgresql://" + postgresConnectionInf.getHost() + ":"
                    + postgresConnectionInf.getPort() + "/" + postgresConnectionInf.getDbName()
                    + "?stringtype=unspecified";
            conn = DriverManager
                    .getConnection(urlStr,
                            postgresConnectionInf.getUsername(), postgresConnectionInf.getPassword());
            System.out.println("---Connect to Postgres database successfully---");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // create normal table
    public void createTable(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = this.getConn()
                .getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, tableName,
                new String[] { "TABLE" });
        if (!resultSet.next()) {
            String sql = "CREATE TABLE " +
                    tableName +
                    " (ID CHAR(100) PRIMARY KEY     NOT NULL," +
                    " DOCUMENT           JSONB    NOT NULL)";
            try {
                Statement stmt = null;
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("---Table " + tableName + " created successfully---");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // delete table
    public void deleteTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " +
                tableName;
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("---Table " + tableName + " deleted successfully---");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // create all tables
    public void createAllTables() throws SQLException {
        // 建立所有table
        for (String i : TableNamesArrayList.getHashSet()) {
            createTable(i);
        }
    }

    // create tables with binary data column and normal tables
    public void createAllTables(List<TableWithBinarydataObj> tableWithBinarydataObjList) throws SQLException {
        // 建立所有table
        String[] strArr = new String[tableWithBinarydataObjList.size()];
        for (int i = 0; i < tableWithBinarydataObjList.size(); i++) {
            strArr[i] = tableWithBinarydataObjList.get(i).getTableName();
        }
        // create normal tables
        for (String i : TableNamesArrayList.getHashSet()) {
            // check if table i exist in strArr
            if (oneToMany(i, strArr) == false)
                createTable(i);
        }
        // create special tables
        for (int i = 0; i < tableWithBinarydataObjList.size(); i++) {
            createTable(tableWithBinarydataObjList.get(i));
        }
    }

    // create tables with binary data column
    public void createTable(TableWithBinarydataObj tableWithBinarydataObj) {
        String tableName = tableWithBinarydataObj.getTableName();
        String[] binaryDataColumns = tableWithBinarydataObj.getBinaryDataColumns();
        // generate sql cmd
        String sql = "CREATE TABLE " +
                tableName +
                " (ID CHAR(100) PRIMARY KEY     NOT NULL," +
                " DOCUMENT           JSONB    NOT NULL";
        for (int i = 0; i < binaryDataColumns.length; i++) {
            sql += "," + binaryDataColumns[i] + "         BYTEA";
        }
        sql += ")";
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("---Table " + tableName + " created successfully---");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert documents with binary data into table
    public void insertDocumentsToTable(List<TableWithBinarydataObj> tableWithBinarydataObjList) {
        // 將所有Documents插入已建好的table中
        // iterate all items
        for (int i = 0; i < ProductArrayList.sizeOfProductArrayList(); i++) {
            // get itemDocument, itemCollectionName, binaryDataColumns from item
            // if no mateched table name, binaryDataColumns = {}
            Document itemDocument = ProductArrayList.getProduct(i).getDocument();
            String itemCollectionName = ProductArrayList.getProduct(i).getCollectionName();
            String[] binaryDataColumns = {};
            for (int j = 0; j < tableWithBinarydataObjList.size(); j++) {
                if (itemCollectionName.compareTo(tableWithBinarydataObjList.get(j).getTableName()) == 0) {
                    binaryDataColumns = tableWithBinarydataObjList.get(j).getBinaryDataColumns();
                }
            }
            // generate sql
            String sql = generateSqlWithBinaryData(itemCollectionName, binaryDataColumns);
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // set id
            try {
                ps.setString(1, itemDocument.get("_id").toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // change from _id to id
            itemDocument.append("id", itemDocument.get("_id").toString());
            itemDocument.remove("_id");
            // set binary data
            String binaryDataString = new String("");
            for (int j = 0; j < binaryDataColumns.length; j++) {
                binaryDataString = itemDocument.getString(binaryDataColumns[j]);
                if (binaryDataString == null)
                    binaryDataString = "";
                try {
                    ps.setBytes(j + 3, binaryDataString.getBytes("ISO8859-1"));
                } catch (UnsupportedEncodingException | SQLException e) {
                    e.printStackTrace();
                }
            }
            // set jsonb string
            for (int j = 0; j < binaryDataColumns.length; j++) {
                itemDocument.remove(binaryDataColumns[j]);
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonbString = gson.toJson(itemDocument);
            try {
                ps.setString(2, jsonbString);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // execute sql
            try {
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean oneToMany(String str, String... strArr) {
        for (String item : strArr) {
            if (str.compareTo(item) == 0) {
                return true;
            }
        }
        return false;
    }

    public String generateSqlWithBinaryData(String productCollectionName, String[] columnsToRemove) {
        String sql = "INSERT INTO " +
                productCollectionName +
                " (ID,DOCUMENT";
        for (int i = 0; i < columnsToRemove.length; i++) {
            sql += "," + columnsToRemove[i];
        }
        sql += ") VALUES(?,?";
        for (int i = 0; i < columnsToRemove.length; i++) {
            sql += ",?";
        }
        sql += ")";
        return sql;
    }
}
