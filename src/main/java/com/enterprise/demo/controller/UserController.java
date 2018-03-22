package com.enterprise.demo.controller;

import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.UserLoginRO;
import com.enterprise.demo.dataobject.ro.UserRegisterRO;
import com.enterprise.demo.dataobject.vo.ResultVO;
import com.enterprise.demo.enums.ResultEnum;
import com.enterprise.demo.service.UserService;
import com.enterprise.demo.utils.ROValidUtils;
import com.enterprise.demo.utils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户登录
     *
     * @param userLoginRO
     * @return
     */
    @PostMapping("/login")
    public ResultVO userLogin(@RequestBody UserLoginRO userLoginRO) {
        Map<String, Object> loginMap = userService.normalUserLogin(userLoginRO);
        if (loginMap.get("error") == null) {
            return ResultVOUtils.returnSuccess(loginMap);
        }
        return ResultVOUtils.returnFail((ResultEnum) loginMap.get("error"));
    }

    /**
     * 用户注册
     *
     * @param userRegisterRO
     * @return
     */
    @PostMapping("/register")
    public ResultVO userRegister(@Valid @RequestBody UserRegisterRO userRegisterRO, BindingResult result) {
        ROValidUtils.valid(result);
        User user = userService.normalUserRegister(userRegisterRO);
        if (user != null) {
            return ResultVOUtils.returnSuccess();
        } else {
            return ResultVOUtils.returnFail();
        }
    }

}
