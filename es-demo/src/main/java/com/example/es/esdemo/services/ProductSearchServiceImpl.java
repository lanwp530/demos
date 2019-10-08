package com.example.es.esdemo.services;


import com.example.es.esdemo.dto.SearchRequest;
import com.example.es.esdemo.dto.SearchResponse;
import com.example.es.esdemo.services.es.entity.ProductSearchModel;
import com.example.es.esdemo.services.es.repository.ProductRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 商品搜索服务类 对商品搜索接口API进行实现
 * 并对外提供商品搜索服务
 *
 * @author jin
 * @version v1.0.0
 * @Date 2019年8月10日
 */
@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public SearchResponse search(SearchRequest request) {
        SearchResponse response = new SearchResponse();
        try {
            request.requestCheck();
            Iterable<ProductSearchModel> elasticRes =
                    productRepository.search(QueryBuilders.termsQuery("title", request.getKeyword()));
            ArrayList<ProductSearchModel> productSearchModels = Lists.newArrayList(elasticRes);
            response.ok(productSearchModels);
        }catch (Exception e){
            e.printStackTrace();
            log.error("ProductSearchServiceImpl.search Occur Exception :" + e);
//            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public SearchResponse fuzzySearch(SearchRequest request) {
        SearchResponse response = new SearchResponse();
        try {
            request.requestCheck();
            Iterable<ProductSearchModel> elasticRes =
                    productRepository.search(QueryBuilders.fuzzyQuery("title", request.getKeyword()));
            ArrayList<ProductSearchModel> productSearchModels = Lists.newArrayList(elasticRes);
            response.ok(productSearchModels);
        }catch (Exception e){
            e.printStackTrace();
            log.error("ProductSearchServiceImpl.fuzzySearch Occur Exception :" + e);
//            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public SearchResponse hotProductKeyword() {
        return null;
    }
}
