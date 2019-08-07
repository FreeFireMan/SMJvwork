package com.example.demo.service.catalog;

import com.example.demo.db.model.CatalogNode;
import com.example.demo.db.model.CategoryNode;
import com.example.demo.db.repository.CatalogRepository;
import com.example.demo.service.fetch.FetchService;
import com.example.demo.utils.OptionalUtils;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private FetchService fetchService;

    public void fetchAndUpdate() {
        Optional<CategoryNode> node = fetchService.fetchCatalog("70037");
        node.ifPresent(catalogRepository::save);
    }

    public Optional<CatalogNode> get() {
        Page<CatalogNode> nodes = catalogRepository.findAll(PageRequest.of(0, 1));
        return OptionalUtils.<CatalogNode>head(nodes);
    }
}
