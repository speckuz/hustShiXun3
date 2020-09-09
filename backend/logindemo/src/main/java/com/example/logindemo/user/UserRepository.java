package com.example.logindemo.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository <User, Integer> {
    //方法按需设置
    User getUserByUserName(String userName);
    List<User> getUsersByInterests(String userInterest);
    User getUserByUserId(Integer userId);
}
