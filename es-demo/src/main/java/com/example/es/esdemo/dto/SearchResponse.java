package com.example.es.esdemo.dto;


import com.example.es.esdemo.common.AbstractResponse;
import lombok.Data;

/**
 * 通用响应类
 *
 * @author jin
 * @version v1.0.0
 * @Date 2019年8月10日
 */
@Data
public class SearchResponse extends AbstractResponse {

    private Object data;

    public Object getData() {
        return data;
    }

    public SearchResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public static SearchResponse ok() {
        SearchResponse response = new SearchResponse();
//        response.setCode(SearchRetCode.SUCCESS.getCode());
//        response.setMsg(SearchRetCode.SUCCESS.getMsg());
        return response;
    }

    public SearchResponse ok(Object data) {
//        this.setCode(SearchRetCode.SUCCESS.getCode());
//        this.setMsg(SearchRetCode.SUCCESS.getMsg());
        return this;
    }

    public static SearchResponse err() {
        SearchResponse response = new SearchResponse();
//        response.setCode(SearchRetCode.FAILED.getCode());
        return response;
    }
}
