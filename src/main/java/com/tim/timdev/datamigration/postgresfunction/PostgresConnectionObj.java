package com.tim.timdev.datamigration.postgresfunction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tim.timdev.datamigration.fetchconnectioninf.ConnectionFunctionImplement;
import com.tim.timdev.datamigration.fetchconnectioninf.ConnectionInf;
import com.tim.timdev.datamigration.struct.DocumentObjectArrayList;
import com.tim.timdev.datamigration.struct.TableNamesArrayList;
import com.tim.timdev.datamigration.struct.TableWithBinarydataObj;

/**
 * PostgresConnectionObj
 */
public class PostgresConnectionObj {
    /**
     * create and set PostgresConnectionObj
     */
    public PostgresConnectionObj() {

        // set postgresConnectionInfFilePath
        String postgresConnectionInfFilePath = "put PostgresConnectionInf.json file path here";

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

    /**
     * create and set PostgresConnectionObj
     *
     * @param postgresConnectionInfFilePath
     */
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

    /**
     * get connection
     *
     * @return Connection
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * set connection
     *
     * @param postgresConnectionInf
     */
    public void setConn(ConnectionInf postgresConnectionInf) {
        try {
            String urlStr = "jdbc:postgresql://" + postgresConnectionInf.getHost() + ":"
                    + postgresConnectionInf.getPort() + "/" + postgresConnectionInf.getDbName()
                    + "?stringtype=unspecified";
            conn = DriverManager
                    .getConnection(urlStr,
                            postgresConnectionInf.getUsername(), postgresConnectionInf.getPassword());
            Logger logger = Logger.getLogger(
                    PostgresConnectionObj.class.getName());
            logger.log(Level.WARNING, "---Connect to Postgres database successfully---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create normal table
     *
     * @param tableName
     * @throws SQLException
     */
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
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                Logger logger = Logger.getLogger(
                        PostgresConnectionObj.class.getName());
                logger.log(Level.INFO, "---Table {0} created successfully---", tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null)
                    stmt.close();
            }
        }
    }

    /**
     * delete table
     *
     * @param tableName
     */
    public void deleteTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " +
                tableName;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            Logger logger = Logger.getLogger(
                    PostgresConnectionObj.class.getName());
            logger.log(Level.INFO, "---Table {0} deleted successfully---", tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * create all tables
     *
     * @throws SQLException
     */
    public void createAllTables() throws SQLException {
        // 建立所有table
        for (String i : TableNamesArrayList.getTableNamesSet()) {
            createTable(i);
        }
    }

    /**
     * create tables with binary data column and normal tables
     *
     * @param tableWithBinarydataObjList
     * @throws SQLException
     */
    public void createAllTables(List<TableWithBinarydataObj> tableWithBinarydataObjList) throws SQLException {
        // 建立所有table
        String[] strArr = new String[tableWithBinarydataObjList.size()];
        for (int i = 0; i < tableWithBinarydataObjList.size(); i++) {
            strArr[i] = tableWithBinarydataObjList.get(i).getTableName();
        }

        // create normal tables
        for (String i : TableNamesArrayList.getTableNamesSet()) {
            // check if table i exist in strArr
            if (!oneToMany(i, strArr))
                createTable(i);
        }

        // create special tables
        for (int i = 0; i < tableWithBinarydataObjList.size(); i++) {
            createTable(tableWithBinarydataObjList.get(i));
        }
    }

    /**
     * create tables with binary data column
     *
     * @param tableWithBinarydataObj
     */
    public void createTable(TableWithBinarydataObj tableWithBinarydataObj) {
        String tableName = tableWithBinarydataObj.getTableName();
        String[] binaryDataColumns = tableWithBinarydataObj.getBinaryDataColumns();

        // generate sql cmd
        String sql = "CREATE TABLE " +
                tableName +
                " (ID CHAR(100) PRIMARY KEY     NOT NULL," +
                " DOCUMENT           JSONB    NOT NULL";
        StringBuilder bld = new StringBuilder();
        bld.append(sql);
        for (int i = 0; i < binaryDataColumns.length; i++) {
            bld.append("," + binaryDataColumns[i] + "         BYTEA");
        }
        sql = bld.toString();
        sql += ")";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            Logger logger = Logger.getLogger(
                    PostgresConnectionObj.class.getName());
            logger.log(Level.INFO, "---Table {0} created successfully---", tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * insert documents with binary data into table
     *
     * @param tableWithBinarydataObjList
     * @throws SQLException
     */
    public void insertDocumentsToTable(List<TableWithBinarydataObj> tableWithBinarydataObjList) {
        // 將所有Documents插入已建好的table中
        // iterate all items
        for (int i = 0; i < DocumentObjectArrayList.sizeOfDocumentObjectArrayList(); i++) {

            // get itemDocument, itemCollectionName, binaryDataColumns from item
            // if no mateched table name, binaryDataColumns contains nothing
            Document itemDocument = DocumentObjectArrayList.getDocumentObject(i).getDocument();
            String itemCollectionName = DocumentObjectArrayList.getDocumentObject(i).getCollectionName();
            String[] binaryDataColumns = {};
            for (int j = 0; j < tableWithBinarydataObjList.size(); j++) {
                if (itemCollectionName.compareTo(tableWithBinarydataObjList.get(j).getTableName()) == 0) {
                    binaryDataColumns = tableWithBinarydataObjList.get(j).getBinaryDataColumns();
                }
            }

            // generate sql
            String sql = generateSqlWithBinaryData(itemCollectionName, binaryDataColumns);

            // setand execute preparedstatement
            setPreparedStatement(sql, itemDocument, binaryDataColumns);
        }
    }

    /**
     * setand execute preparedstatement
     *
     * @param sql
     * @param itemDocument
     * @param binaryDataColumns
     */
    public void setPreparedStatement(String sql, Document itemDocument, String[] binaryDataColumns) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ps == null)
            throw new NullPointerException("PreparedStatement is null.");

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
        String binaryDataString = "";
        for (int j = 0; j < binaryDataColumns.length; j++) {
            binaryDataString = itemDocument.getString(binaryDataColumns[j]);
            if (binaryDataString == null)
                binaryDataString = "";
            try {
                ps.setBytes(j + 3, binaryDataString.getBytes(StandardCharsets.ISO_8859_1));
            } catch (SQLException e) {
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
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * return true if str exists in strArr,
     * return false if str not exists in strArr
     *
     * @param str
     * @param strArr
     * @return boolean
     */
    public boolean oneToMany(String str, String... strArr) {
        for (String item : strArr) {
            if (str.compareTo(item) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * generate sql with binary data
     *
     * @param productCollectionName
     * @param columnsToRemove
     * @return String
     */
    public String generateSqlWithBinaryData(String productCollectionName, String[] columnsToRemove) {
        String sql = "INSERT INTO " +
                productCollectionName +
                " (ID,DOCUMENT";
        StringBuilder bld = new StringBuilder();
        bld.append(sql);
        for (int i = 0; i < columnsToRemove.length; i++) {
            bld.append("," + columnsToRemove[i]);
        }
        sql = bld.toString();
        sql += ") VALUES(?,?";
        StringBuilder bldd = new StringBuilder();
        bldd.append(sql);
        for (int i = 0; i < columnsToRemove.length; i++) {
            bldd.append(",?");
        }
        sql = bldd.toString();
        sql += ")";
        return sql;
    }
}
