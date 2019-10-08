package com.example.es.esdemo.dto;

import com.example.es.esdemo.common.AbstractRequest;
import lombok.Data;

/**
 * 通用请求类
 *
 * @author jin
 * @version v1.0.0
 * @Date 2019年8月10日
 */
@Data
public class SearchRequest extends AbstractRequest {

    private String keyword;
    private Integer currentPage;
    private Integer pageSize;

    @Override
    public void requestCheck() {

    }
}
