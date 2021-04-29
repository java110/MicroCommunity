package com.java110.api.listener.fastuser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IFastuserInnerServiceSMO;
import com.java110.dto.fastuser.FastuserDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.fastuser.ApiFastuserDataVo;
import com.java110.vo.api.fastuser.ApiFastuserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listFastUserListener")
public class ListFastUserListener extends AbstractServiceApiListener {

    @Autowired
    private IFastuserInnerServiceSMO fastuserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.LIST_FASTUSER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FastuserDto fastuserDto = BeanConvertUtil.covertBean(reqJson, FastuserDto.class);

        int count = fastuserInnerServiceSMOImpl.queryFastuserCount(fastuserDto);

        List<ApiFastuserDataVo> fastuserDataVos = null;

        if (count > 0) {
            fastuserDataVos = BeanConvertUtil.covertBeanList(fastuserInnerServiceSMOImpl.queryFastuser(fastuserDto), ApiFastuserDataVo.class);
        } else {
            fastuserDataVos = new ArrayList<>();
        }

        ApiFastuserVo apiFastuserVo = new ApiFastuserVo();

        apiFastuserVo.setTotal(count);
        apiFastuserVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiFastuserVo.setFastuserDataVos(fastuserDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFastuserVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IFastuserInnerServiceSMO getFastuserInnerServiceSMOImpl() {
        return fastuserInnerServiceSMOImpl;
    }

    public void setFastuserInnerServiceSMOImpl(IFastuserInnerServiceSMO fastuserInnerServiceSMOImpl) {
        this.fastuserInnerServiceSMOImpl = fastuserInnerServiceSMOImpl;
    }
}
