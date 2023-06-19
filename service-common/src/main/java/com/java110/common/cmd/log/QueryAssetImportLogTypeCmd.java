package com.java110.common.cmd.log;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.log.AssetImportLogTypeDto;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询导入类型 字段
 */
@Java110Cmd(serviceCode = "log.queryAssetImportLogType")
public class QueryAssetImportLogTypeCmd extends Cmd {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "logType", "未包含类型");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        AssetImportLogTypeDto assetImportLogTypeDto = BeanConvertUtil.covertBean(reqJson, AssetImportLogTypeDto.class);
        List<AssetImportLogTypeDto> assetImportLogTypes = assetImportLogInnerServiceSMOImpl.queryAssetImportLogType(assetImportLogTypeDto);

        context.setResponseEntity(ResultVo.createResponseEntity(assetImportLogTypes));
    }
}
