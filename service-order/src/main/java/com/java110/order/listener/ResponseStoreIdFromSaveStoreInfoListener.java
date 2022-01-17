package com.java110.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.core.annotation.Java110Listener;
import com.java110.entity.order.Business;
import com.java110.core.event.app.order.Ordered;
import com.java110.core.event.center.event.InvokeBusinessBSuccessEvent;
import com.java110.core.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

/**
 *
 * 处理商户服务返回未将storeId 返回出去问题
 * Created by Administrator on 2019/3/30.
 */
@Java110Listener(name = "responseStoreIdFromSaveStoreInfoListener")
public class ResponseStoreIdFromSaveStoreInfoListener implements DataFlowListener<InvokeBusinessBSuccessEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(ResponseStoreIdFromSaveStoreInfoListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void soService(InvokeBusinessBSuccessEvent event) {

        Business business = event.getBusiness();
        if(!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO.equals(business.getBusinessTypeCd())){
            return ;
        }

        JSONObject businessResponseData = event.getBusinessResponseData();

        if(!businessResponseData.containsKey("storeId")) {
            return;
        }

        JSONObject storeInfo = new JSONObject();
        storeInfo.put("storeId",businessResponseData.getString("storeId"));
        event.getDataFlow().getResJson().getJSONArray("msg").add(storeInfo);


    }
}
