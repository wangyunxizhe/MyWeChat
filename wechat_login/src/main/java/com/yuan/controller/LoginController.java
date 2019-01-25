package com.yuan.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.yuan.dao.UserMapper;
import com.yuan.entity.User;
import com.yuan.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 根据微信网页授权文档的介绍，共分4个步骤：
 * 这里不需要第三步：刷新access_token（如果需要）
 */
@Controller
@RequestMapping("WxAuth")
public class LoginController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserMapper userDao;

    @RequestMapping("toLogin")
    public String toLogin() {
        return "index";
    }

    @RequestMapping("wxlogin")
    public String doLogin(HttpServletResponse response) {
        //第一步：引导用户进入授权页面同意授权，获取code
        //请求微信服务器成功后的数据回调地址（需要能在公网访问，用Ngrok映射）
        String backUrl = "http://localhost.ngrok.xiaomiqiu.cn/WxAuth/callBack";
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=" + AuthUtil.APPID_TEST + "&" +
                "redirect_uri=" + URLEncoder.encode(backUrl) + "&" +
                "response_type=code&" +
                "scope=snsapi_userinfo&" +
                "state=STATE#wechat_redirect";
        return "redirect:" + url;
    }

    @RequestMapping("callBack")
    public String callBack(HttpServletRequest request) throws IOException {
        //自己实验的代码
        //通过wbd.html（未绑定页面）进入时该方法时获取的属性，如果为空。
        //则不是通过未绑定页面，而是正常走微信网页授权步骤的请求
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String openidSub = request.getParameter("openid");
        if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(openidSub)) {
            User user = new User();
            user.setOpenid(openidSub);
            user.setAccount(account);
            user.setPassword(password);
            int flag = userDao.updateOpenidByUser(user);
            if (flag > 0) {
                System.out.println("绑定成功");
            }
            return "index";
        }

        //第二步：通过code换取网页授权access_token
        String code = request.getParameter("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + AuthUtil.APPID_TEST + "&" +
                "secret=" + AuthUtil.APPSECRET_TEST + "&" +
                "code=" + code + "&" +
                "grant_type=authorization_code";
        JSONObject js = authUtil.doGetMapping(url);
        String openid = js.getString("openid");
        String token = js.getString("access_token");
        //第四步：拉取用户信息(需scope为 snsapi_userinfo)
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + token + "&" +
                "openid=" + openid + "&" +
                "lang=zh_CN";
        JSONObject userInfo = authUtil.doGetMapping(infoUrl);
        System.out.println("userInfo=====" + userInfo);

        //自己实验的代码
        //第一种项目中的使用情况：使用微信用户信息直接登录，无须注册和绑定
        request.setAttribute("userInfo", userInfo);
        //第二种情况：将微信用户信息与当前系统账号进行绑定
        String userOpenid = userInfo.getString("openid");
        String nickName = userDao.selectNicknameByOpenid(userOpenid);
        if (!StringUtils.isEmpty(nickName)) {//数据库有记录：说明微信用户跟该系统已经绑定
            request.setAttribute("nickName", nickName);
            System.out.println("数据库有记录，用户已绑定");
            return "ybd";
        } else if (StringUtils.isEmpty(nickName)) {//数据库未记录：说明微信用户跟该系统还未绑定
            request.setAttribute("openid", userOpenid);
            System.out.println("数据库无记录，用户未绑定");
            return "wbd";
        }
        return "success";
    }

}
