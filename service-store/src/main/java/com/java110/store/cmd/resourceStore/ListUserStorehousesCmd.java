package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "resourceStore.listUserStorehouses")
public class ListUserStorehousesCmd extends Cmd {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserStorehouseDto userStorehouseDto = BeanConvertUtil.covertBean(reqJson, UserStorehouseDto.class);
        //获取用户id
        String userId = reqJson.getString("userId");
        if(StringUtil.isEmpty(userId)){
            userId = context.getReqHeaders().get("user-id");
        }
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
        if (!StringUtil.isEmpty(reqJson.getString("giveType")) && "1".equals(reqJson.getString("giveType"))) {
            userStorehouseDto.setUserId(reqJson.getString("userId"));
            userStorehouseDto.setUserName(reqJson.getString("searchUserName"));
        }
        userStorehouseDto.setLagerStockZero("1");

        int count = userStorehouseInnerServiceSMOImpl.queryUserStorehousesCount(userStorehouseDto);

        List<UserStorehouseDto> userStorehouseDtos = new ArrayList<>();

        if (count > 0) {
            if (!StringUtil.isEmpty(reqJson.getString("flag")) && reqJson.getString("flag").equals("1")) {
                List<UserStorehouseDto> userStorehouses = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                for (UserStorehouseDto userStorehouse : userStorehouses) {
                    //获取物品是否是固定物品
                    String isFixed = userStorehouse.getIsFixed();
                    if (!StringUtil.isEmpty(isFixed) && isFixed.equals("Y")) { //Y表示是固定物品;N表示不是固定物品;T表示是通用物品
                        continue;
                    } else {
                        userStorehouseDtos.add(userStorehouse);
                    }
                }
            } else {
                userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            }
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
