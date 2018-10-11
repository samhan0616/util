package edu.neu.ccs.util.jwt;

import edu.neu.ccs.util.common.RandomUtil;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Component
public class JwtTokenUtil {

    private static Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public JwtTokenUtil() {
    }

    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    public static Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    public static String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    public static String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    public static String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, JwtProperties.md5Key);
    }

    public static Claims getClaimFromToken(String token) {
        return (Claims) Jwts.parser().setSigningKey(JwtProperties.secret).parseClaimsJws(token).getBody();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(JwtProperties.secret).parseClaimsJws(token);
    }

    public static Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return Boolean.valueOf(expiration.before(new Date()));
    }

//    public static String generateToken(String userName, String randomKey) {
//        HashMap claims = new HashMap();
//        claims.put(JwtProperties.md5Key, randomKey);
//        return doGenerateToken(claims, userName);
//    }

    public static String doGenerateToken(Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + JwtProperties.expiration.longValue() * 1000L);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, JwtProperties.secret).compact();
    }

    public static String getRandomKey() {
        return RandomUtil.randomString(6);
    }

    public static String getTokenFromReq() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getHeader(JwtProperties.header);
    }

    /**
     * 使用指定密钥生成规则，生成JWT加解密密钥
     *
     * @param alg  加解密类型
     * @param rule 密钥生成规则
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        // 将密钥生成键转换为字节数组
        byte[] bytes = Base64.decodeBase64(rule);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getJcaName());
    }


    public static boolean checkJWT(String claimsJws) {
        boolean flag = false;
        try {
            // 获取 JWT 的 payload 部分
            flag = (null != parseToken(claimsJws).getBody());
        } catch (Exception e) {
            logger.warn("JWT验证出错，错误原因：{}", e.getMessage());
        }
        return flag;

    }

    public static void main(String[] args) {
        Map<String ,Object> map = new HashMap<>();
        map.put("xh", "Xiao Han");
        String token = JwtTokenUtil.doGenerateToken(map, "user");
        logger.info(token);
        logger.info(parseToken(token).toString());
        //secret错误
        String wrongSecret = "eyJhbGciOiJIUzUxMiJ9.eyJ4aCI6IlhpYW8gSGFuIiwic3ViIjoidXNlciIsImV4cCI6MTUyNjI3NDkzNCwiaWF0IjoxNTI1NjcwMTM0fQ.sPYNsVJJ9jZvI2hsBl9nx3WtRs9Lfbuj_T8jpss_9udLq7Zo4uo2HPfA0qOQ31O1zzfwBzCcfEDPdAXMioDXNA";
        logger.info(checkJWT(wrongSecret) + "");
    }
}
