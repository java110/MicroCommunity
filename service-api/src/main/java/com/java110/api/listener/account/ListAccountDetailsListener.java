package com.java110.api.listener.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAccountDetailConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listAccountDetailsListener")
public class ListAccountDetailsListener extends AbstractServiceApiListener {

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAccountDetailConstant.LIST_ACCOUNTDETAILS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAccountDetailInnerServiceSMO getAccountDetailInnerServiceSMOImpl() {
        return accountDetailInnerServiceSMOImpl;
    }

    public void setAccountDetailInnerServiceSMOImpl(IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl) {
        this.accountDetailInnerServiceSMOImpl = accountDetailInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AccountDetailDto accountDetailDto = BeanConvertUtil.covertBean(reqJson, AccountDetailDto.class);

        int count = accountDetailInnerServiceSMOImpl.queryAccountDetailsCount(accountDetailDto);

        List<AccountDetailDto> accountDetailDtos = null;

        if (count > 0) {
            accountDetailDtos = accountDetailInnerServiceSMOImpl.queryAccountDetails(accountDetailDto);
        } else {
            accountDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, accountDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
