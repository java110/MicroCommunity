package com.java110.store.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.store.StoreUserPo;
import com.java110.store.dao.IStoreServiceDao;
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
 * Created by wuxw on 2019/3/27.
 */

@Java110Listener("saveStoreUserListener")
@Transactional
public class SaveStoreUserListener extends AbstractStoreBusinessServiceDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(SaveStoreUserListener.class);

    @Autowired
    IStoreServiceDao storeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER;
    }

    /**
     * 保存物业信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessStore 节点
        if (!data.containsKey(StoreUserPo.class.getSimpleName())) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "没有" + BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER + "节点");
        }


        JSONArray businessStoreUsers = data.getJSONArray(StoreUserPo.class.getSimpleName());
        doBusinessStoreUser(business, businessStoreUsers);
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

        //物业用户
        List<Map> businessStoreUsers = storeServiceDaoImpl.getBusinessStoreUser(info);
        if (businessStoreUsers != null && businessStoreUsers.size() > 0) {
            storeServiceDaoImpl.saveStoreUserInstance(info);
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
        //物业照片
        List<Map> storeUsers = storeServiceDaoImpl.getStoreUser(info);
        if (storeUsers != null && storeUsers.size() > 0) {
            storeServiceDaoImpl.updateStoreUserInstance(paramIn);
        }
    }


    /**
     * 处理 businessStore 节点
     *
     * @param business           总的数据节点
     * @param businessStoreUsers 物业用户节点
     */
    private void doBusinessStoreUser(Business business, JSONArray businessStoreUsers) {


        for (int businessStoreUserIndex = 0; businessStoreUserIndex < businessStoreUsers.size(); businessStoreUserIndex++) {
            JSONObject businessStoreUser = businessStoreUsers.getJSONObject(businessStoreUserIndex);
            Assert.jsonObjectHaveKey(businessStoreUser, "storeId", "businessStoreUser 节点下没有包含 storeId 节点");
            Assert.jsonObjectHaveKey(businessStoreUser, "userId", "businessStoreUser 节点下没有包含 userId 节点");

            if (businessStoreUser.getString("storeUserId").startsWith("-")) {
                String storeUserId = GenerateCodeFactory.getStoreUserId();
                businessStoreUser.put("storeUserId", storeUserId);
            }
            businessStoreUser.put("bId", business.getbId());
            businessStoreUser.put("operate", StatusConstant.OPERATE_ADD);
            //保存物业信息
            storeServiceDaoImpl.saveBusinessStoreUser(businessStoreUser);
        }

    }


    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
