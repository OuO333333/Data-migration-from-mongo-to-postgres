package com.tim.datamigration.DB;

import org.bson.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "to postgres")
public class DocumentObject {
    public DocumentObject() {
    }

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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document name) {
        this.document = name;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public void setCollectionName(String collectionNames) {
        this.collectionName = collectionNames;
    }

}
