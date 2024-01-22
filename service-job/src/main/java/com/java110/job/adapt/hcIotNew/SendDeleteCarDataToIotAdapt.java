package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.Business;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
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
 * 同步车辆信息
 */
@Component(value = "sendDeleteCarDataToIotAdapt")
public class SendDeleteCarDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

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
            throw new IllegalArgumentException("车辆不存在");
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(memberId);
        ownerCarDto.setStatusCd("1");
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new IllegalArgumentException("车辆不存在");
        }


        OwnerCarDto tmpOwnerCarDto = ownerCarDtos.get(0);

        JSONObject car = new JSONObject();
        car.put("communityId", ownerCarDtos.get(0).getCommunityId());
        car.put("carMemberId", tmpOwnerCarDto.getMemberId());

        sendIotImpl.post("/iot/api/car.deleteCarApi", car);
    }
}
