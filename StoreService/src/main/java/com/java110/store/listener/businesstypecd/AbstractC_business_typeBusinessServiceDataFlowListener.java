package com.java110.store.listener.businesstypecd;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IC_business_typeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * BusinessType 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractC_business_typeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractC_business_typeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IC_business_typeServiceDao getC_business_typeServiceDaoImpl();

    /**
     * 刷新 businessC_business_typeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessC_business_typeInfo
     */
    protected void flushBusinessC_business_typeInfo(Map businessC_business_typeInfo,String statusCd){
        businessC_business_typeInfo.put("newBId", businessC_business_typeInfo.get("b_id"));
        businessC_business_typeInfo.put("businessTypeCd",businessC_business_typeInfo.get("business_type_cd"));
businessC_business_typeInfo.put("operate",businessC_business_typeInfo.get("operate"));
businessC_business_typeInfo.put("name",businessC_business_typeInfo.get("name"));
businessC_business_typeInfo.put("description",businessC_business_typeInfo.get("description"));
businessC_business_typeInfo.put("id",businessC_business_typeInfo.get("id"));
businessC_business_typeInfo.put("userId",businessC_business_typeInfo.get("user_id"));
businessC_business_typeInfo.remove("bId");
        businessC_business_typeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessC_business_type BusinessType信息
     */
    protected void autoSaveDelBusinessC_business_type(Business business, JSONObject businessC_business_type){
//自动插入DEL
        Map info = new HashMap();
        info.put("id",businessC_business_type.getString("id"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentC_business_typeInfos = getC_business_typeServiceDaoImpl().getC_business_typeInfo(info);
        if(currentC_business_typeInfos == null || currentC_business_typeInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentC_business_typeInfo = currentC_business_typeInfos.get(0);

        currentC_business_typeInfo.put("bId",business.getbId());

        currentC_business_typeInfo.put("businessTypeCd",currentC_business_typeInfo.get("business_type_cd"));
currentC_business_typeInfo.put("operate",currentC_business_typeInfo.get("operate"));
currentC_business_typeInfo.put("name",currentC_business_typeInfo.get("name"));
currentC_business_typeInfo.put("description",currentC_business_typeInfo.get("description"));
currentC_business_typeInfo.put("id",currentC_business_typeInfo.get("id"));
currentC_business_typeInfo.put("userId",currentC_business_typeInfo.get("user_id"));


        currentC_business_typeInfo.put("operate",StatusConstant.OPERATE_DEL);
        getC_business_typeServiceDaoImpl().saveBusinessC_business_typeInfo(currentC_business_typeInfo);
    }





}
