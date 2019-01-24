package com.yuan.utils;

import com.thoughtworks.xstream.XStream;
import com.yuan.entity.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号接收消息处理类
 */
@Component
public class MessageUtil {

    @Autowired
    private WeChatUtil weChatUtil;

    //微信中的各种消息类型
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVNET = "event";//事件推送：关注，取消关注，菜单点击。。。
    public static final String MESSAGE_SUBSCRIBE = "subscribe";//关注
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";//取消关注
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    public static final String MESSAGE_SCANCODE = "scancode_push";

    /**
     * 将微信传来的xml转map
     */
    public Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream is = request.getInputStream();
        Document doc = reader.read(is);
        //获取xml根元素
        Element root = doc.getRootElement();
        List<Element> elements = root.elements();
        for (Element e : elements) {
            map.put(e.getName(), e.getText());
        }
        is.close();
        return map;
    }

    /**
     * 将自定义的文本消息对象转化为微信指定的xml
     */
    public String textMsgToXml(TextMsg textMsg) {
        XStream xStream = new XStream();
        //将xml的根节点替换成“xml”
        xStream.alias("xml", textMsg.getClass());
        return xStream.toXML(textMsg);
    }

    /**
     * 主菜单介绍回复
     */
    public String menuText() {
        StringBuffer sb = new StringBuffer();
        sb.append("来了老弟~~\n\n");
        sb.append("王：介绍\n");
        sb.append("渊：功能\n");
//        sb.append("是：词组翻译\n\n");
        sb.append("回复“爸爸”调出此菜单。");
        return sb.toString();
    }

    /**
     * 用户输入王的回复
     */
    public String one() {
        StringBuffer sb = new StringBuffer();
        sb.append("自用公众号，用于对微信公众号的开发和调试");
        return sb.toString();
    }

    /**
     * 用户输入渊的回复
     */
    public String two() {
        StringBuffer sb = new StringBuffer();
        sb.append("主要是对自己开发的跟微信公众号相关的工程，进行调试");
        return sb.toString();
    }

    /**
     * 组装文本消息
     */
    public String initText(String toUserName, String fromUserName, String content) {
        TextMsg text = new TextMsg();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return textMsgToXml(text);
    }

    /**
     * 组装被动回复图片消息
     */
    public String initImageMessage(String toUserName, String fromUserName) throws Exception {
        String message = null;
        Image image = new Image();
        //WeChatUtil的main方法中获取到的MediaId
        image.setMediaId("Txpi8VL8Rvf2oxPNiS6hrg0sYlzWchHwR1JVcTQmW5iUGT0-7n0qh3jP5AZXfUAW");
        ImageMsg imageMessage = new ImageMsg();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setImage(image);
        message = imageMessageToXml(imageMessage);
        return message;
    }

    /**
     * 将自定义的被动回复图片消息对象转化为微信指定的xml
     */
    public static String imageMessageToXml(ImageMsg imageMessage) {
        XStream xstream = new XStream();
        xstream.alias("xml", imageMessage.getClass());
        return xstream.toXML(imageMessage);
    }

    /**
     * 组装被动回复音乐消息
     */
    public static String initMusicMessage(String toUserName, String fromUserName) {
        String message = null;
        Music music = new Music();
        music.setThumbMediaId("WsHCQr1ftJQwmGUGhCP8gZ13a77XVg5Ah_uHPHVEAQuRE5FEjn-DsZJzFZqZFeFk");
        music.setTitle("see you again");
        music.setDescription("速7片尾曲");
        music.setMusicUrl("http://zapper.tunnel.mobi/Weixin/resource/See You Again.mp3");
        music.setHQMusicUrl("http://zapper.tunnel.mobi/Weixin/resource/See You Again.mp3");
        MusicMsg musicMessage = new MusicMsg();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setCreateTime(new Date().getTime());
        musicMessage.setMusic(music);
        message = musicMessageToXml(musicMessage);
        return message;
    }

    /**
     * 将自定义的被动回复音乐消息对象转化为微信指定的xml
     */
    public static String musicMessageToXml(MusicMsg musicMessage) {
        XStream xstream = new XStream();
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

}
