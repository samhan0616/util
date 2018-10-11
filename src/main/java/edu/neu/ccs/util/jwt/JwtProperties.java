package edu.neu.ccs.util.jwt;

/**
 * Created by Administrator on 2018/3/2.
 */

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {
    public static String header = "Authorization";
    public static String secret = "AppSecret";
    public static Long expiration = Long.valueOf(604800L);
    public static String md5Key = "randomKey";

    public JwtProperties() {
    }

}
