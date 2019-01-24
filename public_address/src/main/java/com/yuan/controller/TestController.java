package com.yuan.controller;

import com.yuan.utils.CheckUtil;
import com.yuan.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private MessageUtil messageUtil;

    @RequestMapping(value = "/testOne", method = RequestMethod.GET)
    public String testOne(@RequestParam(value = "signature") String signature,
                          @RequestParam(value = "timestamp") String timestamp,
                          @RequestParam(value = "nonce") String nonce,
                          @RequestParam(value = "echostr") String echostr) {
        if (checkUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }

    //微信是发送到页面自定义的服务器配置中的URL，以POST的方式，所以这里的url映射必须与服务器配置中的URL一致
    @RequestMapping(value = "/testOne", method = RequestMethod.POST)
    public String getMsg(HttpServletRequest request) throws Exception {
        Map<String, String> map = messageUtil.xmlToMap(request);
        String fromUserName = map.get("FromUserName");//发送方帐号（一个OpenID）
        String toUserName = map.get("ToUserName");//开发者微信号
        String msgType = map.get("MsgType");//消息类型
        String content = map.get("Content");//消息内容
        String msgId = map.get("MsgId");
        String msg = null;
        if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {//判断消息类型是否是文本消息
            if ("王".equals(content)) {//用户输入王
                msg = messageUtil.initText(toUserName, fromUserName, messageUtil.one());
            } else if ("渊".equals(content)) {//用户输入渊
                msg = messageUtil.initText(toUserName, fromUserName, messageUtil.two());
            } else if ("爸爸".equals(content)) {//用户输入爸爸==调出主菜单
                msg = messageUtil.initText(toUserName, fromUserName, messageUtil.menuText());
            } else {//用户发其他消息时，直接回应表情包
                msg = messageUtil.initImageMessage(toUserName, fromUserName);
            }
        } else if (MessageUtil.MESSAGE_EVNET.equals(msgType)) {//判断消息类型是否是事件类型
            String eventType = map.get("Event");//获取用户触发的是哪个事件：关注，取消关注，菜单点击。。。
            if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {//关注事件==构建主菜单
                msg = messageUtil.initText(toUserName, fromUserName, messageUtil.menuText());
            } else if (MessageUtil.MESSAGE_CLICK.equals(eventType)) {//点击事件==构建主菜单
                msg = messageUtil.initText(toUserName, fromUserName, messageUtil.menuText());
            } else if (MessageUtil.MESSAGE_VIEW.equals(eventType)) {//VIEW事件==打印url
                String url = map.get("EventKey");
                System.out.println(url);
                msg = messageUtil.initText(toUserName, fromUserName, url);
            } else if (MessageUtil.MESSAGE_SCANCODE.equals(eventType)) {//扫码事件==打印key
                String key = map.get("EventKey");
                System.out.println(key);
                msg = messageUtil.initText(toUserName, fromUserName, key);
            }
        } else if (MessageUtil.MESSAGE_LOCATION.equals(msgType)) {//发送地理位置事件
            String label = map.get("Label");
            msg = messageUtil.initText(toUserName, fromUserName, "已将你所在位置，发送到你债主的微信中："
                    + label);
        }
        return msg;
    }

}
