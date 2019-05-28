package com.miaosha.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 生成签名，15分钟后过期
 */
public class JwtUtils {
    /**设置超时时间*/
    private static final long EXPIRE_TIME = 15*60*1000;

    /**token私钥*/
    private static final String TOKEN_SECRET = UUIDUtils.generatorUUID();


    public static String sign(String username,String userid){
        //token过期时间
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);

            //私钥加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ","JWT");
            header.put("alg","HS256");

            //附带username,userid生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("userid",userid)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }
        catch (Exception e){
            return null;
        }

    }
}
