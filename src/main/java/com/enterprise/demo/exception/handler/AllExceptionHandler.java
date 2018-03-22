package com.enterprise.demo.exception.handler;


import com.enterprise.demo.dataobject.vo.ResultVO;
import com.enterprise.demo.enums.ResultEnum;
import com.enterprise.demo.exception.ServiceException;
import com.enterprise.demo.utils.ResultVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class AllExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO handlerException(HttpServletRequest request, Exception e) {
        log.error("【异常拦截】" + "[" + request.getRequestURI() + "]" + "接口出现错误," + e.getMessage());
        if (e instanceof ServiceException) { //业务异常 如账号密码错误
            return ResultVOUtils.returnFail(((ServiceException) e).getCode(), e.getMessage());
        } else if (e instanceof NoHandlerFoundException) { //404接口不存在
            return ResultVOUtils.returnFail(ResultEnum.NO_HANDLER.getCode(), ResultEnum.NO_HANDLER.getMessage());
        } else if (e instanceof ServletException) { //400接口报错
            return ResultVOUtils.returnFail(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
        } else { //500错误
            log.error("", e);
            return ResultVOUtils.returnFail(ResultEnum.SERVER_ERROR.getCode(), ResultEnum.SERVER_ERROR.getMessage());
        }
    }

}
