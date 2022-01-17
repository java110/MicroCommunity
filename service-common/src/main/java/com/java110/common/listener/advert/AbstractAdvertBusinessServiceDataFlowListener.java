package com.java110.common.listener.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAdvertBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAdvertBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAdvertServiceDao getAdvertServiceDaoImpl();

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
        Map info = new HashMap();
        info.put("advertId", businessAdvert.getString("advertId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAdvertInfos = getAdvertServiceDaoImpl().getAdvertInfo(info);
        if (currentAdvertInfos == null || currentAdvertInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAdvertInfo = currentAdvertInfos.get(0);

        currentAdvertInfo.put("bId", business.getbId());

        currentAdvertInfo.put("classify", currentAdvertInfo.get("classify"));
        currentAdvertInfo.put("adName", currentAdvertInfo.get("ad_name"));
        currentAdvertInfo.put("locationTypeCd", currentAdvertInfo.get("location_type_cd"));
        currentAdvertInfo.put("adTypeCd", currentAdvertInfo.get("ad_type_cd"));
        currentAdvertInfo.put("advertId", currentAdvertInfo.get("advert_id"));
        currentAdvertInfo.put("operate", currentAdvertInfo.get("operate"));
        currentAdvertInfo.put("startTime", currentAdvertInfo.get("start_time"));
        currentAdvertInfo.put("state", currentAdvertInfo.get("state"));
        currentAdvertInfo.put("endTime", currentAdvertInfo.get("end_time"));
        currentAdvertInfo.put("communityId", currentAdvertInfo.get("community_id"));
        currentAdvertInfo.put("locationObjId", currentAdvertInfo.get("location_obj_id"));
        currentAdvertInfo.put("seq", currentAdvertInfo.get("seq"));
        currentAdvertInfo.put("viewType", currentAdvertInfo.get("view_type"));

        currentAdvertInfo.put("advertType", currentAdvertInfo.get("advert_type"));
        currentAdvertInfo.put("pageUrl", currentAdvertInfo.get("page_url"));


        currentAdvertInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAdvertServiceDaoImpl().saveBusinessAdvertInfo(currentAdvertInfo);
        for(Object key : currentAdvertInfo.keySet()) {
            if(businessAdvert.get(key) == null) {
                businessAdvert.put(key.toString(), currentAdvertInfo.get(key));
            }
        }
    }


}
