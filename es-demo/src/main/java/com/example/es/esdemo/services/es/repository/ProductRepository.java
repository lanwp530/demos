package com.example.es.esdemo.services.es.repository;

import com.example.es.esdemo.services.es.entity.ProductSearchModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author lanwp
 */
public interface ProductRepository extends ElasticsearchRepository<ProductSearchModel, Integer> {
}
