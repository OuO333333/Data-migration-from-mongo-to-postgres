package com.tim.timdev.datamigration.mongofunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.tim.timdev.datamigration.fetchconnectioninf.ConnectionFunctionImplement;
import com.tim.timdev.datamigration.fetchconnectioninf.ConnectionInf;
import com.tim.timdev.datamigration.struct.DocumentObject;
import com.tim.timdev.datamigration.struct.DocumentObjectArrayList;
import com.tim.timdev.datamigration.struct.TableNamesArrayList;

/**
 * MongoConnectionObj
 */
public class MongoConnectionObj {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoTemplate mongoTemplate;

    /**
     * create and set MongoConnectionObj
     */
    public MongoConnectionObj() {
        String mongoConnectionInfFilePath = "put MongoConnectionInf.json file path here";
        ConnectionFunctionImplement connectionFunctionImplement = new ConnectionFunctionImplement();
        ConnectionInf connectionInf = new ConnectionInf();
        try {
            connectionInf = connectionFunctionImplement.fetchConnectionInf(mongoConnectionInfFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        this.setMongoConnectionObj(connectionInf);
    }

    /**
     * create and set MongoConnectionObj
     *
     * @param mongoConnectionInfFilePath
     */
    public MongoConnectionObj(String mongoConnectionInfFilePath) {
        // fetch ConnectionInf
        ConnectionFunctionImplement connectionFunctionImplement = new ConnectionFunctionImplement();
        ConnectionInf connectionInf = new ConnectionInf();
        try {
            connectionInf = connectionFunctionImplement.fetchConnectionInf(mongoConnectionInfFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // set MongoConnectionObj
        this.setMongoConnectionObj(connectionInf);
    }

    /**
     * set MongoConnectionObj:
     * MongoClient, MongoDatabase, MongoTemplate
     *
     * @param mongoConnectionInf
     */
    public void setMongoConnectionObj(ConnectionInf mongoConnectionInf) {
        this.setMongoClient(mongoConnectionInf);
        this.setMongoDatabase(mongoConnectionInf.getDbName());
        this.setMongoTemplate(mongoConnectionInf.getDbName());
    }

    /**
     * get mongoclient
     *
     * @return MongoClient
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * set mongoClient
     *
     * @param mongoConnectionInf
     */
    public void setMongoClient(ConnectionInf mongoConnectionInf) {
        ServerAddress serverAddress = new ServerAddress(mongoConnectionInf.getHost(),
                Integer.parseInt(mongoConnectionInf.getPort()));
        List<ServerAddress> addrs = new ArrayList<>();
        addrs.add(serverAddress);

        // MongoCredential.createScramSha1Credential()三個參數分別為 用戶名 資料庫名稱 密碼
        String str = mongoConnectionInf.getPassword();
        MongoCredential credential = MongoCredential.createScramSha1Credential(mongoConnectionInf.getUsername(),
                mongoConnectionInf.getDbName(),
                str.toCharArray());
        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(credential);

        // 獲取MongoDB連接
        mongoClient = new MongoClient(addrs, credential, MongoClientOptions.builder().build());
    }

    /**
     * get mongoDatabase
     *
     * @return MongoDatabase
     */
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    /**
     * set mongoDatabase
     *
     * @param dbName
     */
    public void setMongoDatabase(String dbName) {
        mongoDatabase = mongoClient.getDatabase(dbName);
    }

    /**
     * get monogoTemplate
     *
     * @return MongoTemplate
     */
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    /**
     * set monngoTemplate
     *
     * @param dbName
     */
    public void setMongoTemplate(String dbName) {
        this.mongoTemplate = new MongoTemplate(this.mongoClient, dbName);
    }

    /**
     * create collection by collectionName
     *
     * @param collectionName
     */
    public void createCollection(String collectionName) {
        try {
            this.mongoDatabase.createCollection(collectionName);

            // Create a Logger
            Logger logger = Logger.getLogger(
                    MongoConnectionObj.class.getName());
            logger.log(Level.INFO, "---Collection {0} create successfully---", collectionName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * iterate documents by collectionName(only one collection)
     *
     * @param collectionName
     */
    public void iterateDocumentsByCollectionName(String collectionName) {
        try {

            // get collection
            MongoCollection<Document> collection = this.mongoDatabase.getCollection(collectionName);

            // start iterate
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                Document nextDocument = mongoCursor.next();
                DocumentObject product = new DocumentObject(nextDocument.get("_id").toString(), nextDocument,
                        collectionName);

                // insert into ProductArrayList
                DocumentObjectArrayList.addDocumentObject(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * iterate all collections
     */
    public void iterateAllCollections() {

        // 遍歷資料庫中所有collection
        try {
            Logger logger = Logger.getLogger(
                    MongoConnectionObj.class.getName());
            logger.log(Level.INFO, "---Start iterateAllCollections---");
            MongoIterable<String> findIterable = this.mongoDatabase.listCollectionNames();
            for (String collectionname : findIterable) {

                // iterate documents by collectionName(only one collection)
                iterateDocumentsByCollectionName(collectionname);

                // collectionnames存進TableNamesArrayList中
                if (!TableNamesArrayList.containTableName(collectionname)) {
                    TableNamesArrayList.addTable(collectionname);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
