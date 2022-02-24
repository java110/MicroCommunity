package com.java110.user.listener.ownerCarAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改业主车辆属性信息 侦听
 *
 * 处理节点
 * 1、businessOwnerCarAttr:{} 业主车辆属性基本信息节点
 * 2、businessOwnerCarAttrAttr:[{}] 业主车辆属性属性信息节点
 * 3、businessOwnerCarAttrPhoto:[{}] 业主车辆属性照片信息节点
 * 4、businessOwnerCarAttrCerdentials:[{}] 业主车辆属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOwnerCarAttrInfoListener")
@Transactional
public class UpdateOwnerCarAttrInfoListener extends AbstractOwnerCarAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOwnerCarAttrInfoListener.class);
    @Autowired
    private IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR_ATTR;
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


            //处理 businessOwnerCarAttr 节点
            if(data.containsKey(OwnerCarAttrPo.class.getSimpleName())){
                Object _obj = data.get(OwnerCarAttrPo.class.getSimpleName());
                JSONArray businessOwnerCarAttrs = null;
                if(_obj instanceof JSONObject){
                    businessOwnerCarAttrs = new JSONArray();
                    businessOwnerCarAttrs.add(_obj);
                }else {
                    businessOwnerCarAttrs = (JSONArray)_obj;
                }
                //JSONObject businessOwnerCarAttr = data.getJSONObject(OwnerCarAttrPo.class.getSimpleName());
                for (int _ownerCarAttrIndex = 0; _ownerCarAttrIndex < businessOwnerCarAttrs.size();_ownerCarAttrIndex++) {
                    JSONObject businessOwnerCarAttr = businessOwnerCarAttrs.getJSONObject(_ownerCarAttrIndex);
                    doBusinessOwnerCarAttr(business, businessOwnerCarAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessOwnerCarAttr.getString("attrId"));
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

        //业主车辆属性信息
        List<Map> businessOwnerCarAttrInfos = ownerCarAttrServiceDaoImpl.getBusinessOwnerCarAttrInfo(info);
        if( businessOwnerCarAttrInfos != null && businessOwnerCarAttrInfos.size() >0) {
            for (int _ownerCarAttrIndex = 0; _ownerCarAttrIndex < businessOwnerCarAttrInfos.size();_ownerCarAttrIndex++) {
                Map businessOwnerCarAttrInfo = businessOwnerCarAttrInfos.get(_ownerCarAttrIndex);
                flushBusinessOwnerCarAttrInfo(businessOwnerCarAttrInfo,StatusConstant.STATUS_CD_VALID);
                ownerCarAttrServiceDaoImpl.updateOwnerCarAttrInfoInstance(businessOwnerCarAttrInfo);
                if(businessOwnerCarAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("attrId", businessOwnerCarAttrInfo.get("attr_id"));
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
        //业主车辆属性信息
        List<Map> ownerCarAttrInfo = ownerCarAttrServiceDaoImpl.getOwnerCarAttrInfo(info);
        if(ownerCarAttrInfo != null && ownerCarAttrInfo.size() > 0){

            //业主车辆属性信息
            List<Map> businessOwnerCarAttrInfos = ownerCarAttrServiceDaoImpl.getBusinessOwnerCarAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessOwnerCarAttrInfos == null || businessOwnerCarAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（ownerCarAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _ownerCarAttrIndex = 0; _ownerCarAttrIndex < businessOwnerCarAttrInfos.size();_ownerCarAttrIndex++) {
                Map businessOwnerCarAttrInfo = businessOwnerCarAttrInfos.get(_ownerCarAttrIndex);
                flushBusinessOwnerCarAttrInfo(businessOwnerCarAttrInfo,StatusConstant.STATUS_CD_VALID);
                ownerCarAttrServiceDaoImpl.updateOwnerCarAttrInfoInstance(businessOwnerCarAttrInfo);
            }
        }

    }



    /**
     * 处理 businessOwnerCarAttr 节点
     * @param business 总的数据节点
     * @param businessOwnerCarAttr 业主车辆属性节点
     */
    private void doBusinessOwnerCarAttr(Business business,JSONObject businessOwnerCarAttr){

        Assert.jsonObjectHaveKey(businessOwnerCarAttr,"attrId","businessOwnerCarAttr 节点下没有包含 attrId 节点");

        if(businessOwnerCarAttr.getString("attrId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessOwnerCarAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessOwnerCarAttr(business,businessOwnerCarAttr);

        businessOwnerCarAttr.put("bId",business.getbId());
        businessOwnerCarAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存业主车辆属性信息
        ownerCarAttrServiceDaoImpl.saveBusinessOwnerCarAttrInfo(businessOwnerCarAttr);

    }



    @Override
    public IOwnerCarAttrServiceDao getOwnerCarAttrServiceDaoImpl() {
        return ownerCarAttrServiceDaoImpl;
    }

    public void setOwnerCarAttrServiceDaoImpl(IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl) {
        this.ownerCarAttrServiceDaoImpl = ownerCarAttrServiceDaoImpl;
    }



}
