package com.enterprise.demo.dataobject.ro;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jinzhengang
 * @date 2018-03-29 16:23
 **/
@Data
public class GetUserByPhoneRO {
    @NotNull(message = "手机号不能为空")
    private String phone;
}
