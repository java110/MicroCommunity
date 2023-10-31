package com.java110.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.app.AppDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.IAppInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "admin.queryAppServiceData")
public class QueryAppServiceDataCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_DEV);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        AppDto appDto = new AppDto();
        List<AppDto> appDtos = appInnerServiceSMOImpl.queryAppsServiceData(appDto);

        context.setResponseEntity(ResultVo.createResponseEntity(appDtos));
    }
}
