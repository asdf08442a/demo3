package com.enterprise.demo.utils;

import com.enterprise.demo.exception.ServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ROValidUtils {

    public static void valid(BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                throw new ServiceException(1, error.getDefaultMessage());
            }
        }
    }
}
