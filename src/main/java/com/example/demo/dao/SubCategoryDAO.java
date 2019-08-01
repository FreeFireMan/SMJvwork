package com.example.demo.dao;

import com.example.demo.entity.SubCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCategoryDAO extends MongoRepository<SubCategory, ObjectId> {
}
