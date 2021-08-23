package com.tingyu.controller;

import com.tingyu.util.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public JsonResult login(){
        return JsonResult.ok("这是登录页，请进行登录！");
    }

    @RequestMapping("/doLogin")
    public JsonResult doLogin(@RequestParam("username") String username, @RequestParam("password") String password){
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        currentUser.login(token);
        return JsonResult.ok("登录成功！");
    }

    @RequestMapping("/index")
    public JsonResult index(){
        return JsonResult.ok("这是首页，游客也可以访问！");
    }

}
