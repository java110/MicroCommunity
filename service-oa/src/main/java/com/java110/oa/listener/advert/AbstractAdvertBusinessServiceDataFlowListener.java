package com.java110.oa.listener.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.Map;

/**
 * 广告信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAdvertBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAdvertBusinessServiceDataFlowListener.class);


    /**
     * 刷新 businessAdvertInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAdvertInfo
     */
    protected void flushBusinessAdvertInfo(Map businessAdvertInfo, String statusCd) {
        businessAdvertInfo.put("newBId", businessAdvertInfo.get("b_id"));
        businessAdvertInfo.put("classify", businessAdvertInfo.get("classify"));
        businessAdvertInfo.put("adName", businessAdvertInfo.get("ad_name"));
        businessAdvertInfo.put("locationTypeCd", businessAdvertInfo.get("location_type_cd"));
        businessAdvertInfo.put("adTypeCd", businessAdvertInfo.get("ad_type_cd"));
        businessAdvertInfo.put("advertId", businessAdvertInfo.get("advert_id"));
        businessAdvertInfo.put("operate", businessAdvertInfo.get("operate"));
        businessAdvertInfo.put("startTime", businessAdvertInfo.get("start_time"));
        businessAdvertInfo.put("state", businessAdvertInfo.get("state"));
        businessAdvertInfo.put("endTime", businessAdvertInfo.get("end_time"));
        businessAdvertInfo.put("communityId", businessAdvertInfo.get("community_id"));
        businessAdvertInfo.put("locationObjId", businessAdvertInfo.get("location_obj_id"));
        businessAdvertInfo.put("seq", businessAdvertInfo.get("seq"));
        businessAdvertInfo.put("viewType", businessAdvertInfo.get("view_type"));
        businessAdvertInfo.put("advertType", businessAdvertInfo.get("advert_type"));
        businessAdvertInfo.put("pageUrl", businessAdvertInfo.get("page_url"));
        businessAdvertInfo.remove("bId");
        businessAdvertInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAdvert 广告信息信息
     */
    protected void autoSaveDelBusinessAdvert(Business business, JSONObject businessAdvert) {
//自动插入DEL

    }


}
