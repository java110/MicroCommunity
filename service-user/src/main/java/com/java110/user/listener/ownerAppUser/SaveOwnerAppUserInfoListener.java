package com.java110.user.listener.ownerAppUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.user.dao.IOwnerAppUserServiceDao;
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
 * 保存 绑定业主信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerAppUserInfoListener")
@Transactional
public class SaveOwnerAppUserInfoListener extends AbstractOwnerAppUserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerAppUserInfoListener.class);

    @Autowired
    private IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_APP_USER;
    }

    /**
     * 保存绑定业主信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerAppUser 节点
        if (data.containsKey(OwnerAppUserPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerAppUserPo.class.getSimpleName());
            JSONArray businessOwnerAppUsers = null;
            if (bObj instanceof JSONObject) {
                businessOwnerAppUsers = new JSONArray();
                businessOwnerAppUsers.add(bObj);
            } else {
                businessOwnerAppUsers = (JSONArray) bObj;
            }
            //JSONObject businessOwnerAppUser = data.getJSONObject("businessOwnerAppUser");
            for (int bOwnerAppUserIndex = 0; bOwnerAppUserIndex < businessOwnerAppUsers.size(); bOwnerAppUserIndex++) {
                JSONObject businessOwnerAppUser = businessOwnerAppUsers.getJSONObject(bOwnerAppUserIndex);
                doBusinessOwnerAppUser(business, businessOwnerAppUser);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("appUserId", businessOwnerAppUser.getString("appUserId"));
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

        //绑定业主信息
        List<Map> businessOwnerAppUserInfo = ownerAppUserServiceDaoImpl.getBusinessOwnerAppUserInfo(info);
        if (businessOwnerAppUserInfo != null && businessOwnerAppUserInfo.size() > 0) {
            reFreshShareColumn(info, businessOwnerAppUserInfo.get(0));
            ownerAppUserServiceDaoImpl.saveOwnerAppUserInfoInstance(info);
            if (businessOwnerAppUserInfo.size() == 1) {
                dataFlowContext.addParamOut("appUserId", businessOwnerAppUserInfo.get(0).get("app_user_id"));
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
        //绑定业主信息
        List<Map> ownerAppUserInfo = ownerAppUserServiceDaoImpl.getOwnerAppUserInfo(info);
        if (ownerAppUserInfo != null && ownerAppUserInfo.size() > 0) {
            reFreshShareColumn(paramIn, ownerAppUserInfo.get(0));
            ownerAppUserServiceDaoImpl.updateOwnerAppUserInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessOwnerAppUser 节点
     *
     * @param business             总的数据节点
     * @param businessOwnerAppUser 绑定业主节点
     */
    private void doBusinessOwnerAppUser(Business business, JSONObject businessOwnerAppUser) {

        Assert.jsonObjectHaveKey(businessOwnerAppUser, "appUserId", "businessOwnerAppUser 节点下没有包含 appUserId 节点");

        if (businessOwnerAppUser.getString("appUserId").startsWith("-")) {
            //刷新缓存
            //flushOwnerAppUserId(business.getDatas());

            businessOwnerAppUser.put("appUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));

        }

        businessOwnerAppUser.put("bId", business.getbId());
        businessOwnerAppUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存绑定业主信息
        ownerAppUserServiceDaoImpl.saveBusinessOwnerAppUserInfo(businessOwnerAppUser);

    }

    public IOwnerAppUserServiceDao getOwnerAppUserServiceDaoImpl() {
        return ownerAppUserServiceDaoImpl;
    }

    public void setOwnerAppUserServiceDaoImpl(IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl) {
        this.ownerAppUserServiceDaoImpl = ownerAppUserServiceDaoImpl;
    }
}
