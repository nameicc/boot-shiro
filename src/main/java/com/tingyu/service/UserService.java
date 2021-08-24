package com.tingyu.service;

import com.tingyu.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserService {

    public User getUserById(Integer id){
        User user = new User();
        user.setId(id);
        user.setUsername("测试" + id + "号");
        user.setPassword("shiro");
        user.setLastLoginTime(new Date());
        return user;
    }

    public User getUserByUsername(String username){
        User user = new User();
        user.setId(1);
        user.setUsername(username);
        user.setPassword(username);
        return user;
    }

    public List<String> getUserPermissions(Integer id){
        List<String> permissions = new ArrayList<>();
        permissions.add("user:query");
        return permissions;
    }

}
