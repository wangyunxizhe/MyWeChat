package com.yuan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuan.entity.AccessToken;
import com.yuan.entity.menu.Button;
import com.yuan.entity.menu.ClickButton;
import com.yuan.entity.menu.Menu;
import com.yuan.entity.menu.ViewButton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取微信access_token
 */
@Component
public class WeChatUtil {

    //具体的值参考微信开发者的基本配置中
    private static final String APPID = "wx6ebb2cc9775d636f";//正式账号（只有部分微信接口请求权限）
    private static final String APPSECRET = "a60298f866118e2c069bd40a0b25c133";//正式账号（只有部分微信接口请求权限）
    private static final String APPID_TEST = "wx247a262650eeb4f0";//测试账号（全权限）
    private static final String APPSECRET_TEST = "91627d70598dda9d0032872af96234bc";//测试账号（全权限）
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=APPID&secret=APPSECRET";//请求access_token的url
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?" +
            "access_token=ACCESS_TOKEN&type=TYPE";//上传多媒体的url
    private static final String GET_URL = "https://api.weixin.qq.com/cgi-bin/media/get?" +
            "access_token=ACCESS_TOKEN&media_id=MEDIA_ID";//下载多媒体的url
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?" +
            "access_token=ACCESS_TOKEN";//创建自定义菜单的url
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?" +
            "access_token=ACCESS_TOKEN";//查询自定义菜单的url
    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?" +
            "access_token=ACCESS_TOKEN";//删除自定义菜单的url

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
        return js;
    }

    /**
     * 向微信服务器发送POST请求
     */
    public JSONObject doPostMapping(String url, String outStr) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject js = null;
        httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            js = JSON.parseObject(result);
        }
        return js;
    }

    /**
     * 获取AccessToken
     */
    public AccessToken getAccessToken() throws IOException {
        AccessToken accessToken = new AccessToken();
        //将自己的参数值放入到请求微信access_token的url中
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID_TEST).replace("APPSECRET", APPSECRET_TEST);
        //获取微信端请求结果
        JSONObject js = doGetMapping(url);
        if (js != null) {
            accessToken.setToken(js.getString("access_token"));//key值参考微信获取access_token的开发者文档
            accessToken.setExpiresIn(js.getInteger("expires_in"));
        }
        return accessToken;
    }

    /**
     * 微信端文件上传工具类
     * 功能：获取上传后微信回调的media_id字段值，该值是后续为了让订阅号，发送图片消息的必要参数。
     * 此media_id就代表了上传的这张图片
     */
    public String upload(String filePath, String accessToken, String type) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }
        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);
        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        JSONObject jsonObj = JSONObject.parseObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if (!"image".equals(type)) {
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }

    /**
     * 组装自定义菜单
     */
    public Menu initMenu() {
        Menu menu = new Menu();
        ClickButton button11 = new ClickButton();//1级菜单：click类型
        button11.setName("天字1号房");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();//1级菜单：view类型
        button21.setName("天字2号房");
        button21.setType("view");
        button21.setUrl("http://www2.flightclub.cn");

        ClickButton button31 = new ClickButton();//2级菜单：scancode_push类型（扫码）。属于1级菜单天字3号房
        button31.setName("扫码");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();//2级菜单：location_select类型（地理位置）。属于1级菜单天字3号房
        button32.setName("发送位置");
        button32.setType("location_select");
        button32.setKey("32");

        Button button = new Button();//1级菜单：有子菜单
        button.setName("天字3号房");
        button.setSub_button(new Button[]{button31, button32});

        menu.setButton(new Button[]{button11, button21, button});
        return menu;
    }

    /**
     * 创建自定义菜单
     */
    public int createMenu(String token, String menu) throws Exception {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostMapping(url, menu);
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    /**
     * 查询自定义菜单
     */
    public JSONObject queryMenu(String token) throws Exception {
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetMapping(url);
        return jsonObject;
    }

    /**
     * 删除自定义菜单
     */
    public int deleteMenu(String token) throws Exception {
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetMapping(url);
        int result = 0;
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        WeChatUtil weChatUtil = new WeChatUtil();
        AccessToken accessToken = weChatUtil.getAccessToken();
        System.out.println("票据：" + accessToken.getToken());
        System.out.println("有效时间：" + accessToken.getExpiresIn());
        //上传临时素材，有效期3天
//        String imgPath = "F:\\图片\\素材\\老实点.jpg";
//        String mediaId = weChatUtil.upload(imgPath, accessToken.getToken(), "image");
//        System.out.println(mediaId);
        //创建自定义菜单
//        String menu = JSONObject.toJSONString(weChatUtil.initMenu());
//        int result = weChatUtil.createMenu(accessToken.getToken(), menu);
//        if (result == 0) {
//            System.out.println("创建菜单成功");
//        } else {
//            System.out.println("创建菜单失败，错误码：" + result);
//        }
        //查询自定义菜单
        JSONObject js = weChatUtil.queryMenu(accessToken.getToken());
        System.out.println(js);
    }
}
