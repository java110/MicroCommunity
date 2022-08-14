package com.java110.dev.cmd.menu;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.menu.MenuDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.menu.ApiMenuDataVo;
import com.java110.vo.api.menu.ApiMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "menu.listMenus")
public class ListMenusCmd extends Cmd {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
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
