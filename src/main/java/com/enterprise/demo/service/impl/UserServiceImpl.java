package com.enterprise.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.GetUserByPhoneRO;
import com.enterprise.demo.dataobject.ro.LoginRO;
import com.enterprise.demo.dataobject.ro.RegisterRO;
import com.enterprise.demo.enums.ResultEnum;
import com.enterprise.demo.exception.ServiceException;
import com.enterprise.demo.repository.UserRepository;
import com.enterprise.demo.service.UserService;
import com.enterprise.demo.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> login(LoginRO loginRO) {
        User user = userRepository.findByUsernameAndPassword(loginRO.getUsername(), loginRO.getPassword());
        if (user == null) {
            throw new ServiceException(ResultEnum.ERROR_LOGIN);
        }
        String password = loginRO.getPassword();
        Map<String, Object> resMap = new HashMap<>();
        if (password.equals(user.getPassword())) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            String token = null;
            try {
                token = JWTUtils.createJWT(JSON.toJSONString(map));
            } catch (Exception e) {
                log.error("【JWT加密】JWT加密过程中出现错误,subject:{}", JSON.toJSONString(map));
                e.printStackTrace();
            }
            resMap.put("token", token);
            resMap.put("id", user.getId());
        } else {
            resMap.put("error", ResultEnum.AUTH_ERROR);
        }
        return resMap;
    }

    @Override
    public User register(RegisterRO registerRO) {
        User user = new User();
        BeanUtils.copyProperties(registerRO, user);
        return userRepository.save(user);
    }

    @Override
    @Cacheable(value = "user")
    public User getByPhone(GetUserByPhoneRO getUserByPhoneRO) {
        return userRepository.findOneByPhone(getUserByPhoneRO.getPhone());
    }
}
