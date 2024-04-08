package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workCopy.WorkCopyDto;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.dto.workPoolContent.WorkPoolContentDto;
import com.java110.dto.workPoolFile.WorkPoolFileDto;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.intf.oa.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询我起草的 工作单
 */
@Java110Cmd(serviceCode = "work.queryStartWork")
public class QueryStartWorkCmd extends Cmd {

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolContentV1InnerServiceSMO workPoolContentV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        String storeId = CmdContextUtils.getStoreId(context);
        reqJson.put("storeId", storeId);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        WorkPoolDto workPoolDto = BeanConvertUtil.covertBean(reqJson, WorkPoolDto.class);

        String userId = CmdContextUtils.getUserId(context);
        workPoolDto.setCreateUserId(userId);

        int count = workPoolV1InnerServiceSMOImpl.queryWorkPoolsCount(workPoolDto);

        List<WorkPoolDto> workPoolDtos = null;

        if (count > 0) {
            workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);
        } else {
            workPoolDtos = new ArrayList<>();
        }

        //todo 查询 处理人 和抄送人
        queryTaskAndCopy(workPoolDtos);

        //todo 查询内容
        queryContentAndFile(workPoolDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workPoolDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 查询内容
     *
     * @param workPoolDtos
     */
    private void queryContentAndFile(List<WorkPoolDto> workPoolDtos) {
        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }

        if (workPoolDtos.size() != 1) {
            return;
        }

        WorkPoolContentDto workPoolContentDto = new WorkPoolContentDto();
        workPoolContentDto.setWorkId(workPoolDtos.get(0).getWorkId());

        List<WorkPoolContentDto> workPoolContentDtos = workPoolContentV1InnerServiceSMOImpl.queryWorkPoolContents(workPoolContentDto);

        if (ListUtil.isNull(workPoolContentDtos)) {
            return;
        }

        workPoolDtos.get(0).setContent(workPoolContentDtos.get(0).getContent());

        WorkPoolFileDto workPoolFileDto = new WorkPoolFileDto();
        workPoolFileDto.setWorkId(workPoolDtos.get(0).getWorkId());
        workPoolFileDto.setFileType(WorkPoolFileDto.FILE_TYPE_START);
        List<WorkPoolFileDto> workPoolFileDtos = workPoolFileV1InnerServiceSMOImpl.queryWorkPoolFiles(workPoolFileDto);

        if (ListUtil.isNull(workPoolFileDtos)) {
            return;
        }

        workPoolDtos.get(0).setPathUrl(workPoolFileDtos.get(0).getPathUrl());
    }

    private void queryTaskAndCopy(List<WorkPoolDto> workPoolDtos) {

        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }
        List<String> workIds = new ArrayList<>();
        for (WorkPoolDto workPoolDto : workPoolDtos) {
            workIds.add(workPoolDto.getWorkId());
        }

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setWorkIds(workIds.toArray(new String[workIds.size()]));
        List<WorkTaskDto> workTaskDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);

        String curStaffName = "";
        for (WorkPoolDto workPoolDto : workPoolDtos) {
            curStaffName = "";
            for (WorkTaskDto tmpWorkTaskDto : workTaskDtos) {
                if (!WorkTaskDto.STATE_WAIT.equals(tmpWorkTaskDto.getState())) {
                    continue;
                }

                if (!workPoolDto.getWorkId().equals(tmpWorkTaskDto.getWorkId())) {
                    continue;
                }

                if (curStaffName.split(",").length > 2) {
                    continue;
                }

                curStaffName += (tmpWorkTaskDto.getStaffName() + ",");
            }

            workPoolDto.setCurStaffName(curStaffName);
        }

        WorkCopyDto workCopyDto = new WorkCopyDto();
        workCopyDto.setWorkIds(workIds.toArray(new String[workIds.size()]));
        List<WorkCopyDto> workCopyDtos = workCopyV1InnerServiceSMOImpl.queryWorkCopys(workCopyDto);

        String curCopyName = "";
        for (WorkPoolDto workPoolDto : workPoolDtos) {
            curCopyName = "";
            for (WorkCopyDto tmpWorkCopyDto : workCopyDtos) {
                if (!WorkTaskDto.STATE_WAIT.equals(tmpWorkCopyDto.getState())) {
                    continue;
                }

                if (!workPoolDto.getWorkId().equals(tmpWorkCopyDto.getWorkId())) {
                    continue;
                }

                if (curCopyName.split(",").length > 2) {
                    continue;
                }

                curCopyName += (tmpWorkCopyDto.getStaffName() + ",");
            }

            workPoolDto.setCurCopyName(curCopyName);
        }

    }
}
