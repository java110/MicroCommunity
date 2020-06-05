package com.java110.api.listener.carBlackWhite;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.ICarBlackWhiteInnerServiceSMO;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeCarBlackWhiteConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.carBlackWhite.ApiCarBlackWhiteDataVo;
import com.java110.vo.api.carBlackWhite.ApiCarBlackWhiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listCarBlackWhitesListener")
public class ListCarBlackWhitesListener extends AbstractServiceApiListener {

    @Autowired
    private ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeCarBlackWhiteConstant.LIST_CARBLACKWHITES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICarBlackWhiteInnerServiceSMO getCarBlackWhiteInnerServiceSMOImpl() {
        return carBlackWhiteInnerServiceSMOImpl;
    }

    public void setCarBlackWhiteInnerServiceSMOImpl(ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl) {
        this.carBlackWhiteInnerServiceSMOImpl = carBlackWhiteInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        CarBlackWhiteDto carBlackWhiteDto = BeanConvertUtil.covertBean(reqJson, CarBlackWhiteDto.class);

        int count = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);

        List<ApiCarBlackWhiteDataVo> carBlackWhites = null;

        if (count > 0) {
            carBlackWhites = BeanConvertUtil.covertBeanList(carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto), ApiCarBlackWhiteDataVo.class);
        } else {
            carBlackWhites = new ArrayList<>();
        }

        ApiCarBlackWhiteVo apiCarBlackWhiteVo = new ApiCarBlackWhiteVo();

        apiCarBlackWhiteVo.setTotal(count);
        apiCarBlackWhiteVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarBlackWhiteVo.setCarBlackWhites(carBlackWhites);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCarBlackWhiteVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
