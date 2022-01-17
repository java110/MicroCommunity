package com.java110.user.listener.ownerCarAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 业主车辆属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerCarAttrInfoListener")
@Transactional
public class SaveOwnerCarAttrInfoListener extends AbstractOwnerCarAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerCarAttrInfoListener.class);

    @Autowired
    private IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR_ATTR;
    }

    /**
     * 保存业主车辆属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerCarAttr 节点
        if(data.containsKey(OwnerCarAttrPo.class.getSimpleName())){
            Object bObj = data.get(OwnerCarAttrPo.class.getSimpleName());
            JSONArray businessOwnerCarAttrs = null;
            if(bObj instanceof JSONObject){
                businessOwnerCarAttrs = new JSONArray();
                businessOwnerCarAttrs.add(bObj);
            }else {
                businessOwnerCarAttrs = (JSONArray)bObj;
            }
            //JSONObject businessOwnerCarAttr = data.getJSONObject(OwnerCarAttrPo.class.getSimpleName());
            for (int bOwnerCarAttrIndex = 0; bOwnerCarAttrIndex < businessOwnerCarAttrs.size();bOwnerCarAttrIndex++) {
                JSONObject businessOwnerCarAttr = businessOwnerCarAttrs.getJSONObject(bOwnerCarAttrIndex);
                doBusinessOwnerCarAttr(business, businessOwnerCarAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessOwnerCarAttr.getString("attrId"));
                }
            }
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

        //业主车辆属性信息
        List<Map> businessOwnerCarAttrInfo = ownerCarAttrServiceDaoImpl.getBusinessOwnerCarAttrInfo(info);
        if( businessOwnerCarAttrInfo != null && businessOwnerCarAttrInfo.size() >0) {
            reFreshShareColumn(info, businessOwnerCarAttrInfo.get(0));
            ownerCarAttrServiceDaoImpl.saveOwnerCarAttrInfoInstance(info);
            if(businessOwnerCarAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessOwnerCarAttrInfo.get(0).get("attr_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
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
        //业主车辆属性信息
        List<Map> ownerCarAttrInfo = ownerCarAttrServiceDaoImpl.getOwnerCarAttrInfo(info);
        if(ownerCarAttrInfo != null && ownerCarAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, ownerCarAttrInfo.get(0));
            ownerCarAttrServiceDaoImpl.updateOwnerCarAttrInfoInstance(paramIn);
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
            //刷新缓存
            //flushOwnerCarAttrId(business.getDatas());

            businessOwnerCarAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

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
