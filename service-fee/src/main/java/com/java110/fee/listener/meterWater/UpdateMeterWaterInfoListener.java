package com.java110.fee.listener.meterWater;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IMeterWaterServiceDao;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改水电费信息 侦听
 *
 * 处理节点
 * 1、businessMeterWater:{} 水电费基本信息节点
 * 2、businessMeterWaterAttr:[{}] 水电费属性信息节点
 * 3、businessMeterWaterPhoto:[{}] 水电费照片信息节点
 * 4、businessMeterWaterCerdentials:[{}] 水电费证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateMeterWaterInfoListener")
@Transactional
public class UpdateMeterWaterInfoListener extends AbstractMeterWaterBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateMeterWaterInfoListener.class);
    @Autowired
    private IMeterWaterServiceDao meterWaterServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_METER_WATER;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");


            //处理 businessMeterWater 节点
            if(data.containsKey(MeterWaterPo.class.getSimpleName())){
                Object _obj = data.get(MeterWaterPo.class.getSimpleName());
                JSONArray businessMeterWaters = null;
                if(_obj instanceof JSONObject){
                    businessMeterWaters = new JSONArray();
                    businessMeterWaters.add(_obj);
                }else {
                    businessMeterWaters = (JSONArray)_obj;
                }
                //JSONObject businessMeterWater = data.getJSONObject(MeterWaterPo.class.getSimpleName());
                for (int _meterWaterIndex = 0; _meterWaterIndex < businessMeterWaters.size();_meterWaterIndex++) {
                    JSONObject businessMeterWater = businessMeterWaters.getJSONObject(_meterWaterIndex);
                    doBusinessMeterWater(business, businessMeterWater);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("waterId", businessMeterWater.getString("waterId"));
                    }
                }
            }
    }


    /**
     * business to instance 过程
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //水电费信息
        List<Map> businessMeterWaterInfos = meterWaterServiceDaoImpl.getBusinessMeterWaterInfo(info);
        if( businessMeterWaterInfos != null && businessMeterWaterInfos.size() >0) {
            for (int _meterWaterIndex = 0; _meterWaterIndex < businessMeterWaterInfos.size();_meterWaterIndex++) {
                Map businessMeterWaterInfo = businessMeterWaterInfos.get(_meterWaterIndex);
                flushBusinessMeterWaterInfo(businessMeterWaterInfo,StatusConstant.STATUS_CD_VALID);
                meterWaterServiceDaoImpl.updateMeterWaterInfoInstance(businessMeterWaterInfo);
                if(businessMeterWaterInfo.size() == 1) {
                    dataFlowContext.addParamOut("waterId", businessMeterWaterInfo.get("water_id"));
                }
            }
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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //水电费信息
        List<Map> meterWaterInfo = meterWaterServiceDaoImpl.getMeterWaterInfo(info);
        if(meterWaterInfo != null && meterWaterInfo.size() > 0){

            //水电费信息
            List<Map> businessMeterWaterInfos = meterWaterServiceDaoImpl.getBusinessMeterWaterInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessMeterWaterInfos == null || businessMeterWaterInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（meterWater），程序内部异常,请检查！ "+delInfo);
            }
            for (int _meterWaterIndex = 0; _meterWaterIndex < businessMeterWaterInfos.size();_meterWaterIndex++) {
                Map businessMeterWaterInfo = businessMeterWaterInfos.get(_meterWaterIndex);
                flushBusinessMeterWaterInfo(businessMeterWaterInfo,StatusConstant.STATUS_CD_VALID);
                meterWaterServiceDaoImpl.updateMeterWaterInfoInstance(businessMeterWaterInfo);
            }
        }

    }



    /**
     * 处理 businessMeterWater 节点
     * @param business 总的数据节点
     * @param businessMeterWater 水电费节点
     */
    private void doBusinessMeterWater(Business business,JSONObject businessMeterWater){

        Assert.jsonObjectHaveKey(businessMeterWater,"waterId","businessMeterWater 节点下没有包含 waterId 节点");

        if(businessMeterWater.getString("waterId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"waterId 错误，不能自动生成（必须已经存在的waterId）"+businessMeterWater);
        }
        //自动保存DEL
        autoSaveDelBusinessMeterWater(business,businessMeterWater);

        businessMeterWater.put("bId",business.getbId());
        businessMeterWater.put("operate", StatusConstant.OPERATE_ADD);
        //保存水电费信息
        meterWaterServiceDaoImpl.saveBusinessMeterWaterInfo(businessMeterWater);

    }



    @Override
    public IMeterWaterServiceDao getMeterWaterServiceDaoImpl() {
        return meterWaterServiceDaoImpl;
    }

    public void setMeterWaterServiceDaoImpl(IMeterWaterServiceDao meterWaterServiceDaoImpl) {
        this.meterWaterServiceDaoImpl = meterWaterServiceDaoImpl;
    }



}
