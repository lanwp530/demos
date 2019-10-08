package com.example.lwp.validatordemo.exception;

import com.example.lwp.validatordemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author lanwp
 * @Date 2019/9/29 1:05
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler {

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public Exception handleException(Exception ex) {
        System.out.println(11111111 + "----------");
        log.error("全局异常", ex);
        System.out.println(99999999 + "----------");
        return ex;
    }

    /**
     * 参数校验异常异常
     */
    @ExceptionHandler({BindException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(BindException ex) {
        log.error("参数校验异常11", ex);
        return getErrorMsg(ex.getBindingResult());
    }

    private Result getErrorMsg(BindingResult bindingResult){
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder errorMesssage = new StringBuilder();
        errorMesssage.append("校验失败:");
        for (int i = 0, errorSize = bindingResult.getFieldErrors().size(); i < errorSize; i++) {
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            errorMesssage.append(fieldError.getDefaultMessage());
            if (i + 1 == errorSize) {
                errorMesssage.append(".");
            } else {
                errorMesssage.append(";");
            }
//            errorMesssage.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage());
        }
        return new Result<String>("500", errorMesssage.toString(), null);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验异常22", ex);
        return getErrorMsg(ex.getBindingResult());
    }
}
