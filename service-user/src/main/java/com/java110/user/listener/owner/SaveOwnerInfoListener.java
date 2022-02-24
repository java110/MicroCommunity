package com.java110.user.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerServiceDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 业主信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerInfoListener")
@Transactional
public class SaveOwnerInfoListener extends AbstractOwnerBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerInfoListener.class);

    @Autowired
    private IOwnerServiceDao ownerServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO;
    }

    /**
     * 保存业主信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOwner 节点
        if (data.containsKey(OwnerPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerPo.class.getSimpleName());
            JSONArray businessOwners = null;
            if (bObj instanceof JSONObject) {
                businessOwners = new JSONArray();
                businessOwners.add(bObj);
            } else {
                businessOwners = (JSONArray) bObj;
            }
            //JSONObject businessOwner = data.getJSONObject("businessOwner");
            for (int bOwnerIndex = 0; bOwnerIndex < businessOwners.size(); bOwnerIndex++) {
                JSONObject businessOwner = businessOwners.getJSONObject(bOwnerIndex);
                if(!businessOwner.containsKey("ownerFlag")){
                    businessOwner.put("ownerFlag", OwnerDto.OWNER_FLAG_TRUE);
                }
                doBusinessOwner(business, businessOwner);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("memberId", businessOwner.getString("memberId"));
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

        //业主信息
        List<Map> businessOwnerInfo = ownerServiceDaoImpl.getBusinessOwnerInfo(info);
        if (businessOwnerInfo != null && businessOwnerInfo.size() > 0) {
            ownerServiceDaoImpl.saveOwnerInfoInstance(info);
            if (businessOwnerInfo.size() == 1) {
                dataFlowContext.addParamOut("memberId", businessOwnerInfo.get(0).get("member_id"));
            }
        }
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
        //业主信息
        List<Map> ownerInfo = ownerServiceDaoImpl.getOwnerInfo(info);
        if (ownerInfo != null && ownerInfo.size() > 0) {
            ownerServiceDaoImpl.updateOwnerInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOwner 节点
     *
     * @param business      总的数据节点
     * @param businessOwner 业主节点
     */
    private void doBusinessOwner(Business business, JSONObject businessOwner) {

        Assert.jsonObjectHaveKey(businessOwner, "memberId", "businessOwner 节点下没有包含 memberId 节点");
        Assert.jsonObjectHaveKey(businessOwner, "ownerId", "businessOwner 节点下没有包含 ownerId 节点");

        String tmpMemberId = "";
        if (businessOwner.getString("memberId").startsWith("-")) {
            //刷新缓存
            //flushOwnerId(business.getDatas());

            tmpMemberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);

            businessOwner.put("memberId", tmpMemberId);

        }

        if ("1001".equals(businessOwner.getString("ownerTypeCd"))
                && !StringUtils.isEmpty(tmpMemberId)
                ) {
            businessOwner.put("ownerId", tmpMemberId);

        }

        businessOwner.put("bId", business.getbId());
        businessOwner.put("operate", StatusConstant.OPERATE_ADD);
        //保存业主信息
        ownerServiceDaoImpl.saveBusinessOwnerInfo(businessOwner);

    }

    public IOwnerServiceDao getOwnerServiceDaoImpl() {
        return ownerServiceDaoImpl;
    }

    public void setOwnerServiceDaoImpl(IOwnerServiceDao ownerServiceDaoImpl) {
        this.ownerServiceDaoImpl = ownerServiceDaoImpl;
    }
}
