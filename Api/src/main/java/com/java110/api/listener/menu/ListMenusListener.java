package com.java110.api.listener.menu;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeMenuConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.menu.MenuDto;
import com.java110.dto.menuGroup.MenuGroupDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.menu.ApiMenuDataVo;
import com.java110.vo.api.menu.ApiMenuVo;
import com.java110.vo.api.menuGroup.ApiMenuGroupDataVo;
import com.java110.vo.api.menuGroup.ApiMenuGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询菜单类
 */
@Java110Listener("listMenusListener")
public class ListMenusListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMenuConstant.LIST_MENUS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        MenuDto menuDto = BeanConvertUtil.covertBean(reqJson, MenuDto.class);

        int count = menuInnerServiceSMOImpl.queryMenusCount(menuDto);

        List<ApiMenuDataVo> menus = null;

        if (count > 0) {
            menus = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryMenus(menuDto), ApiMenuDataVo.class);
        } else {
            menus = new ArrayList<>();
        }

        ApiMenuVo apiMenuVo = new ApiMenuVo();

        apiMenuVo.setTotal(count);
        apiMenuVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMenuVo.setMenus(menus);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMenuVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);


    }
}
