package com.tim.datamigration.DB;

import org.bson.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <Description>
 * document object: id, document, collectionName
 */
@Entity
@Table(name = "to postgres")
public class DocumentObject {

    /**
     * <Description>
     * document object constructer
     */
    public DocumentObject() {
    }

    /**
     * <Description>
     * create and set DocumentObject
     *
     * @param id
     * @param document
     * @param collectionName
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
     * <Description>
     * get id
     *
     * @return String
     */
    public String getId() {
        return this.id;
    }

    /**
     * <Description>
     * set id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <Description>
     * get document
     *
     * @return Document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * <Description>
     * set document
     *
     * @param document
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * <Description>
     * get collectionName
     *
     * @return String
     */
    public String getCollectionName() {
        return this.collectionName;
    }

    /**
     *
     * <Description>
     * set collectionName
     *
     * @param collectionName
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

}
