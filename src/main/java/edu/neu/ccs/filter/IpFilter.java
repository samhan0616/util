package edu.neu.ccs.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 支持IP段*通配符配置，如：
 * --所有IP；
 192.168.* --所有192.168.开头的IP；
 192.168.0.* --所有0网段的IP；
 10.195.13.0-10.195.13.255 --也支持这种格式的IP段指定；
 192.168.1.100 --单独IP指定；
 192.168.1*.50 --所有以192.168.1开头.50结尾的IP，即192.168.10.50-192.168.19.50; 192.168.100.50-192.168.199.50。
 */
public class IpFilter implements Filter {

    protected Logger log = LoggerFactory.getLogger(IpFilter.class);

    //分号拼接
    public static String AllowIPList = "" ;

    // 允许的IP访问列表
    private Set<String> ipList = new HashSet<String>();

    // IP的正则
    private Pattern pattern = Pattern
            .compile("(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})");

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;

        // 获取请求IP
        InetAddress inet = null;
        String ip = request.getRemoteAddr();
        try {
            inet = InetAddress.getLocalHost();
            if (ip.equals("127.0.0.1"))
                ip = inet.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // 不在IP白名单里，
        if (!checkLoginIP(ip)) {
            response.getOutputStream()
                    .write((ip + " ip forbidden.").getBytes());
            response.getOutputStream().flush();
            return;
        }

        // 其它继续
        chain.doFilter(arg0, arg1);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        String allowIp = AllowIPList;

        // 转换IP地址
        init(allowIp);
        log.info("Allow IP list:" + ipList);
    }

    /**
     * 转换IP地址的形式.
     *
     * @param allowIp
     */
    private void init(String allowIp) {

        // 192.168.0.*转换为192.168.0.1-192.168.0.255
        for (String allow : allowIp.replaceAll("\\s", "").split(";")) {
            if (allow.indexOf("*") > -1) {
                String[] ips = allow.split("\\.");
                String[] from = new String[] { "0", "0", "0", "0" };
                String[] end = new String[] { "255", "255", "255", "255" };
                List<String> tem = new ArrayList<String>();
                for (int i = 0; i < ips.length; i++)
                    if (ips[i].indexOf("*") > -1) {
                        tem = complete(ips[i]);
                        from[i] = null;
                        end[i] = null;
                    } else {
                        from[i] = ips[i];
                        end[i] = ips[i];
                    }

                StringBuffer fromIP = new StringBuffer();
                StringBuffer endIP = new StringBuffer();
                for (int i = 0; i < 4; i++)
                    if (from[i] != null) {
                        fromIP.append(from[i]).append(".");
                        endIP.append(end[i]).append(".");
                    } else {
                        fromIP.append("[*].");
                        endIP.append("[*].");
                    }
                fromIP.deleteCharAt(fromIP.length() - 1);
                endIP.deleteCharAt(endIP.length() - 1);

                for (String s : tem) {
                    String ip = fromIP.toString().replace("[*]",
                            s.split(";")[0])
                            + "-"
                            + endIP.toString().replace("[*]", s.split(";")[1]);
                    ipList.add(ip);
                }
            } else {
                ipList.add(allow);
            }
        }
    }

    /**
     * 对单个IP节点进行范围限定
     *
     * @param arg
     * @return 返回限定后的IP范围，格式为List[10;19, 100;199]
     */
    private List<String> complete(String arg) {
        List<String> com = new ArrayList<String>();
        if (arg.length() == 1) {
            com.add("0;255");
        } else if (arg.length() == 2) {
            String s1 = complete(arg, 1);
            if (s1 != null)
                com.add(s1);
            String s2 = complete(arg, 2);
            if (s2 != null)
                com.add(s2);
        } else {
            String s1 = complete(arg, 1);
            if (s1 != null)
                com.add(s1);
        }
        return com;
    }

    private String complete(String arg, int length) {
        String from = "";
        String end = "";
        if (length == 1) {
            from = arg.replace("*", "0");
            end = arg.replace("*", "9");
        } else {
            from = arg.replace("*", "00");
            end = arg.replace("*", "99");
        }
        if (Integer.valueOf(from) > 255)
            return null;
        if (Integer.valueOf(end) > 255)
            end = "255";
        return from + ";" + end;
    }

//    /**
//     * 在添加至白名单时进行格式校验
//     *
//     * @param ip
//     * @return
//     */
//    private boolean validate(String ip) {
//        for (String s : ip.split("-"))
//            if (!pattern.matcher(s).matches()) {
//                return false;
//            }
//        return true;
//    }

    private boolean checkLoginIP(String ip) {
        if (ipList.isEmpty() || ipList.contains(ip))
            return true;
        else {
            for (String allow : ipList) {
                if (allow.indexOf("-") > -1) {
                    String[] from = allow.split("-")[0].split("\\.");
                    String[] end = allow.split("-")[1].split("\\.");
                    String[] tag = ip.split("\\.");

                    // 对IP从左到右进行逐段匹配
                    boolean check = true;
                    for (int i = 0; i < 4; i++) {
                        int s = Integer.valueOf(from[i]);
                        int t = Integer.valueOf(tag[i]);
                        int e = Integer.valueOf(end[i]);
                        if (!(s <= t && t <= e)) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}

