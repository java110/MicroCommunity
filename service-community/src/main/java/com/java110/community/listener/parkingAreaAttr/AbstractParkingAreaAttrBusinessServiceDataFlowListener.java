package com.java110.community.listener.parkingAreaAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingAreaAttrServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单元属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractParkingAreaAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractParkingAreaAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IParkingAreaAttrServiceDao getParkingAreaAttrServiceDaoImpl();

    /**
     * 刷新 businessParkingAreaAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessParkingAreaAttrInfo
     */
    protected void flushBusinessParkingAreaAttrInfo(Map businessParkingAreaAttrInfo, String statusCd) {
        businessParkingAreaAttrInfo.put("newBId", businessParkingAreaAttrInfo.get("b_id"));
        businessParkingAreaAttrInfo.put("attrId", businessParkingAreaAttrInfo.get("attr_id"));
        businessParkingAreaAttrInfo.put("operate", businessParkingAreaAttrInfo.get("operate"));
        businessParkingAreaAttrInfo.put("paId", businessParkingAreaAttrInfo.get("pa_id"));
        businessParkingAreaAttrInfo.put("specCd", businessParkingAreaAttrInfo.get("spec_cd"));
        businessParkingAreaAttrInfo.put("communityId", businessParkingAreaAttrInfo.get("community_id"));
        businessParkingAreaAttrInfo.put("value", businessParkingAreaAttrInfo.get("value"));
        businessParkingAreaAttrInfo.remove("bId");
        businessParkingAreaAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessParkingAreaAttr 单元属性信息
     */
    protected void autoSaveDelBusinessParkingAreaAttr(Business business, JSONObject businessParkingAreaAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessParkingAreaAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentParkingAreaAttrInfos = getParkingAreaAttrServiceDaoImpl().getParkingAreaAttrInfo(info);
        if (currentParkingAreaAttrInfos == null || currentParkingAreaAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentParkingAreaAttrInfo = currentParkingAreaAttrInfos.get(0);

        currentParkingAreaAttrInfo.put("bId", business.getbId());

        currentParkingAreaAttrInfo.put("attrId", currentParkingAreaAttrInfo.get("attr_id"));
        currentParkingAreaAttrInfo.put("operate", currentParkingAreaAttrInfo.get("operate"));
        currentParkingAreaAttrInfo.put("paId", currentParkingAreaAttrInfo.get("pa_id"));
        currentParkingAreaAttrInfo.put("specCd", currentParkingAreaAttrInfo.get("spec_cd"));
        currentParkingAreaAttrInfo.put("communityId", currentParkingAreaAttrInfo.get("community_id"));
        currentParkingAreaAttrInfo.put("value", currentParkingAreaAttrInfo.get("value"));


        currentParkingAreaAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getParkingAreaAttrServiceDaoImpl().saveBusinessParkingAreaAttrInfo(currentParkingAreaAttrInfo);
        for (Object key : currentParkingAreaAttrInfo.keySet()) {
            if (businessParkingAreaAttr.get(key) == null) {
                businessParkingAreaAttr.put(key.toString(), currentParkingAreaAttrInfo.get(key));
            }
        }
    }


}
