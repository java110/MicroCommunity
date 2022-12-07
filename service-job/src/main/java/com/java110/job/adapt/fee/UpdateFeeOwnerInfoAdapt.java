package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.Business;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.fee.asyn.IUpdateFeeOwnerInfo;
import com.java110.po.owner.OwnerPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改业主时 刷新费用信息
 *
 * @ClassName UpdateFeeOwnerInfoAdapt
 * @Description TODO
 * @Author wuxw
 * @Date 2021/10/7 20:50
 * @Version 1.0
 * add by wuxw 2021/10/7
 **/
@Component(value = "updateFeeOwnerInfoAdapt")
public class UpdateFeeOwnerInfoAdapt extends DatabusAdaptImpl {


    @Autowired
    private IUpdateFeeOwnerInfo updateFeeOwnerInfoImpl;

    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        if (data.containsKey(OwnerPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerPo.class.getSimpleName());
            JSONArray businessMachines = null;
            if (bObj instanceof JSONObject) {
                businessMachines = new JSONArray();
                businessMachines.add(bObj);
            } else if (bObj instanceof List) {
                businessMachines = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessMachines = (JSONArray) bObj;
            }
            for (int bOwnerIndex = 0; bOwnerIndex < businessMachines.size(); bOwnerIndex++) {
                JSONObject businessOwner = businessMachines.getJSONObject(bOwnerIndex);
                doOwnerInfo(business, businessOwner);
            }
        }else {
            if (data instanceof JSONObject) {
                doOwnerInfo(business, data);
            }
        }
    }

    private void doOwnerInfo(Business business, JSONObject businessOwner) {

        updateFeeOwnerInfoImpl.doUpdate(business, businessOwner);


    }

}
