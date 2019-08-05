package com.example.demo.dao;

import com.example.demo.entity.ProductDefinition;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDAO extends MongoRepository<ProductDefinition, ObjectId> {
}
