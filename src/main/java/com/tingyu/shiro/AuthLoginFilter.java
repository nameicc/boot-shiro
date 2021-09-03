package com.tingyu.shiro;

import com.alibaba.fastjson.JSONObject;
import com.tingyu.util.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

public class AuthLoginFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            // 只进行登录检测
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            // 向Response中写入Json响应数据，并声明ContentType和编码格式
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.write(JSONObject.toJSONString(JsonResult.error("登录校验未通过")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return false;
    }

}
