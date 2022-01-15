
package com.java110.user.listener.ownerRoomRel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.user.dao.IOwnerRoomRelServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 业主房屋信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerRoomRelInfoListener")
@Transactional
public class SaveOwnerRoomRelInfoListener extends AbstractOwnerRoomRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerRoomRelInfoListener.class);

    @Autowired
    private IOwnerRoomRelServiceDao ownerRoomRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ROOM_REL;
    }

    /**
     * 保存业主房屋信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerRoomRel 节点
        if (data.containsKey(OwnerRoomRelPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerRoomRelPo.class.getSimpleName());
            JSONArray businessOwnerRoomRels = null;
            if (bObj instanceof JSONObject) {
                businessOwnerRoomRels = new JSONArray();
                businessOwnerRoomRels.add(bObj);
            } else {
                businessOwnerRoomRels = (JSONArray) bObj;
            }
            //JSONObject businessOwnerRoomRel = data.getJSONObject("businessOwnerRoomRel");
            for (int bOwnerRoomRelIndex = 0; bOwnerRoomRelIndex < businessOwnerRoomRels.size(); bOwnerRoomRelIndex++) {
                JSONObject businessOwnerRoomRel = businessOwnerRoomRels.getJSONObject(bOwnerRoomRelIndex);
                doBusinessOwnerRoomRel(business, businessOwnerRoomRel);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rel_id", businessOwnerRoomRel.getString("rel_id"));
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

        //业主房屋信息
        List<Map> businessOwnerRoomRelInfo = ownerRoomRelServiceDaoImpl.getBusinessOwnerRoomRelInfo(info);
        if (businessOwnerRoomRelInfo != null && businessOwnerRoomRelInfo.size() > 0) {
            // reFresh(info, businessOwnerRoomRelInfo.get(0));
            ownerRoomRelServiceDaoImpl.saveOwnerRoomRelInfoInstance(info);
            if (businessOwnerRoomRelInfo.size() == 1) {
                dataFlowContext.addParamOut("relId", businessOwnerRoomRelInfo.get(0).get("rel_id"));
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

        if (info.containsKey("ownerId")) {
            return;
        }

        if (!businessInfo.containsKey("owner_id")) {
            return;
        }

        info.put("ownerId", businessInfo.get("owner_id"));
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
        //业主房屋信息
        List<Map> ownerRoomRelInfo = ownerRoomRelServiceDaoImpl.getOwnerRoomRelInfo(info);
        if (ownerRoomRelInfo != null && ownerRoomRelInfo.size() > 0) {
            reFreshShareColumn(paramIn, ownerRoomRelInfo.get(0));
            ownerRoomRelServiceDaoImpl.updateOwnerRoomRelInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOwnerRoomRel 节点
     *
     * @param business             总的数据节点
     * @param businessOwnerRoomRel 业主房屋节点
     */
    private void doBusinessOwnerRoomRel(Business business, JSONObject businessOwnerRoomRel) {

        Assert.jsonObjectHaveKey(businessOwnerRoomRel, "relId", "businessOwnerRoomRel 节点下没有包含 relId 节点");

        if (businessOwnerRoomRel.getString("relId").startsWith("-")) {
            //刷新缓存
            //flushOwnerRoomRelId(business.getDatas());

            businessOwnerRoomRel.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerRoomRelId));

        }

        businessOwnerRoomRel.put("bId", business.getbId());
        businessOwnerRoomRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存业主房屋信息
        ownerRoomRelServiceDaoImpl.saveBusinessOwnerRoomRelInfo(businessOwnerRoomRel);

    }

    public IOwnerRoomRelServiceDao getOwnerRoomRelServiceDaoImpl() {
        return ownerRoomRelServiceDaoImpl;
    }

    public void setOwnerRoomRelServiceDaoImpl(IOwnerRoomRelServiceDao ownerRoomRelServiceDaoImpl) {
        this.ownerRoomRelServiceDaoImpl = ownerRoomRelServiceDaoImpl;
    }
}
