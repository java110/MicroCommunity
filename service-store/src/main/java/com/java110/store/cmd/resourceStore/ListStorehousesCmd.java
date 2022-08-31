package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IStorehouseInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "resourceStore.listStorehouses")
public class ListStorehousesCmd extends Cmd {

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        StorehouseDto storehouseDto = BeanConvertUtil.covertBean(reqJson, StorehouseDto.class);

        //是否具有查看集团仓库权限
        String userId = reqJson.getString("userId");
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewGroupWarehouse");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            storehouseDto.setShObjIds(new String[]{reqJson.getString("communityId")});
        } else {
            if (reqJson.containsKey("operationType") && reqJson.getString("operationType").equals("1000")) {
                storehouseDto.setShType("");
            }
            storehouseDto.setShObjIds(new String[]{reqJson.getString("communityId"), reqJson.getString("storeId")});
        }

        BasePrivilegeDto basePrivilegeDto1 = new BasePrivilegeDto();
        basePrivilegeDto1.setResource("/viewHiddenWarehouse");
        basePrivilegeDto1.setUserId(userId);
        List<Map> viewHiddenWarehousePrivileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto1);
        if (viewHiddenWarehousePrivileges.size() == 0) {
            storehouseDto.setIsShow("true");
        }
        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("0")) {//调拨申请查看所有仓库
            storehouseDto.setShObjIds(null);
        }
        int count = storehouseInnerServiceSMOImpl.queryStorehousesCount(storehouseDto);

        List<StorehouseDto> storehouseDtos = null;

        if (count > 0) {
            storehouseDtos = storehouseInnerServiceSMOImpl.queryStorehouses(storehouseDto);
        } else {
            storehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, storehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
