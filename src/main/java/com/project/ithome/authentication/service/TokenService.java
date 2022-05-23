package com.project.ithome.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.project.ithome.authentication.exception.GetUserIdFromTokenException;
import com.project.ithome.authentication.exception.TokenNotFoundException;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.exception.BaseException;
import org.springframework.stereotype.Service;

@Service("TokenService")
public class TokenService {
    public String getToken(UserInfo user) {
        String token;
        token= JWT.create()
                .withAudience(user.getUserId())               //将userId保存到token中
                .sign(Algorithm.HMAC256(user.getPassword()));  //将password作为token的密钥
        return token;
    }

    public String getUserIdFromToken(String token) throws BaseException {
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
        return userId;
    }
}
