package com.yuan.entity;

/**
 * 所有消息对象的父类（属性参考微信开发者文档）
 */
public class BaseMsg {

    private String ToUserName;//开发者微信号
    private String FromUserName;//发送方帐号（一个OpenID）
    private Long CreateTime;//消息创建时间 （整型）
    private String MsgType;//消息类型
    private String MsgId;//消息id，64位整型

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
