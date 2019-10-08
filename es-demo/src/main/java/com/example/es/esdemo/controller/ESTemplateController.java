package com.example.es.esdemo.controller;

import com.example.es.esdemo.services.es.entity.ProductSearchModel;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lanwp
 * @Date 2019/8/13 12:30
 */
@RestController
@RequestMapping("/esTemplate")
public class ESTemplateController {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    private final static String INDEX_TB_ITEM = "tb_item";

    /**
     * 查询所有
     * @throws Exception
     */
    @GetMapping("/all")
    public List<Map<String, Object>> searchAll() throws Exception {
        //这一步是最关键的
//        elasticsearchTemplate.queryForPage()
        Client client = elasticsearchTemplate.getClient();
        // @Document(indexName = "product", type = "book")
//        SearchRequestBuilder srb = client.prepareSearch("product").setTypes("book");
        SearchRequestBuilder srb = client.prepareSearch(INDEX_TB_ITEM).setTypes("doc");
        SearchResponse sr = srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet(); // 查询所有
        SearchHits hits = sr.getHits();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : hits) {
//            Map<String, Object> source = hit.getSource();
            Map<String, Object> source = hit.getSourceAsMap(); // 新版本getSource()修改了
            list.add(source);
            System.out.println(hit.getSourceAsString());
        }
        return list;
    }

    @GetMapping("/all1")
    public List<Map<String, Object>> searchAll1() throws Exception {
        //这一步是最关键的
//        SearchQuery searchQuery = new NativeSearchQuery(QueryBuilder.)
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchQuery("name","gaolujie")).build();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("index_item").build();
        List<ProductSearchModel> productSearchModels = elasticsearchTemplate.queryForList(searchQuery, ProductSearchModel.class);
        System.out.println(productSearchModels);

        System.out.println("--------------------");
        Client client = elasticsearchTemplate.getClient();
        // @Document(indexName = "product", type = "book")
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_TB_ITEM);
        // QueryBuilders.matchAllQuery() {"query":{"match_all":{"boost":1.0}}}
//        SearchResponse sr = searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet(); // 查询所有
        SearchResponse sr = searchRequestBuilder.execute().actionGet(); // 查询所有
        System.out.println(sr.toString());
        SearchHits hits = sr.getHits();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap(); // 新版本getSource()修改了 Map<String, Object> source = hit.getSource();
            list.add(source);
            System.out.println(hit.getSourceAsString());
        }
        return list;
    }
}
