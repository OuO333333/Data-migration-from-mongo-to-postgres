package com.tim.timdev.datamigration.struct;

import org.bson.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * DocumentObject: String id, Document document, String collectionName
 */
@Entity
@Table(name = "to postgres")
public class DocumentObject {
    public DocumentObject() {
    }

    /**
     * set DocumentObject
     */
    public DocumentObject(String id, Document document, String collectionName) {
        this.id = id;
        this.document = document;
        this.collectionName = collectionName;
    }

    @Id
    private String id;
    @Convert(converter = JpaConverterJson.class)
    @Column(columnDefinition = "jsonb")
    private Document document;
    private String collectionName;

    /**
     * get id
     *
     * @return String
     */
    public String getId() {
        return this.id;
    }

    /**
     * set id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get document
     *
     * @return Document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * set document
     *
     * @param name
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * get collection name
     *
     * @return String
     */
    public String getCollectionName() {
        return this.collectionName;
    }

    /**
     * set collection name
     *
     * @param collectionNames
     */
    public void setCollectionName(String collectionNames) {
        this.collectionName = collectionNames;
    }

}
