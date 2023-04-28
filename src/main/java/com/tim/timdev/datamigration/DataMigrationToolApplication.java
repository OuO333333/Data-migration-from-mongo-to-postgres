package com.tim.timdev.datamigration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tim.timdev.datamigration.mongofunction.MongoConnectionObj;
import com.tim.timdev.datamigration.postgresfunction.PostgresConnectionObj;
import com.tim.timdev.datamigration.struct.TableWithBinarydataObj;

@SpringBootApplication
public class DataMigrationToolApplication {

    /**
     * main
     *
     * @author Tim Lee
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // args[0] = mongoConnectionInfFilePath
        MongoConnectionObj mongoConnectionObj = new MongoConnectionObj(args[0]);
        mongoConnectionObj.iterateAllCollections();

        // args[1] = postgresConnectionInfFilePath
        PostgresConnectionObj postgresConnectionObj = new PostgresConnectionObj(args[1]);

        // set table with binary object
        TableWithBinarydataObj tableWithBinarydataObj1 = new TableWithBinarydataObj();
        String[] binaryDataColumns = { "str1" };
        tableWithBinarydataObj1.setTableName("collection1");
        tableWithBinarydataObj1.setBinaryDataColumns(binaryDataColumns);

        TableWithBinarydataObj tableWithBinarydataObj2 = new TableWithBinarydataObj();
        String[] binaryDataColumns2 = { "str1", "str2" };
        tableWithBinarydataObj2.setTableName("collection2");
        tableWithBinarydataObj2.setBinaryDataColumns(binaryDataColumns2);

        // add object to list
        List<TableWithBinarydataObj> tableWithBinarydataObjList = new ArrayList<>();
        tableWithBinarydataObjList.add(tableWithBinarydataObj1);
        tableWithBinarydataObjList.add(tableWithBinarydataObj2);

        // create all tables
        postgresConnectionObj.createAllTables(tableWithBinarydataObjList);

        // insert documents to table
        postgresConnectionObj.insertDocumentsToTable(tableWithBinarydataObjList);
    }

}
