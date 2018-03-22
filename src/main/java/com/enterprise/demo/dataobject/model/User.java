package com.enterprise.demo.dataobject.model;

import com.enterprise.demo.enums.UserRoleEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    //0 超级管理员  1 普通用户
    private Integer role = UserRoleEnum.NORMAL.getCode();

    private Date createTime;

    private Date updateTime;

}
