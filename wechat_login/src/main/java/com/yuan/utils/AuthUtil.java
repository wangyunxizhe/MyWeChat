package com.yuan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 微信认证工具类
 */
@Component
public class AuthUtil {

    //具体的值参考微信开发者的基本配置中
    public static final String APPID = "wx6ebb2cc9775d636f";//正式账号（只有部分微信接口请求权限）
    public static final String APPSECRET = "a60298f866118e2c069bd40a0b25c133";//正式账号（只有部分微信接口请求权限）
    public static final String APPID_TEST = "wx247a262650eeb4f0";//测试账号（全权限）
    public static final String APPSECRET_TEST = "91627d70598dda9d0032872af96234bc";//测试账号（全权限）

    /**
     * 向微信服务器发送GET请求
     */
    public JSONObject doGetMapping(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject js = null;
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            js = JSON.parseObject(result);
        }
        httpGet.releaseConnection();//释放掉链接
        return js;
    }

}
