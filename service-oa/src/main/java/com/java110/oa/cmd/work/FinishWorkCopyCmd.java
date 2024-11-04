package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserDto;
import com.java110.dto.work.WorkCopyDto;
import com.java110.dto.work.WorkEventDto;
import com.java110.dto.work.WorkPoolDto;
import com.java110.dto.work.WorkTaskItemDto;
import com.java110.intf.oa.IWorkCopyV1InnerServiceSMO;
import com.java110.intf.oa.IWorkEventV1InnerServiceSMO;
import com.java110.intf.oa.IWorkTaskItemV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.workPool.WorkCopyPo;
import com.java110.po.workPool.WorkEventPo;
import com.java110.po.workPool.WorkTaskItemPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "work.finishWorkCopy")
public class FinishWorkCopyCmd extends Cmd {

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskItemV1InnerServiceSMO workTaskItemV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IWorkEventV1InnerServiceSMO workEventV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "copyId", "未包含抄送人");
        Assert.hasKeyAndValue(reqJson, "itemId", "未包含内容ID");
        Assert.hasKeyAndValue(reqJson, "deductionReason", "未包含说明");
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

        WorkTaskItemDto workTaskItemDto = new WorkTaskItemDto();
        workTaskItemDto.setItemId(reqJson.getString("itemId"));
        List<WorkTaskItemDto> workTaskItemDtos = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItems(workTaskItemDto);

        if (ListUtil.isNull(workTaskItemDtos)) {
            throw new CmdException("工作单内容不存在");
        }

        if (!WorkTaskItemDto.STATE_COMPLETE.equals(workTaskItemDtos.get(0).getState())) {
            throw new CmdException("工作单内容未办理完成，抄送不能办理");
        }

        reqJson.put("taskId", workTaskItemDtos.get(0).getTaskId());
        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        String deductionMoney = "0.00";
        if (reqJson.containsKey("deductionMoney") && StringUtil.isEmpty(reqJson.getString("deductionMoney"))) {
            deductionMoney = reqJson.getString("deductionMoney");
        }

        WorkTaskItemPo workTaskItemPo = new WorkTaskItemPo();
        workTaskItemPo.setItemId(reqJson.getString("itemId"));
        workTaskItemPo.setDeductionMoney(deductionMoney);
        workTaskItemPo.setDeductionReason(reqJson.getString("deductionReason"));
        workTaskItemPo.setDeductionPersonId(userDtos.get(0).getUserId());
        workTaskItemPo.setDeductionPersonName(userDtos.get(0).getName());
        workTaskItemPo.setState(WorkTaskItemDto.STATE_COPY_COMPLETE);
        workTaskItemV1InnerServiceSMOImpl.updateWorkTaskItem(workTaskItemPo);

        WorkEventPo workEventPo = new WorkEventPo();
        workEventPo.setWorkId(workTaskItemDtos.get(0).getWorkId());
        workEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        workEventPo.setCommunityId(workTaskItemDtos.get(0).getCommunityId());
        workEventPo.setRemark(reqJson.getString("deductionReason"));
        workEventPo.setStaffId(userId);
        workEventPo.setPreStaffId(userId);
        workEventPo.setPreStaffName(userDtos.get(0).getName());
        workEventPo.setStaffName(userDtos.get(0).getName());
        workEventPo.setStoreId(workTaskItemDtos.get(0).getStoreId());
        workEventPo.setTaskId(workTaskItemDtos.get(0).getTaskId());
        workEventPo.setItemId(reqJson.getString("itemId"));
        workEventPo.setContentId(workTaskItemDtos.get(0).getContentId());
        workEventPo.setEventType(WorkEventDto.EVENT_TYPE_COPY_COMPLETE);

        workEventV1InnerServiceSMOImpl.saveWorkEvent(workEventPo);

        workTaskItemDto = new WorkTaskItemDto();
        workTaskItemDto.setTaskId(reqJson.getString("taskId"));
        workTaskItemDto.setState(WorkTaskItemDto.STATE_COMPLETE);
        int count = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItemsCount(workTaskItemDto);

        if (count > 0) {
            return;
        }

        WorkCopyPo workCopyPo = new WorkCopyPo();
        workCopyPo.setCopyId(reqJson.getString("copyId"));
        workCopyPo.setState(WorkPoolDto.STATE_COMPLETE);
        workCopyPo.setRemark(reqJson.getString("deductionReason"));
        workCopyV1InnerServiceSMOImpl.updateWorkCopy(workCopyPo);
    }
}
