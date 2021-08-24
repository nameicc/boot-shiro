package com.tingyu.controller;

import com.tingyu.entity.User;
import com.tingyu.service.UserService;
import com.tingyu.util.JsonResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping("query/{id}")
    @RequiresPermissions(value = {"user:query"})
    public JsonResult queryUser(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return JsonResult.ok(user);
    }

}
