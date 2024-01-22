package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.Business;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步业主信息
 */
@Component(value = "sendDeleteOwnerDataToIotAdapt")
public class SendDeleteOwnerDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Autowired
    private ISendIot sendIotImpl;
    @Override
    public void execute(Business business, List<Business> businesses) {

        JSONObject data = business.getData();
        String memberId = data.getString("memberId");
        if (StringUtil.isEmpty(memberId)) {
            throw new IllegalArgumentException("未包含业主信息");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(memberId);
        ownerDto.setStatusCd("1");
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }

        JSONObject car = new JSONObject();
        car.put("communityId", ownerDtos.get(0).getCommunityId());
        car.put("ownerId", ownerDtos.get(0).getOwnerId());
        car.put("memberId", ownerDtos.get(0).getMemberId());


        sendIotImpl.post("/iot/api/owner.deleteOwnerApi", car);

    }
}
