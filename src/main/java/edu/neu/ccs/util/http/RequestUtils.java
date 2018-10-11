package edu.neu.ccs.util.http;

import edu.neu.ccs.util.common.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/3/2.
 */
public class RequestUtils {
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String LOGIN_ROLE_ID = "LOGIN_ROLE_ID";

    public RequestUtils() {
    }

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if(request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if(StringUtil.isEmpty(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    public static String getLoginUserId(HttpServletRequest request) {
        return (String)request.getSession().getAttribute("LOGIN_USER_ID");
    }

    public static String getLoginRoleId(HttpServletRequest request) {
        return (String)request.getSession().getAttribute("LOGIN_ROLE_ID");
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
