package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserDto;
import com.java110.dto.workEvent.WorkEventDto;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.dto.workPoolFile.WorkPoolFileDto;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.dto.workTaskItem.WorkTaskItemDto;
import com.java110.intf.oa.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.workEvent.WorkEventPo;
import com.java110.po.workPool.WorkPoolPo;
import com.java110.po.workPoolFile.WorkPoolFilePo;
import com.java110.po.workTask.WorkTaskPo;
import com.java110.po.workTaskItem.WorkTaskItemPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "work.finishWorkTask")
public class FinishWorkTaskCmd extends Cmd {

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskItemV1InnerServiceSMO workTaskItemV1InnerServiceSMOImpl;

    @Autowired
    private IWorkEventV1InnerServiceSMO workEventV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "itemId", "未包含内容");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含说明");

        if ("T".equals(reqJson.getString("auditCode"))) {
            Assert.hasKeyAndValue(reqJson, "taskId", "未包含转单人");
        }

        String userId = CmdContextUtils.getUserId(context);

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setTaskId(reqJson.getString("taskId"));
        workTaskDto.setStaffId(userId);
        List<WorkTaskDto> workTaskDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);

        if (ListUtil.isNull(workTaskDtos)) {
            throw new CmdException("工作单不在你的工位，您无权处理");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = CmdContextUtils.getStoreId(context);

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setTaskId(reqJson.getString("taskId"));
        workTaskDto.setStoreId(storeId);
        List<WorkTaskDto> workTaskDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);


        //todo 转单
        if ("T".equals(reqJson.getString("auditCode"))) {
            doTransfor(reqJson, workTaskDtos.get(0));
        } else if ("C".equals(reqJson.getString("auditCode"))) {
            doFinish(reqJson, workTaskDtos.get(0));
        }

        if (!reqJson.containsKey("pathUrl")) {
            return;
        }

        String pathUrl = reqJson.getString("pathUrl");
        if (StringUtil.isEmpty(pathUrl)) {
            return;
        }

        WorkPoolFilePo workPoolFilePo = new WorkPoolFilePo();
        workPoolFilePo.setCommunityId(workTaskDtos.get(0).getCommunityId());
        workPoolFilePo.setFileType(WorkPoolFileDto.FILE_TYPE_END);
        workPoolFilePo.setFileId(GenerateCodeFactory.getGeneratorId("11"));
        workPoolFilePo.setWorkId(workTaskDtos.get(0).getWorkId());
        workPoolFilePo.setTaskId(workTaskDtos.get(0).getTaskId());
        workPoolFilePo.setPathUrl(reqJson.getString("pathUrl"));
        workPoolFilePo.setStoreId(workTaskDtos.get(0).getStoreId());
        workPoolFilePo.setItemId(reqJson.getString("itemId"));
        workPoolFileV1InnerServiceSMOImpl.saveWorkPoolFile(workPoolFilePo);

    }

    /**
     * 完成工作单
     *
     * @param reqJson
     * @param workTaskDto
     */
    private void doFinish(JSONObject reqJson, WorkTaskDto workTaskDto) {

        Date endTime = DateUtil.getDateFromStringA(workTaskDto.getEndTime());

        String taskTimeout = "N";
        //todo 工单已经超时
        if (endTime.before(DateUtil.getCurrentDate())) {
            taskTimeout = "Y";
        }

        String orderState = WorkPoolDto.STATE_DOING;


        //todo 完成任务

        WorkTaskPo workTaskPo = new WorkTaskPo();
        workTaskPo.setState(orderState);
        workTaskPo.setTaskId(workTaskDto.getTaskId());
        workTaskPo.setStoreId(workTaskDto.getStoreId());
//        workTaskPo.setFinishTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
//        workTaskPo.setTaskTimeout(taskTimeout);
        workTaskV1InnerServiceSMOImpl.updateWorkTask(workTaskPo);

        WorkTaskItemPo workTaskItemPo = new WorkTaskItemPo();
        workTaskItemPo.setItemId(reqJson.getString("itemId"));
        workTaskItemPo.setState(WorkTaskDto.STATE_COMPLETE);
        workTaskItemPo.setTaskId(workTaskDto.getTaskId());
        workTaskItemPo.setFinishTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

        workTaskItemV1InnerServiceSMOImpl.updateWorkTaskItem(workTaskItemPo);

        //todo 查询 工作单
        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(workTaskDto.getWorkId());
        workPoolDto.setStoreId(workTaskDto.getStoreId());
        List<WorkPoolDto> workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        Assert.listOnlyOne(workPoolDtos, "工作单不存在");

        String preStaffId = workPoolDtos.get(0).getCreateUserId();
        String preStaffName = workPoolDtos.get(0).getCreateUserName();

        WorkEventDto workEventDto = new WorkEventDto();
        workEventDto.setStoreId(workTaskDto.getStoreId());
        workEventDto.setTaskId(workTaskDto.getTaskId());
        workEventDto.setWorkId(workTaskDto.getWorkId());
        workEventDto.setOrderByDesc("desc");
        List<WorkEventDto> workEventDtos = workEventV1InnerServiceSMOImpl.queryWorkEvents(workEventDto);
        if (!ListUtil.isNull(workEventDtos)) {
            preStaffId = workEventDtos.get(0).getStaffId();
            preStaffName = workEventDtos.get(0).getStaffName();

        }
        WorkTaskItemDto workTaskItemDto = new WorkTaskItemDto();
        workTaskItemDto.setItemId(reqJson.getString("itemId"));
        workTaskItemDto.setCommunityId(workTaskDto.getCommunityId());
        List<WorkTaskItemDto> workTaskItemDtos = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItems(workTaskItemDto);

        //todo 保存事件
        WorkEventPo workEventPo = new WorkEventPo();
        workEventPo.setWorkId(workTaskDto.getWorkId());
        workEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        workEventPo.setCommunityId(workTaskDto.getCommunityId());
        workEventPo.setRemark(reqJson.getString("auditMessage"));
        workEventPo.setStaffId(workTaskDto.getStaffId());
        workEventPo.setPreStaffId(preStaffId);
        workEventPo.setPreStaffName(preStaffName);
        workEventPo.setStaffName(workTaskDto.getStaffName());
        workEventPo.setStoreId(workTaskDto.getStoreId());
        workEventPo.setTaskId(workTaskDto.getTaskId());
        workEventPo.setItemId(reqJson.getString("itemId"));
        workEventPo.setContentId("-1");
        if (!ListUtil.isNull(workTaskItemDtos)) {
            workEventPo.setContentId(workTaskItemDtos.get(0).getContentId());
        }
        workEventV1InnerServiceSMOImpl.saveWorkEvent(workEventPo);

        // todo 查询 任务明细是否处理完成
        workTaskItemDto = new WorkTaskItemDto();
        workTaskItemDto.setTaskId(workTaskDto.getTaskId());
        workTaskItemDto.setCommunityId(workTaskDto.getCommunityId());
        workTaskItemDto.setState(WorkTaskDto.STATE_WAIT);
        int count = workTaskItemV1InnerServiceSMOImpl.queryWorkTaskItemsCount(workTaskItemDto);
        if (count > 0) {
            WorkPoolPo workPoolPo = new WorkPoolPo();
            workPoolPo.setWorkId(workTaskDto.getWorkId());
            workPoolPo.setState(WorkPoolDto.STATE_DOING);
            workPoolV1InnerServiceSMOImpl.updateWorkPool(workPoolPo);
            return;
        }

        workTaskPo = new WorkTaskPo();
        workTaskPo.setTaskId(workTaskDto.getTaskId());
        workTaskPo.setState(WorkTaskDto.STATE_COMPLETE);
        workTaskPo.setFinishTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        workTaskPo.setTaskTimeout(taskTimeout);
        workTaskV1InnerServiceSMOImpl.updateWorkTask(workTaskPo);

        //todo 查询 工单任务

        WorkTaskDto tmpWorkTaskDto = new WorkTaskDto();
        tmpWorkTaskDto.setWorkId(workTaskDto.getWorkId());
        tmpWorkTaskDto.setStoreId(workTaskDto.getStoreId());
        tmpWorkTaskDto.setStates(new String[]{WorkTaskDto.STATE_WAIT, WorkTaskDto.STATE_DOING});
        int waitCount = workTaskV1InnerServiceSMOImpl.queryWorkTasksCount(tmpWorkTaskDto);
        WorkPoolPo workPoolPo = new WorkPoolPo();
        workPoolPo.setWorkId(workTaskDto.getWorkId());
        if (waitCount > 0) {
            workPoolPo.setState(WorkPoolDto.STATE_DOING);
        } else {
            workPoolPo.setState(WorkPoolDto.STATE_COMPLETE);
        }

        workPoolV1InnerServiceSMOImpl.updateWorkPool(workPoolPo);


    }

    private void doTransfor(JSONObject reqJson, WorkTaskDto workTaskDto) {

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("staffId"));

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        //todo 完成任务

        WorkTaskPo workTaskPo = new WorkTaskPo();
        workTaskPo.setState(WorkPoolDto.STATE_WAIT);
        workTaskPo.setTaskId(workTaskDto.getTaskId());
        workTaskPo.setStoreId(workTaskDto.getStoreId());
        workTaskPo.setStaffId(userDtos.get(0).getUserId());
        workTaskPo.setStaffName(userDtos.get(0).getName());
        workTaskV1InnerServiceSMOImpl.updateWorkTask(workTaskPo);

        //todo 查询 工作单
        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(workTaskDto.getWorkId());
        workPoolDto.setStoreId(workTaskDto.getStoreId());
        List<WorkPoolDto> workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        Assert.listOnlyOne(workPoolDtos, "工作单不存在");

        WorkEventPo workEventPo = new WorkEventPo();
        workEventPo.setWorkId(workTaskDto.getWorkId());
        workEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        workEventPo.setCommunityId(workTaskDto.getCommunityId());
        workEventPo.setRemark(reqJson.getString("auditMessage") + ";转单给" + userDtos.get(0).getName());
        workEventPo.setStaffId(workTaskDto.getStaffId());
        workEventPo.setPreStaffId(workPoolDtos.get(0).getCreateUserId());
        workEventPo.setPreStaffName(workPoolDtos.get(0).getCreateUserName());
        workEventPo.setStaffName(workTaskDto.getStaffName());
        workEventPo.setStoreId(workTaskDto.getStoreId());
        workEventPo.setTaskId(workTaskDto.getTaskId());
        workEventPo.setItemId("-1");
        workEventPo.setContentId("-1");
        workEventV1InnerServiceSMOImpl.saveWorkEvent(workEventPo);
    }
}
