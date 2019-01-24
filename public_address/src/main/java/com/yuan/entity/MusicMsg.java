package com.yuan.entity;

/**
 * 回复音乐消息。注意：不是接收，是被动回复接口
 */
public class MusicMsg extends BaseMsg {

    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }

}
