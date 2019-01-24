package com.yuan.entity;

/**
 * 微信文本消息对象（属性参考微信开发者文档中接受普通消息==文本消息中的属性）
 */
public class TextMsg extends BaseMsg {

    private String Content;//消息内容

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
