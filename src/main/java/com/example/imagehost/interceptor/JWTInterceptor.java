package com.example.imagehost.interceptor;

import com.example.imagehost.util.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// 请求拦截器

public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //HTTP 请求头获取源IP或域名　并配置到跨域源中
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,PUT,HEAD");
        response.addHeader("Access-Control-Max-Age", "3600000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With, Content-Type, Accept,token");
        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK); // 200
        }

        String login = request.getRequestURI();
//        if (login.contains("/api/auth/register")||
//                login.contains("/api/auth/identify")||
//                login.contains("/api/auth/login")||
//                login.contains("/api/file/download")
//        ){
//            return true;   // 不需要拦截登陆请求
//        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.contains("Bearer ")){   // BearerToken
            return false;
        }

        String mailbox = JwtUtils.decodeJwt(bearerToken.split(" ")[1]);
        request.setAttribute("mailbox",mailbox);
        return true;
    }
}
