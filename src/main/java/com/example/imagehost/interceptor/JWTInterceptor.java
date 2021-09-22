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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK); // 200
        }
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.contains("Bearer ")){   // BearerToken
            return false;
        }
        String mailbox = JwtUtils.decodeJwt(bearerToken.split(" ")[1]);
        if (mailbox==null){
            return false;
        }
        request.setAttribute("mailbox",mailbox);
        return true;
    }
}
