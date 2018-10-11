package edu.neu.ccs.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.neu.ccs.util.common.FileUtil;
import edu.neu.ccs.util.http.RequestUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
public class CsrfFilter implements Filter {
    private static final Log logger = LogFactory.getLog(CsrfFilter.class);
    private List<String> whiteUrls;
    private int _size = 0;

    public CsrfFilter() {
    }

    public void init(FilterConfig filterConfig) {
        String path = CsrfFilter.class.getResource("/").getFile();
        this.whiteUrls = FileUtil.readAsStringList(path + "csrfWhite.txt");
        this._size = null == this.whiteUrls?0:this.whiteUrls.size();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest e = (HttpServletRequest)request;
            HttpServletResponse res = (HttpServletResponse)response;
            String url = e.getRequestURL().toString();
            String referurl = e.getHeader("Referer");
            if(!this.isWhiteReq(referurl)) {
                e.getRequestDispatcher("/").forward(e, res);
                String log = "";
                String date = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
                String clientIp = RequestUtils.getClientIp(e);
                log = "跨站请求---->>>" + clientIp + "||" + date + "||" + referurl + "||" + url;
                logger.warn(log);
                return;
            }

            chain.doFilter(request, response);
        } catch (Exception var11) {
            logger.error("doFilter", var11);
        }

    }

    private boolean isWhiteReq(String referUrl) {
        if(referUrl != null && !"".equals(referUrl) && this._size != 0) {
            String refHost = "";
            referUrl = referUrl.toLowerCase();
            if(referUrl.startsWith("http://")) {
                refHost = referUrl.substring(7);
            } else if(referUrl.startsWith("https://")) {
                refHost = referUrl.substring(8);
            }

            Iterator var3 = this.whiteUrls.iterator();

            String urlTemp;
            do {
                if(!var3.hasNext()) {
                    return false;
                }

                urlTemp = (String)var3.next();
            } while(refHost.indexOf(urlTemp.toLowerCase()) <= -1);

            return true;
        } else {
            return true;
        }
    }

    public void destroy() {
    }
}
