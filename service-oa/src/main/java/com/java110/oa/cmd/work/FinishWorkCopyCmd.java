package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workCopy.WorkCopyDto;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.intf.oa.IWorkCopyV1InnerServiceSMO;
import com.java110.po.workCopy.WorkCopyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "work.finishWorkCopy")
public class FinishWorkCopyCmd extends Cmd {

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "copyId", "未包含抄送人");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含说明");
        String userId = CmdContextUtils.getUserId(context);
        WorkCopyDto workCopyDto = new WorkCopyDto();
        workCopyDto.setCopyId(reqJson.getString("copyId"));
        workCopyDto.setState(WorkPoolDto.STATE_DOING);
        workCopyDto.setStaffId(userId);
        List<WorkCopyDto> workCopyDtos = workCopyV1InnerServiceSMOImpl.queryWorkCopys(workCopyDto);

        if (ListUtil.isNull(workCopyDtos)) {
            throw new CmdException("抄送单不存在");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        WorkCopyPo workCopyPo = new WorkCopyPo();
        workCopyPo.setCopyId(reqJson.getString("copyId"));
        workCopyPo.setState(WorkPoolDto.STATE_COMPLETE);
        workCopyPo.setRemark(reqJson.getString("auditMessage"));
        workCopyV1InnerServiceSMOImpl.updateWorkCopy(workCopyPo);
    }
}
