package com.tingyu.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Value("${session.redis.expireTime}")
    private long expireTime;

    @Bean
    public SecurityManager securityManager(UserAuthorizingRealm userAuthorizingRealm, DefaultWebSessionManager defaultWebSessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userAuthorizingRealm);
        securityManager.setRememberMeManager(null);
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authLogin", new AuthLoginFilter());
        shiroFilterFactoryBean.setFilters(filters);
        // 拦截规则
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 首页、登录页和登录请求放行
        filterMap.put("/index", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/doLogin", "anon");
        // 其他请求需要通过验证
        filterMap.put("/**", "authLogin");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * @Author shichuanfeng
     * @Description 代理生成器，需要借助SpringAOP来扫描@RequireRoles和@RequirePermissions等注解，生成代理类实现功能增强，从而实现权限控制
     * @Date 2021/8/24 9:43
     * @Param []
     **/
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * @Author shichuanfeng
     * @Description 定义Session交由Redis管理
     * @Date 2021/8/25 16:33
     * @Param [redisSessionDAO]
     **/
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(expireTime * 1000);
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionDAO(redisSessionDAO);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        // 修改Cookie中的SessionId的key，默认为JSESSIONID，自定义名称
        defaultWebSessionManager.setSessionIdCookie(new SimpleCookie("JSESSIONID"));
        return defaultWebSessionManager;
    }

}
