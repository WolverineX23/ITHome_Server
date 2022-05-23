package com.project.ithome.authentication.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.ithome.authentication.annotation.PassToken;
import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.exception.GetUserIdFromTokenException;
import com.project.ithome.authentication.exception.TokenNotFoundException;
import com.project.ithome.authentication.exception.UserNotFoundException;
import com.project.ithome.authentication.exception.VerificationException;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.exception.BaseException;
import com.project.ithome.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BaseException {
        String token =request.getHeader("token");
        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        Method method=handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if(method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken=method.getAnnotation(PassToken.class);
            if(passToken.required()) {
                return true;
            }
        }
        //检查是否有需要用户权限UserLoginToken的注解
        if(method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken=method.getAnnotation(UserLoginToken.class);
            if(userLoginToken.required()) {
                //执行认证
                if(token == null) {
                    throw new TokenNotFoundException();
                }
                //获取token中的userId
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new GetUserIdFromTokenException();
                }
                UserInfo user = userMapper.selectById(userId);
                if(user == null) {
                    throw new UserNotFoundException(userId);
                }
                //验证token
                JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new VerificationException();
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
