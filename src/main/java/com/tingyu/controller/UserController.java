package com.tingyu.controller;

import com.tingyu.entity.User;
import com.tingyu.service.UserService;
import com.tingyu.util.JsonResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("query/{id}")
    public JsonResult queryUser(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return JsonResult.ok(user);
    }

}
