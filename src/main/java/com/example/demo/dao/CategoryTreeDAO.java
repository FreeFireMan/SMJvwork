package com.example.demo.dao;

import com.example.demo.entity.CategoryTree;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryTreeDAO extends MongoRepository<CategoryTree, ObjectId> {
}
