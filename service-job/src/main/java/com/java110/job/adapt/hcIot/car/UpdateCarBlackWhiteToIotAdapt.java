package com.java110.job.adapt.hcIot.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.ICarBlackWhiteInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "updateCarBlackWhiteToIotAdapt")
public class UpdateCarBlackWhiteToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcCarBlackWhiteAsynImpl;

    @Autowired
    private ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessCarBlackWhites = new JSONArray();
        if (data.containsKey(CarBlackWhitePo.class.getSimpleName())) {
            Object bObj = data.get(CarBlackWhitePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessCarBlackWhites.add(bObj);
            } else if (bObj instanceof List) {
                businessCarBlackWhites = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessCarBlackWhites = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessCarBlackWhites.add(data);
            }
        }
        //JSONObject businessCarBlackWhite = data.getJSONObject("businessCarBlackWhite");
        for (int bCarBlackWhiteIndex = 0; bCarBlackWhiteIndex < businessCarBlackWhites.size(); bCarBlackWhiteIndex++) {
            JSONObject businessCarBlackWhite = businessCarBlackWhites.getJSONObject(bCarBlackWhiteIndex);
            doSendCarBlackWhite(business, businessCarBlackWhite);
        }
    }

    private void doSendCarBlackWhite(Business business, JSONObject businessCarBlackWhite) {
        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(businessCarBlackWhite, CarBlackWhitePo.class);
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setCarNum(carBlackWhitePo.getCarNum());
        carBlackWhiteDto.setCommunityId(carBlackWhitePo.getCommunityId());
        List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
        Assert.listOnlyOne(carBlackWhiteDtos, "未找到车辆黑白名单");
        JSONObject postParameters = new JSONObject();
        postParameters.put("carNum", carBlackWhiteDtos.get(0).getCarNum());
        postParameters.put("startTime", carBlackWhiteDtos.get(0).getStartTime());
        postParameters.put("endTime", carBlackWhiteDtos.get(0).getEndTime());
        postParameters.put("extPaId", carBlackWhiteDtos.get(0).getPaId());
        postParameters.put("blackWhite", carBlackWhiteDtos.get(0).getBlackWhite());
        postParameters.put("extBwId", carBlackWhiteDtos.get(0).getBwId());
        postParameters.put("extCommunityId", carBlackWhiteDtos.get(0).getCommunityId());
        hcCarBlackWhiteAsynImpl.updateCarBlackWhite(postParameters);
    }

}
