package com.enterprise.demo.service;

import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.GetUserByPhoneRO;
import com.enterprise.demo.dataobject.ro.LoginRO;
import com.enterprise.demo.dataobject.ro.RegisterRO;

import java.util.Map;

public interface UserService {

    // 普通用户登录
    Map<String, Object> login(LoginRO loginRO);

    // 用户注册
    User register(RegisterRO registerRO);

    // 根据手机号查询用户
    User getByPhone(GetUserByPhoneRO getUserByPhoneRO);
}
