package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeUserStorehouseConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询小区侦听类
 */
@Java110Listener("listUserStorehousesListener")
public class ListUserStorehousesListener extends AbstractServiceApiListener {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeUserStorehouseConstant.LIST_USERSTOREHOUSES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IUserStorehouseInnerServiceSMO getUserStorehouseInnerServiceSMOImpl() {
        return userStorehouseInnerServiceSMOImpl;
    }

    public void setUserStorehouseInnerServiceSMOImpl(IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl) {
        this.userStorehouseInnerServiceSMOImpl = userStorehouseInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UserStorehouseDto userStorehouseDto = BeanConvertUtil.covertBean(reqJson, UserStorehouseDto.class);
        //获取用户id
        String userId = reqJson.getString("userId");
        List<Map> privileges = null;
        //查看所有个人物品权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/everythingGoods");
        basePrivilegeDto.setUserId(userId);
        privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges != null && privileges.size() > 0) {
            userStorehouseDto.setUserId(reqJson.getString("searchUserId"));
            userStorehouseDto.setUserName(reqJson.getString("searchUserName"));
        }
        //转增只查询自己的物品
        if(!StringUtil.isEmpty(reqJson.getString("giveType")) && "1".equals(reqJson.getString("giveType"))){
            userStorehouseDto.setUserId(reqJson.getString("userId"));
            userStorehouseDto.setUserName(reqJson.getString("userName"));
        }

        int count = userStorehouseInnerServiceSMOImpl.queryUserStorehousesCount(userStorehouseDto);

        List<UserStorehouseDto> userStorehouseDtos = null;

        if (count > 0) {
            userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        } else {
            Object chooseType = reqJson.get("chooseType");
            if (chooseType != null && !StringUtil.isEmpty(chooseType.toString()) && reqJson.get("chooseType").equals("repair")) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您还没有该类型的物品，请您先申领物品！");
                context.setResponseEntity(responseEntity);
                return;
            }
            userStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, userStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
