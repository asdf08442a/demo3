package com.enterprise.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.UserLoginRO;
import com.enterprise.demo.dataobject.ro.UserRegisterRO;
import com.enterprise.demo.enums.ResultEnum;
import com.enterprise.demo.exception.ServiceException;
import com.enterprise.demo.repository.UserRepository;
import com.enterprise.demo.service.UserService;
import com.enterprise.demo.utils.BCrypt;
import com.enterprise.demo.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Boolean adminUserLogin(UserLoginRO userLoginRO) {
        User user = userRepository.findByUsernameAndPasswordAndRole(userLoginRO.getUsername(), userLoginRO.getPassword(), 0);
        if (user == null) {
            throw new ServiceException(ResultEnum.ERROR_LOGIN);
        }
        return true;
    }

    @Override
    public Map<String, Object> normalUserLogin(UserLoginRO userLoginRO) {
        User user = userRepository.findByUsernameAndRole(userLoginRO.getUsername(), 1);
        if (user == null) {
            throw new ServiceException(ResultEnum.ERROR_LOGIN);
        }
        String password = userLoginRO.getPassword();
        String hashPassword = user.getPassword();
        String salt = hashPassword.substring(0, 29);
        String hashed = BCrypt.hashpw(password, salt);
        Map<String, Object> resMap = new HashMap<>();
        if (hashed.equals(hashPassword)) {
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
    public User normalUserRegister(UserRegisterRO userRegisterRO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterRO, user);
        String salt = BCrypt.gensalt(); //生成29位salt字符串
        String hashed = BCrypt.hashpw(userRegisterRO.getPassword(), salt);
        user.setPassword(hashed);
        return userRepository.save(user);
    }
}
