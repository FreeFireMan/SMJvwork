package com.example.demo.dao;

import com.example.demo.entity.CategoryDefinition;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryDAO extends MongoRepository<CategoryDefinition, ObjectId> {
}
