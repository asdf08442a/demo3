package com.enterprise.demo.repository;

import com.enterprise.demo.dataobject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: WireChen
 * @Date: Created in 下午4:03 2018/3/16
 * @Description:
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPasswordAndRole(String username, String password, Integer role);

    User findByUsernameAndRole(String username, Integer role);
}
