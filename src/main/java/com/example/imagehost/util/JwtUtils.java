package com.example.imagehost.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtils {

    private final static int days = 30; //有效时间

    private final static String secretKey = "SegmentTree"; //有效时间

    public static String generateJwt(String mailbox){
        return JWT.create()
                .withIssuer("Pigeon377")   // 发布者
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(days*24*60*1000+System.currentTimeMillis()))
                .withAudience(mailbox)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static String decodeJwt(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getAudience().get(0);
            // getAudience方法返回List<String>
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}