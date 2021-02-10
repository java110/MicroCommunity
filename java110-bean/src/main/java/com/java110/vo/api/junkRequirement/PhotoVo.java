package com.java110.vo.api.junkRequirement;

import java.io.Serializable;

/**
 * @ClassName Photo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/4/20 20:31
 * @Version 1.0
 * add by wuxw 2020/4/20
 **/
public class PhotoVo implements Serializable {
    private String url;

    private String relTypeCd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelTypeCd() {
        return relTypeCd;
    }

    public void setRelTypeCd(String relTypeCd) {
        this.relTypeCd = relTypeCd;
    }
}
