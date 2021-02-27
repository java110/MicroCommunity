package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName editOwnerCarListener
 * @Description 修改业主车辆信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * @site http://www.homecommunity.cn
 * add by wuxw 2019/4/26
 **/

@Java110Listener("editOwnerCarListener")
public class EditOwnerCarListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(EditOwnerCarListener.class);

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EDIT_OWNER_CAR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(reqJson, "carBrand", "请求报文中未包含carBrand");
        Assert.jsonObjectHaveKey(reqJson, "carType", "请求报文中未包含carType");
        Assert.jsonObjectHaveKey(reqJson, "carColor", "未包含carColor");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        String psId = ownerCarDtos.get(0).getPsId();

        if (StringUtil.isEmpty(psId) || "-1".equals(psId)) {
            throw new IllegalArgumentException("车位已经被释放，不允许修改车辆信息");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(reqJson, OwnerCarPo.class);
        super.update(context, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR);

    }


    @Override
    public int getOrder() {
        return 0;
    }

}
