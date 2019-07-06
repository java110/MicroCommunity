package com.java110.store.listener.businesstypecd;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IC_business_typeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改BusinessType信息 侦听
 *
 * 处理节点
 * 1、businessC_business_type:{} BusinessType基本信息节点
 * 2、businessC_business_typeAttr:[{}] BusinessType属性信息节点
 * 3、businessC_business_typePhoto:[{}] BusinessType照片信息节点
 * 4、businessC_business_typeCerdentials:[{}] BusinessType证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateC_business_typeInfoListener")
@Transactional
public class UpdateC_business_typeInfoListener extends AbstractC_business_typeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateC_business_typeInfoListener.class);
    @Autowired
    private IC_business_typeServiceDao c_business_typeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_BUSINESSTYPE_INFO;
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

        //处理 businessC_business_type 节点
        if(data.containsKey("businessC_business_type")){
            //处理 businessC_business_type 节点
            if(data.containsKey("businessC_business_type")){
                Object _obj = data.get("businessC_business_type");
                JSONArray businessC_business_types = null;
                if(_obj instanceof JSONObject){
                    businessC_business_types = new JSONArray();
                    businessC_business_types.add(_obj);
                }else {
                    businessC_business_types = (JSONArray)_obj;
                }
                //JSONObject businessC_business_type = data.getJSONObject("businessC_business_type");
                for (int _c_business_typeIndex = 0; _c_business_typeIndex < businessC_business_types.size();_c_business_typeIndex++) {
                    JSONObject businessC_business_type = businessC_business_types.getJSONObject(_c_business_typeIndex);
                    doBusinessC_business_type(business, businessC_business_type);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("id", businessC_business_type.getString("id"));
                    }
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

        //BusinessType信息
        List<Map> businessC_business_typeInfos = c_business_typeServiceDaoImpl.getBusinessC_business_typeInfo(info);
        if( businessC_business_typeInfos != null && businessC_business_typeInfos.size() >0) {
            for (int _c_business_typeIndex = 0; _c_business_typeIndex < businessC_business_typeInfos.size();_c_business_typeIndex++) {
                Map businessC_business_typeInfo = businessC_business_typeInfos.get(_c_business_typeIndex);
                flushBusinessC_business_typeInfo(businessC_business_typeInfo,StatusConstant.STATUS_CD_VALID);
                c_business_typeServiceDaoImpl.updateC_business_typeInfoInstance(businessC_business_typeInfo);
                if(businessC_business_typeInfo.size() == 1) {
                    dataFlowContext.addParamOut("id", businessC_business_typeInfo.get("id"));
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
        //BusinessType信息
        List<Map> c_business_typeInfo = c_business_typeServiceDaoImpl.getC_business_typeInfo(info);
        if(c_business_typeInfo != null && c_business_typeInfo.size() > 0){

            //BusinessType信息
            List<Map> businessC_business_typeInfos = c_business_typeServiceDaoImpl.getBusinessC_business_typeInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessC_business_typeInfos == null || businessC_business_typeInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（c_business_type），程序内部异常,请检查！ "+delInfo);
            }
            for (int _c_business_typeIndex = 0; _c_business_typeIndex < businessC_business_typeInfos.size();_c_business_typeIndex++) {
                Map businessC_business_typeInfo = businessC_business_typeInfos.get(_c_business_typeIndex);
                flushBusinessC_business_typeInfo(businessC_business_typeInfo,StatusConstant.STATUS_CD_VALID);
                c_business_typeServiceDaoImpl.updateC_business_typeInfoInstance(businessC_business_typeInfo);
            }
        }

    }



    /**
     * 处理 businessC_business_type 节点
     * @param business 总的数据节点
     * @param businessC_business_type BusinessType节点
     */
    private void doBusinessC_business_type(Business business,JSONObject businessC_business_type){

        Assert.jsonObjectHaveKey(businessC_business_type,"id","businessC_business_type 节点下没有包含 id 节点");

        if(businessC_business_type.getString("id").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"id 错误，不能自动生成（必须已经存在的id）"+businessC_business_type);
        }
        //自动保存DEL
        autoSaveDelBusinessC_business_type(business,businessC_business_type);

        businessC_business_type.put("bId",business.getbId());
        businessC_business_type.put("operate", StatusConstant.OPERATE_ADD);
        //保存BusinessType信息
        c_business_typeServiceDaoImpl.saveBusinessC_business_typeInfo(businessC_business_type);

    }




    public IC_business_typeServiceDao getC_business_typeServiceDaoImpl() {
        return c_business_typeServiceDaoImpl;
    }

    public void setC_business_typeServiceDaoImpl(IC_business_typeServiceDao c_business_typeServiceDaoImpl) {
        this.c_business_typeServiceDaoImpl = c_business_typeServiceDaoImpl;
    }



}
