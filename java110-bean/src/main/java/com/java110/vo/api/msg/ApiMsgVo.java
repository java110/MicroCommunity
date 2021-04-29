package com.java110.vo.api.msg;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMsgVo extends MorePageVo implements Serializable {
    List<ApiMsgDataVo> msgs;

    public List<ApiMsgDataVo> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<ApiMsgDataVo> msgs) {
        this.msgs = msgs;
    }
}
