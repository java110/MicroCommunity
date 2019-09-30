package com.java110.fee.listener.config;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.fee.dao.IFeeConfigServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 费用配置 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFeeConfigBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractFeeConfigBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IFeeConfigServiceDao getFeeConfigServiceDaoImpl();

    /**
     * 刷新 businessFeeConfigInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessFeeConfigInfo
     */
    protected void flushBusinessFeeConfigInfo(Map businessFeeConfigInfo,String statusCd){
        businessFeeConfigInfo.put("newBId", businessFeeConfigInfo.get("b_id"));
        businessFeeConfigInfo.put("squarePrice",businessFeeConfigInfo.get("square_price"));
businessFeeConfigInfo.put("operate",businessFeeConfigInfo.get("operate"));
businessFeeConfigInfo.put("feeTypeCd",businessFeeConfigInfo.get("fee_type_cd"));
businessFeeConfigInfo.put("configId",businessFeeConfigInfo.get("config_id"));
businessFeeConfigInfo.put("additionalAmount",businessFeeConfigInfo.get("additional_amount"));
businessFeeConfigInfo.put("communityId",businessFeeConfigInfo.get("community_id"));
businessFeeConfigInfo.remove("bId");
        businessFeeConfigInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessFeeConfig 费用配置信息
     */
    protected void autoSaveDelBusinessFeeConfig(Business business, JSONObject businessFeeConfig){
//自动插入DEL
        Map info = new HashMap();
        info.put("configId",businessFeeConfig.getString("configId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentFeeConfigInfos = getFeeConfigServiceDaoImpl().getFeeConfigInfo(info);
        if(currentFeeConfigInfos == null || currentFeeConfigInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentFeeConfigInfo = currentFeeConfigInfos.get(0);

        currentFeeConfigInfo.put("bId",business.getbId());

        currentFeeConfigInfo.put("squarePrice",currentFeeConfigInfo.get("square_price"));
currentFeeConfigInfo.put("operate",currentFeeConfigInfo.get("operate"));
currentFeeConfigInfo.put("feeTypeCd",currentFeeConfigInfo.get("fee_type_cd"));
currentFeeConfigInfo.put("configId",currentFeeConfigInfo.get("config_id"));
currentFeeConfigInfo.put("additionalAmount",currentFeeConfigInfo.get("additional_amount"));
currentFeeConfigInfo.put("communityId",currentFeeConfigInfo.get("community_id"));


        currentFeeConfigInfo.put("operate",StatusConstant.OPERATE_DEL);
        getFeeConfigServiceDaoImpl().saveBusinessFeeConfigInfo(currentFeeConfigInfo);
    }





}
