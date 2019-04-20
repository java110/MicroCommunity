package com.java110.floor.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.floor.dao.IFloorServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 小区楼信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFloorInfoListener")
@Transactional
public class SaveFloorInfoListener extends AbstractFloorBusinessServiceDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(SaveFloorInfoListener.class);

    @Autowired
    IFloorServiceDao floorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.sdddfff;
    }

    /**
     * 保存小区楼信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessFloor 节点
        if(data.containsKey("businessFloor")){
            JSONObject businessFloor = data.getJSONObject("businessFloor");
            doBusinessFloor(business,businessFloor);
            dataFlowContext.addParamOut("floorId",businessFloor.getString("floorId"));
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //小区楼信息
        Map businessFloorInfo = floorServiceDaoImpl.getBusinessFloorInfo(info);
        if( businessFloorInfo != null && !businessFloorInfo.isEmpty()) {
            floorServiceDaoImpl.saveFloorInfoInstance(info);
            dataFlowContext.addParamOut("floorId",businessFloorInfo.get("floor_id"));
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //小区楼信息
        Map floorInfo = floorServiceDaoImpl.getFloorInfo(info);
        if(floorInfo != null && !floorInfo.isEmpty()){
            paramIn.put("floorId",floorInfo.get("floor_id").toString());
            floorServiceDaoImpl.updateFloorInfoInstance(paramIn);
            dataFlowContext.addParamOut("floorId",floorInfo.get("floor_id"));
        }
    }



    /**
     * 处理 businessFloor 节点
     * @param business 总的数据节点
     * @param businessFloor 小区楼节点
     */
    private void doBusinessFloor(Business business,JSONObject businessFloor){

        Assert.jsonObjectHaveKey(businessFloor,"floorId","businessFloor 节点下没有包含 floorId 节点");

        if(businessFloor.getString("floorId").startsWith("-")){
            //刷新缓存
            flushFloorId(business.getDatas());
        }

        businessFloor.put("bId",business.getbId());
        businessFloor.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区楼信息
        floorServiceDaoImpl.saveBusinessFloorInfo(businessFloor);

    }

    public IFloorServiceDao getFloorServiceDaoImpl() {
        return floorServiceDaoImpl;
    }

    public void setFloorServiceDaoImpl(IFloorServiceDao floorServiceDaoImpl) {
        this.floorServiceDaoImpl = floorServiceDaoImpl;
    }
}
