package com.java110.api.listener.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.account.AccountDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAccountConstant;
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
@Java110Listener("listAccountsListener")
public class ListAccountsListener extends AbstractServiceApiListener {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAccountConstant.LIST_ACCOUNTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAccountInnerServiceSMO getAccountInnerServiceSMOImpl() {
        return accountInnerServiceSMOImpl;
    }

    public void setAccountInnerServiceSMOImpl(IAccountInnerServiceSMO accountInnerServiceSMOImpl) {
        this.accountInnerServiceSMOImpl = accountInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AccountDto accountDto = BeanConvertUtil.covertBean(reqJson, AccountDto.class);

        int count = accountInnerServiceSMOImpl.queryAccountsCount(accountDto);

        List<AccountDto> accountDtos = null;

        if (count > 0) {
            accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        } else {
            accountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, accountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
