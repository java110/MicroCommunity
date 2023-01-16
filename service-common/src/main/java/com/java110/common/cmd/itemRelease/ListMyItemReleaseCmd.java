package com.java110.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.itemRelease.ItemReleaseDto;
import com.java110.intf.common.IItemReleaseResV1InnerServiceSMO;
import com.java110.intf.common.IItemReleaseV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Java110Cmd(serviceCode = "itemRelease.listMyItemRelease")
public class ListMyItemReleaseCmd extends Cmd {

    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Autowired
    private IItemReleaseResV1InnerServiceSMO itemReleaseResV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        ItemReleaseDto itemReleaseDto = BeanConvertUtil.covertBean(reqJson, ItemReleaseDto.class);
        itemReleaseDto.setCreateUserId(userId);

        int count = itemReleaseV1InnerServiceSMOImpl.queryItemReleasesCount(itemReleaseDto);

        List<ItemReleaseDto> itemReleaseDtos = null;

        if (count > 0) {
            itemReleaseDtos = itemReleaseV1InnerServiceSMOImpl.queryItemReleases(itemReleaseDto);

        } else {
            itemReleaseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, itemReleaseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
