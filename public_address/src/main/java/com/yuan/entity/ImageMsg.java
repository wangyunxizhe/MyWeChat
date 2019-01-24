package com.yuan.entity;

/**
 * 回复图片消息。注意：不是接收，是被动回复接口
 */
public class ImageMsg extends BaseMsg {

    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }
}
