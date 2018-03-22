package com.enterprise.demo.exception;

import com.enterprise.demo.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private Integer code;

    public ServiceException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
