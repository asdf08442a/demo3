package com.enterprise.demo.service;

import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.UserLoginRO;
import com.enterprise.demo.dataobject.ro.UserRegisterRO;

import java.util.Map;

public interface UserService {

    // 管理员登录
    Boolean adminUserLogin(UserLoginRO userLoginRO);

    // 普通用户登录
    Map<String, Object> normalUserLogin(UserLoginRO userLoginRO);

    // 用户注册
    User normalUserRegister(UserRegisterRO userRegisterRO);
}
