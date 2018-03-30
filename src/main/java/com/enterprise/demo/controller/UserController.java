package com.enterprise.demo.controller;

import com.enterprise.demo.dataobject.model.User;
import com.enterprise.demo.dataobject.ro.GetUserByPhoneRO;
import com.enterprise.demo.dataobject.ro.LoginRO;
import com.enterprise.demo.dataobject.ro.RegisterRO;
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
     * @param loginRO
     * @return
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginRO loginRO) {
        Map<String, Object> loginMap = userService.login(loginRO);
        if (loginMap.get("error") == null) {
            return ResultVOUtils.returnSuccess(loginMap);
        }
        return ResultVOUtils.returnFail((ResultEnum) loginMap.get("error"));
    }

    /**
     * 用户注册
     *
     * @param registerRO
     * @return
     */
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody RegisterRO registerRO, BindingResult result) {
        ROValidUtils.valid(result);
        User user = userService.register(registerRO);
        if (user != null) {
            return ResultVOUtils.returnSuccess();
        } else {
            return ResultVOUtils.returnFail();
        }
    }

    /**
     * 根据手机号查询用户
     *
     * @param getUserByPhoneRO
     * @return
     */
    @PostMapping("/getByPhone")
    public ResultVO getByPhone(@Valid @RequestBody GetUserByPhoneRO getUserByPhoneRO, BindingResult result) {
        ROValidUtils.valid(result);
        User user = userService.getByPhone(getUserByPhoneRO);
        return ResultVOUtils.returnSuccess(user);
    }

}
