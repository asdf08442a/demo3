package com.enterprise.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {

    ADMIN(0, "管理员"),
    NORMAL(1, "普通用户"),;

    private Integer code;

    private String message;
}
