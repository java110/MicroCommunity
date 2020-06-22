package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.wechatMenu.IWechatMenuInnerServiceSMO;
import com.java110.dto.wechatMenu.WechatMenuDto;
import com.java110.utils.constant.ServiceCodeWechatMenuConstant;
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
@Java110Listener("listWechatMenusListener")
public class ListWechatMenusListener extends AbstractServiceApiListener {

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWechatMenuConstant.LIST_WECHATMENUS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IWechatMenuInnerServiceSMO getWechatMenuInnerServiceSMOImpl() {
        return wechatMenuInnerServiceSMOImpl;
    }

    public void setWechatMenuInnerServiceSMOImpl(IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl) {
        this.wechatMenuInnerServiceSMOImpl = wechatMenuInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WechatMenuDto wechatMenuDto = BeanConvertUtil.covertBean(reqJson, WechatMenuDto.class);

        int count = wechatMenuInnerServiceSMOImpl.queryWechatMenusCount(wechatMenuDto);

        List<WechatMenuDto> wechatMenuDtos = null;

        if (count > 0) {
            wechatMenuDtos = wechatMenuInnerServiceSMOImpl.queryWechatMenus(wechatMenuDto);
        } else {
            wechatMenuDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, wechatMenuDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
