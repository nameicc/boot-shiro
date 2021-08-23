package com.tingyu.shiro;

import com.tingyu.entity.User;
import com.tingyu.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class UserAuthorizingRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * @Author shichuanfeng
     * @Description 授权
     * @Date 2021/8/23 16:25
     * @Param [principalCollection]
     * @Return 
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User)principalCollection.getPrimaryPrincipal();
        List<String> permissions = userService.getUserPermissions(user.getId());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * @Author shichuanfeng
     * @Description 认证
     * @Date 2021/8/23 16:26
     * @Param [authenticationToken]
     * @Return 
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        User user = userService.getUserByUsername(token.getUsername());
        if(user == null){
            throw new UnknownAccountException("账号不存在！");
        }
        if(!user.getPassword().equals(new String(token.getPassword()))){
            throw new UnknownAccountException("密码错误！");
        }
        user.setLastLoginTime(new Date());
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

}
