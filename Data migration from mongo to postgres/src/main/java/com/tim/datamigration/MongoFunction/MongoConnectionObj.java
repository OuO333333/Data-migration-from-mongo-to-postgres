package com.tim.datamigration.MongoFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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
import com.tim.datamigration.DB.DocumentObject;
import com.tim.datamigration.DB.ProductArrayList;
import com.tim.datamigration.DB.TableNamesArrayList;
import com.tim.datamigration.FetchConnectionInf.ConnectionFunctionImplement;
import com.tim.datamigration.FetchConnectionInf.ConnectionInf;

public class MongoConnectionObj {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoTemplate mongoTemplate;

    public MongoConnectionObj() {
        String mongoConnectionInfFilePath = new String(
                "C:\\timgit\\data migration from mongo to postgres\\data migration from mongo to postgres\\src\\main\\java\\com\\tim\\datamigration\\FetchConnectionInf\\MongoConnectionInf.json");
        ConnectionFunctionImplement connectionFunctionImplement = new ConnectionFunctionImplement();
        ConnectionInf connectionInf = new ConnectionInf();
        try {
            connectionInf = connectionFunctionImplement.fetchConnectionInf(mongoConnectionInfFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        this.setMongoConnectionObj(connectionInf);
    }

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

    // set MongoConnectionObj
    // MongoClient, MongoDatabase, MongoTemplate
    public void setMongoConnectionObj(ConnectionInf mongoConnectionInf) {
        this.setMongoClient(mongoConnectionInf);
        this.setMongoDatabase(mongoConnectionInf.getDbName());
        this.setMongoTemplate(mongoConnectionInf.getDbName());
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    // set MongoClient
    public void setMongoClient(ConnectionInf mongoConnectionInf) {
        ServerAddress serverAddress = new ServerAddress(mongoConnectionInf.getHost(),
                Integer.parseInt(mongoConnectionInf.getPort()));
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress);
        // MongoCredential.createScramSha1Credential()三個參數分別為 用戶名 資料庫名稱 密碼
        String str = new String(mongoConnectionInf.getPassword());
        MongoCredential credential = MongoCredential.createScramSha1Credential(mongoConnectionInf.getUsername(),
                mongoConnectionInf.getDbName(),
                str.toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);
        // 獲取MongoDB連接
        mongoClient = new MongoClient(addrs, credential, MongoClientOptions.builder().build());
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public void setMongoDatabase(String dbName) {
        mongoDatabase = mongoClient.getDatabase(dbName);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(String dbName) {
        this.mongoTemplate = new MongoTemplate(this.mongoClient, dbName);
    }

    // create collection by collectionName
    public void createCollection(String collectionName) {
        try {
            this.mongoDatabase.createCollection(collectionName);
            System.out.println("---Collection " + collectionName + " create successfully---");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // iterate documents by collectionName(only one collection)
    public void iterateDocumentsByCollectionName(String collectionName) {
        try {
            // get collection
            MongoCollection<Document> collection = this.mongoDatabase.getCollection(collectionName);

            // start iterate
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                Document nextDocument = mongoCursor.next();
                DocumentObject product = new DocumentObject(nextDocument.get("_id").toString(), nextDocument, collectionName);
                // insert into ProductArrayList
                ProductArrayList.addproduct(product);
            }
        } catch (Exception e) {
        }
    }

    // iterate all collections
    public void iterateAllCollections() {
        // 遍歷資料庫中所有collection
        try {
            System.out.println("---Start iterateAllCollections---");
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
        }
    }
}
