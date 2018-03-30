package com.enterprise.demo.repository;

import com.enterprise.demo.dataobject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findOneByPhone(String phone);

    User findByUsernameAndPassword(String username, String password);
}
