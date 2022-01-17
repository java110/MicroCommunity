package com.java110.order.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.event.app.order.Ordered;
import com.java110.core.event.center.event.InvokeBusinessBSuccessEvent;
import com.java110.core.event.center.listener.DataFlowListener;
import com.java110.entity.order.Business;
import com.java110.po.store.StoreUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.List;

/**
 * 刷新 员工的storeId
 * Created by Administrator on 2019/3/30.
 */
@Java110Listener(name = "refreshStaffStoreIdFromSaveStoreInfoListener")
public class RefreshStaffStoreIdFromSaveStoreInfoListener implements DataFlowListener<InvokeBusinessBSuccessEvent>, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(RefreshStaffStoreIdFromSaveStoreInfoListener.class);

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void soService(InvokeBusinessBSuccessEvent event) {

        Business business = event.getBusiness();
        if (!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO.equals(business.getBusinessTypeCd())) {
            return;
        }
        List<Business> businessList = event.getDataFlow().getBusinessList();
        JSONObject businessResponseData = event.getBusinessResponseData();

        if (!businessResponseData.containsKey("storeId")) {
            return;
        }

        String storeId = businessResponseData.getString("storeId");

        for (Business tmpBusiness : businessList) {
            if (!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER.equals(tmpBusiness.getBusinessTypeCd())) {
                continue;
            }

            JSONArray businessStoreUsers = tmpBusiness.getData().getJSONArray(StoreUserPo.class.getSimpleName());
            dealBusinessStoreUserStoreId(businessStoreUsers, storeId);
        }
    }


    /**
     * businessStoreUser 中storeId 为负数的问题
     *
     * @param businessStoreUsers
     */
    private void dealBusinessStoreUserStoreId(JSONArray businessStoreUsers, String storeId) {
        for (int bStoreUserIndex = 0; bStoreUserIndex < businessStoreUsers.size(); bStoreUserIndex++) {
            JSONObject businessStoreUser = businessStoreUsers.getJSONObject(bStoreUserIndex);

            if (!businessStoreUser.containsKey("storeId") || "-1".equals(businessStoreUser.getString("storeId"))) {
                businessStoreUser.put("storeId", storeId);
            }

        }
    }
}
