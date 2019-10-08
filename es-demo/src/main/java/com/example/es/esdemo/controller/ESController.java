package com.example.es.esdemo.controller;

import com.example.es.esdemo.services.es.entity.ProductSearchModel;
import com.example.es.esdemo.services.es.repository.ProductRepository;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author lanwp
 * @Date 2019/8/12 15:56
 */
@RestController
@RequestMapping("/es")
public class ESController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/hi")
    public String hi(String name){
        return "hello" + name;
    }


    @GetMapping("/search/fuzzy")
    public Object esSearch(String key) {
        System.out.println((QueryBuilders.fuzzyQuery("title", key)).toString());
        Iterable<ProductSearchModel> elasticRes =
                productRepository.search(QueryBuilders.fuzzyQuery("title", key));
        ArrayList<ProductSearchModel> response = Lists.newArrayList(elasticRes);
        return response;
    }

    @GetMapping("/search/terms")
    public Object esSearchTerms(String key) {
        // request.getKeyword()
        // 查询一个列多个值
        System.out.println((QueryBuilders.termsQuery("title", key)).toString());
        Iterable<ProductSearchModel> elasticRes =
                productRepository.search(QueryBuilders.termsQuery("title", key));
        ArrayList<ProductSearchModel> response = Lists.newArrayList(elasticRes);
        return response;
    }
}
