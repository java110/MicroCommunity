package com.java110.vo.api.smallWeChat;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiSmallWeChatVo extends MorePageVo implements Serializable {
    List<ApiSmallWeChatDataVo> smallWeChats;


    public List<ApiSmallWeChatDataVo> getSmallWeChats() {
        return smallWeChats;
    }

    public void setSmallWeChats(List<ApiSmallWeChatDataVo> smallWeChats) {
        this.smallWeChats = smallWeChats;
    }
}
