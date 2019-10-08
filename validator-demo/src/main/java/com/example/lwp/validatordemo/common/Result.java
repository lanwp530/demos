package com.example.lwp.validatordemo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lanwp
 * @Date 2019/9/29 1:32
 */
@Data
@AllArgsConstructor
public class Result<T> {
    private String code;
    private String msg;
    private T data;
}
