package com.example.es.esdemo.controller;

import com.example.es.esdemo.dto.SearchRequest;
import com.example.es.esdemo.dto.SearchResponse;
import com.example.es.esdemo.services.ProductSearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanwp
 * @Date 2019/8/14 9:20
 */
@RestController
@RequestMapping("/db")
public class DubboController {

    @Reference(timeout = 3000)
    public ProductSearchService productSearchService;

    @GetMapping("/hi")
    public SearchResponse hi(){
        SearchRequest request = new SearchRequest();
        request.setKeyword("华为");
        SearchResponse searchResponse = productSearchService.fuzzySearch(request);
        return searchResponse;
    }
}
