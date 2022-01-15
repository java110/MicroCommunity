package com.java110.user.listener.ownerAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.user.dao.IOwnerAttrServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 业主属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerAttrInfoListener")
@Transactional
public class SaveOwnerAttrInfoListener extends AbstractOwnerAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerAttrInfoListener.class);

    @Autowired
    private IOwnerAttrServiceDao ownerAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ATTR_INFO;
    }

    /**
     * 保存业主属性信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerAttr 节点
        if (data.containsKey(OwnerAttrPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerAttrPo.class.getSimpleName());
            JSONArray businessOwnerAttrs = null;
            if (bObj instanceof JSONObject) {
                businessOwnerAttrs = new JSONArray();
                businessOwnerAttrs.add(bObj);
            } else {
                businessOwnerAttrs = (JSONArray) bObj;
            }
            //JSONObject businessOwnerAttr = data.getJSONObject(OwnerAttrPo.class.getSimpleName());
            for (int bOwnerAttrIndex = 0; bOwnerAttrIndex < businessOwnerAttrs.size(); bOwnerAttrIndex++) {
                JSONObject businessOwnerAttr = businessOwnerAttrs.getJSONObject(bOwnerAttrIndex);
                doBusinessOwnerAttr(business, businessOwnerAttr);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessOwnerAttr.getString("attrId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //业主属性信息
        List<Map> businessOwnerAttrInfo = ownerAttrServiceDaoImpl.getBusinessOwnerAttrInfo(info);
        if (businessOwnerAttrInfo != null && businessOwnerAttrInfo.size() > 0) {
            reFreshShareColumn(info, businessOwnerAttrInfo.get(0));
            ownerAttrServiceDaoImpl.saveOwnerAttrInfoInstance(info);
            if (businessOwnerAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessOwnerAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("community_id")) {
            return;
        }

        if (!businessInfo.containsKey("communityId")) {
            return;
        }

        info.put("community_id", businessInfo.get("communityId"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //业主属性信息
        List<Map> ownerAttrInfo = ownerAttrServiceDaoImpl.getOwnerAttrInfo(info);
        if (ownerAttrInfo != null && ownerAttrInfo.size() > 0) {
            reFreshShareColumn(paramIn, ownerAttrInfo.get(0));
            ownerAttrServiceDaoImpl.updateOwnerAttrInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOwnerAttr 节点
     *
     * @param business          总的数据节点
     * @param businessOwnerAttr 业主属性节点
     */
    private void doBusinessOwnerAttr(Business business, JSONObject businessOwnerAttr) {

        Assert.jsonObjectHaveKey(businessOwnerAttr, "attrId", "businessOwnerAttr 节点下没有包含 attrId 节点");

        if (businessOwnerAttr.getString("attrId").startsWith("-")) {
            //刷新缓存
            //flushOwnerAttrId(business.getDatas());

            businessOwnerAttr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessOwnerAttr.put("bId", business.getbId());
        businessOwnerAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存业主属性信息
        ownerAttrServiceDaoImpl.saveBusinessOwnerAttrInfo(businessOwnerAttr);

    }

    @Override
    public IOwnerAttrServiceDao getOwnerAttrServiceDaoImpl() {
        return ownerAttrServiceDaoImpl;
    }

    public void setOwnerAttrServiceDaoImpl(IOwnerAttrServiceDao ownerAttrServiceDaoImpl) {
        this.ownerAttrServiceDaoImpl = ownerAttrServiceDaoImpl;
    }
}
