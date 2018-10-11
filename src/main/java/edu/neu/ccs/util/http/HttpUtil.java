package edu.neu.ccs.util.http;

/**
 * Created by Administrator on 2018/3/2.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Preconditions;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.neu.ccs.util.common.StringUtil;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public HttpUtil() {
    }

    public static String httpSend(String url, Map<String, Object> param, boolean post, File uploadFile) {
        String response = null;
        if(post) {
            response = post(url, param);
        } else {
            response = get(url, param);
        }

        return response;
    }

    public static String post(String baseUrl, Map<String, Object> paramMap) {
        return post(baseUrl, (Map)null, (String)null, (String)null, paramMap, (String)null, false);
    }

    public static String post(String baseUrl, String json) {
        return post(baseUrl, (Map)null, (String)null, (String)null, (Map)null, json, false);
    }

    public static String post(String baseUrl, Map<String, String> headers, String json, boolean contentType) {
        return post(baseUrl, headers, (String)null, (String)null, (Map)null, json, contentType);
    }

    public static String post(String baseUrl, Map<String, String> headerMap, String routekey, String routevalue, Map<String, Object> paramMap, String body, boolean contentType) {
        HttpResponse jsonResponse = null;
        HttpRequestWithBody httpRequestWithBody = null;

        try {
            if(contentType && !CollectionUtils.isEmpty(headerMap)) {
                httpRequestWithBody = Unirest.post(baseUrl).headers(headerMap);
            } else {
                httpRequestWithBody = Unirest.post(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8");
            }

            if(StringUtils.isNotEmpty(routekey)) {
                httpRequestWithBody.routeParam(routekey, routevalue);
            }

            if(!CollectionUtils.isEmpty(paramMap)) {
                httpRequestWithBody.queryString(paramMap);
            }

            if(StringUtils.isNotEmpty(body)) {
                httpRequestWithBody.body(body);
            }

            jsonResponse = httpRequestWithBody.asString();
        } catch (UnirestException var10) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var10);
        }

        if(isOKStatusCode(jsonResponse.getStatus())) {
            logger.info((String)jsonResponse.getBody());
            return (String)jsonResponse.getBody();
        } else {
            logger.error(String.format("http request failed %s %d %s", new Object[]{baseUrl, Integer.valueOf(jsonResponse.getStatus()), jsonResponse.getBody()}));
            return "";
        }
    }

    public static Map<String, Object> post(String baseUrl, Map<String, String> headerMap, String routekey, String routevalue, Map<String, Object> paramMap, String body) {
        String response = null;
        HttpRequestWithBody httpRequestWithBody = null;
        httpRequestWithBody = Unirest.post(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8");

        try {
            if(!CollectionUtils.isEmpty(headerMap)) {
                httpRequestWithBody = Unirest.post(baseUrl).headers(headerMap);
            }

            if(StringUtils.isNotEmpty(routekey)) {
                httpRequestWithBody.routeParam(routekey, routevalue);
            }

            if(!CollectionUtils.isEmpty(paramMap)) {
                httpRequestWithBody.queryString(paramMap);
            }

            if(StringUtils.isNotEmpty(body)) {
                httpRequestWithBody.body(body);
            }

            HttpResponse e = httpRequestWithBody.asString();
            response = (String)e.getBody();
            logger.info(String.format("http request success %s %d %s", new Object[]{baseUrl, Integer.valueOf(e.getStatus()), response}));
            if(!JSONUtils.mayBeJSON(response)) {
                HashMap result = new HashMap();
                result.put("status", "success");
                result.put("result", response);
                return result;
            }
        } catch (UnirestException var10) {
            logger.error(String.format("http request failed %s %s", new Object[]{baseUrl, response}));
        }

        return (Map)JSON.parseObject(response, Map.class, new Feature[0]);
    }

    public static String put(String baseUrl, Map<String, Object> paramMap) {
        return put(baseUrl, (Map)null, (String)null, (String)null, paramMap, (String)null);
    }

    public static String put(String baseUrl, String json) {
        return put(baseUrl, (Map)null, (String)null, (String)null, (Map)null, json);
    }

    public static String put(String baseUrl, Map<String, String> headerMap, String routekey, String routevalue, Map<String, Object> paramMap, String body) {
        Object response = null;
        HttpRequestWithBody httpRequestWithBody = null;
        httpRequestWithBody = Unirest.put(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8");

        try {
            if(!CollectionUtils.isEmpty(headerMap)) {
                httpRequestWithBody = Unirest.post(baseUrl).headers(headerMap);
            }

            if(StringUtils.isNotEmpty(routekey)) {
                httpRequestWithBody.routeParam(routekey, routevalue);
            }

            if(!CollectionUtils.isEmpty(paramMap)) {
                httpRequestWithBody.queryString(paramMap);
            }

            if(StringUtils.isNotEmpty(body)) {
                httpRequestWithBody.body(body);
            }

            HttpResponse e = httpRequestWithBody.asString();
            if(isOKStatusCode(e.getStatus())) {
                return (String)e.getBody();
            }
        } catch (UnirestException var9) {
            logger.error(String.format("http request failed %s %s", new Object[]{baseUrl, response}));
        }

        return (String)response;
    }

    public static String post(String baseUrl, String fileName, InputStream uploadFile, Map<String, Object> paramMap) {
        HttpResponse jsonResponse = null;

        try {
            jsonResponse = Unirest.post(baseUrl).fields(paramMap).field(fileName, uploadFile, fileName).asString();
        } catch (UnirestException var6) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var6);
        }

        if(isOKStatusCode(jsonResponse.getStatus())) {
            return (String)jsonResponse.getBody();
        } else {
            logger.error(String.format("http post request failed %s %d", new Object[]{baseUrl, Integer.valueOf(jsonResponse.getStatus())}));
            return "";
        }
    }

    public static String post(String baseUrl, String fileName, InputStream uploadFile, Map<String, Object> paramMap, String routeKey, String routeValue) {
        Preconditions.checkArgument(!StringUtil.isEmpty(routeKey) | !StringUtil.isEmpty(routeValue), "routeKey|routeValue can\'t be empty.");
        HttpResponse jsonResponse = null;

        try {
            if(!StringUtil.isEmpty(fileName) && !StringUtil.isEmpty(uploadFile)) {
                jsonResponse = Unirest.post(baseUrl).routeParam(routeKey, URLEncoder.encode(routeValue, "UTF-8")).fields(paramMap).field(fileName, uploadFile, fileName).asString();
            } else {
                jsonResponse = Unirest.post(baseUrl).routeParam(routeKey, URLEncoder.encode(routeValue, "UTF-8")).fields(paramMap).asString();
            }
        } catch (UnirestException var8) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var8);
        } catch (UnsupportedEncodingException var9) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var9);
        }

        if(isOKStatusCode(jsonResponse.getStatus())) {
            return (String)jsonResponse.getBody();
        } else {
            logger.error(String.format("http post request failed %s %d", new Object[]{baseUrl, Integer.valueOf(jsonResponse.getStatus())}));
            return "";
        }
    }

    public static String post(String baseUrl, Map<String, Object> paramMap, String routeKey, String routeValue) {
        return post(baseUrl, (String)null, (InputStream)null, (Map)paramMap, (String)routeKey, routeValue);
    }

    public static String get(String baseUrl, Map<String, Object> paramMap) {
        return get(baseUrl, paramMap, (String)null, (String)null);
    }

    public static String get(String baseUrl, Map<String, Object> paramMap, String routeKey, String routeValue) {
        HttpResponse jsonResponse = null;

        try {
            if(!StringUtil.isEmpty(routeKey) && !StringUtil.isEmpty(routeValue)) {
                jsonResponse = Unirest.get(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8").routeParam(routeKey, URLEncoder.encode(routeValue, "UTF-8")).queryString(paramMap).asString();
            } else {
                jsonResponse = Unirest.get(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8").queryString(paramMap).asString();
            }
        } catch (UnirestException var6) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var6);
        } catch (UnsupportedEncodingException var7) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var7);
        }

        if(isOKStatusCode(jsonResponse.getStatus())) {
            return (String)jsonResponse.getBody();
        } else {
            logger.error(String.format("http get request failed %s %d", new Object[]{baseUrl, Integer.valueOf(jsonResponse.getStatus())}));
            return "";
        }
    }

    public static String delete(String baseUrl, Map<String, Object> paramMap) {
        return delete(baseUrl, paramMap, (String)null, (String)null, (String)null);
    }

    public static String delete(String baseUrl, String body) {
        return delete(baseUrl, (Map)null, body, (String)null, (String)null);
    }

    public static String delete(String baseUrl, Map<String, Object> paramMap, String body, String routeKey, String routeValue) {
        HttpResponse jsonResponse = null;
        HttpRequestWithBody httpRequestWithBody = Unirest.delete(baseUrl).header("Accept", "application/json").header("Content-Type", "application/json;charset=UTF-8");

        try {
            if(!StringUtil.isEmpty(routeKey) && !StringUtil.isEmpty(routeValue)) {
                httpRequestWithBody.routeParam(routeKey, URLEncoder.encode(routeValue, "UTF-8"));
            }

            if(!CollectionUtils.isEmpty(paramMap)) {
                httpRequestWithBody.queryString(paramMap);
            }

            if(StringUtils.isNotEmpty(body)) {
                httpRequestWithBody.body(body);
            }

            jsonResponse = httpRequestWithBody.asString();
        } catch (UnirestException var8) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var8);
        } catch (UnsupportedEncodingException var9) {
            logger.error(String.format("http request failed %s", new Object[]{baseUrl}), var9);
        }

        if(isOKStatusCode(jsonResponse.getStatus())) {
            return (String)jsonResponse.getBody();
        } else {
            logger.error(String.format("http delete request failed %s %d", new Object[]{baseUrl, Integer.valueOf(jsonResponse.getStatus())}));
            return "";
        }
    }

    private static boolean isOKStatusCode(int statuscode) {
        return statuscode == 200?true:(statuscode == 202?true:(statuscode == 201?true:statuscode == 302));
    }
}