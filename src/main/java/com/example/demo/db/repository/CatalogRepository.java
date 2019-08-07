package com.example.demo.db.repository;

import com.example.demo.db.model.CatalogNode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogRepository extends MongoRepository<CatalogNode, ObjectId> {}