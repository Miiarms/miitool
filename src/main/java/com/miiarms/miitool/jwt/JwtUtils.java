package com.miiarms.miitool.jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {
    private JwtUtils(){}

    private static final String token_key = "nwcsg-elink-app";
    private static final String payload_key = "user";

    public static String createToken(Object payload) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("nwcsg", "garbage");
        headers.put("jadp", "garbage framework");
        return Jwts.builder()
                .setHeader(headers)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(3, ChronoUnit.HOURS))) //3个小时过期
                .claim(payload_key, JSON.toJSONString(payload))
                .signWith(SignatureAlgorithm.HS512, token_key).compact();
    }

    public static boolean verify(String token){
        if(!StringUtils.hasLength(token)){
            return false;
        }
        try {
            Claims claims = parseToken(token);
            //是否过期
            return claims != null && claims.getExpiration() != null && !claims.getExpiration().before(new Date());
        }catch (ExpiredJwtException exp){
            log.info("token已过期！");
        }catch (Exception e){
            log.error("解析jwt 失败", e);
        }
       return false;
    }

    private static Claims parseToken(String token) {
        if(!StringUtils.hasLength(token)){
            return null;
        }
        return Jwts.parser()
                .setSigningKey(token_key)
                .parseClaimsJws(token)
                .getBody();
    }

    public static <T> T getPayload(String token, Class<T> clazz){
        Claims claims = parseToken(token);
        if(claims == null){
            return null;
        }
        String uu = (String) claims.get(payload_key);
        return JSON.parseObject(uu, clazz);
    }
}
