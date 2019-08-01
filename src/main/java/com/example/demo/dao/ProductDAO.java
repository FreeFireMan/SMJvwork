package com.example.demo.dao;

import com.example.demo.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDAO extends MongoRepository<Product, ObjectId> {
}
