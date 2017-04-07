package com.java110.entity.protocol;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 报文体信息
 * Created by wuxw on 2017/2/27.
 */
public class SvcCont {

    List<JSONObject> objs;

    public List<JSONObject> getObjs() {
        return objs;
    }

    public void setObjs(List<JSONObject> objs) {
        this.objs = objs;
    }
}
