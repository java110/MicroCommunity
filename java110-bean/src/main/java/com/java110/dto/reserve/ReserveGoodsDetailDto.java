package com.java110.dto.reserve;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预约商品详情数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReserveGoodsDetailDto extends PageDto implements Serializable {

    private String goodsId;
private String detailId;
private String communityId;
private String content;


    private Date createTime;

    private String statusCd = "0";


    public String getGoodsId() {
        return goodsId;
    }
public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getContent() {
        return content;
    }
public void setContent(String content) {
        this.content = content;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
