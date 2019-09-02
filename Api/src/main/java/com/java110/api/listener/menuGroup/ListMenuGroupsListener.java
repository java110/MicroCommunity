package com.java110.api.listener.menuGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeMenuGroupConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.menuGroup.MenuGroupDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.menuGroup.ApiMenuGroupDataVo;
import com.java110.vo.api.menuGroup.ApiMenuGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMenuGroupsListener")
public class ListMenuGroupsListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMenuGroupConstant.LIST_MENUGROUPS;
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
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);

        int count = menuInnerServiceSMOImpl.queryMenuGroupsCount(menuGroupDto);

        List<ApiMenuGroupDataVo> menuGroups = null;

        if (count > 0) {
            menuGroups = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryMenuGroups(menuGroupDto), ApiMenuGroupDataVo.class);
        } else {
            menuGroups = new ArrayList<>();
        }

        ApiMenuGroupVo apiMenuGroupVo = new ApiMenuGroupVo();

        apiMenuGroupVo.setTotal(count);
        apiMenuGroupVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMenuGroupVo.setMenuGroups(menuGroups);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMenuGroupVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }
}
