package com.enterprise.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.enterprise.demo.enums.ResultEnum;
import com.enterprise.demo.exception.ServiceException;
import com.enterprise.demo.utils.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class AuthorizeAspect {

    private static final String tokenName = "Authorization";
    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件

    /**
     * 拦截接口除了登录/注册接口
     */
    @Pointcut("execution(public * com.enterprise.demo.controller.*.*(..))" +
            "&& !execution(public * com.enterprise.demo.controller.UserController.login(..))" +
            "&& !execution(public * com.enterprise.demo.controller.UserController.register(..))")
    public void verifyAuthorize() {
    }

    @Before("verifyAuthorize()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.setAttribute("start_time", System.currentTimeMillis());
        if (!"dev".equals(env)) { //开发环境不进行auth认证
            try {
                if (JWTUtils.parseJWT(request.getHeader(tokenName)) == null) {
                    log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));
                    throw new ServiceException(ResultEnum.AUTH_ERROR);
                }
            } catch (IllegalArgumentException e) { //说明没有传Header-Authorization
                throw new ServiceException(403, "没有认证");
            } catch (SignatureException e) {
                throw new ServiceException(403, "token有误");
            } catch (ExpiredJwtException e) {
                throw new ServiceException(403, "认证失效已过期，请重新登录");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(ResultEnum.AUTH_ERROR);
            }
        }
    }

    @After("verifyAuthorize()")
    public void afterVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = (long) request.getAttribute("start_time");
        request.removeAttribute("start_time");
        long endTime = System.currentTimeMillis();
        log.info("================ 请求接口[{}]{}，处理时间：{}ms", request.getMethod(), request.getRequestURI(), endTime - startTime);
    }


    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}
