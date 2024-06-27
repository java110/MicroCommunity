package com.java110.job.adapt.oa;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.system.Business;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.dto.workType.WorkTypeDto;
import com.java110.intf.oa.IWorkPoolV1InnerServiceSMO;
import com.java110.intf.oa.IWorkTaskV1InnerServiceSMO;
import com.java110.intf.oa.IWorkTypeV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

@Component("oaWorkTaskToStaffAdapt")
public class OaWorkTaskToStaffAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(OaWorkTaskToStaffAdapt.class);

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTypeV1InnerServiceSMO workTypeV1InnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();

        Assert.hasKeyAndValue(data, "taskId", "未包含任务ID");

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setTaskId(data.getString("taskId"));
        List<WorkTaskDto> workTaskDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);

        if (ListUtil.isNull(workTaskDtos)) {
            return;
        }
        workTaskDto = workTaskDtos.get(0);

        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(workTaskDto.getWorkId());
        List<WorkPoolDto> workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }

        WorkTypeDto workTypeDto = new WorkTypeDto();
        workTypeDto.setWtId(workPoolDtos.get(0).getWtId());
        List<WorkTypeDto> workTypeDtos = workTypeV1InnerServiceSMOImpl.queryWorkTypes(workTypeDto);
        if (ListUtil.isNull(workTypeDtos)) {
            return;
        }

        if (WorkTaskDto.STATE_WAIT.equals(workTaskDtos.get(0).getState())) {
            todoStaff(workTaskDto, workPoolDtos.get(0), workTypeDtos.get(0));
        } else if (WorkTaskDto.STATE_COMPLETE.equals(workTaskDtos.get(0).getState())) {
            todoStartStaff(workTaskDto, workPoolDtos.get(0), workTypeDtos.get(0));
        }
    }

    /**
     * 通知
     *
     * @param workTaskDto
     * @param workPoolDto
     * @param workTypeDto
     */
    private void todoStartStaff(WorkTaskDto workTaskDto, WorkPoolDto workPoolDto, WorkTypeDto workTypeDto) {

        //todo 给申请人发消息
        JSONObject content = new JSONObject();
        content.put("flowName", workPoolDto.getWorkName());
        content.put("staffName", "处理完成("+workTaskDto.getStaffName()+")");
        content.put("orderId", workPoolDto.getWorkId());
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);

        if(WorkTypeDto.SMS_WAY_WECHAT.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
            msgNotify.sendOaCreateStaffMsg(workTypeDto.getCommunityId(), workTaskDto.getCreateUserId(), content);
        }else if(WorkTypeDto.SMS_WAY_ALI.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
            msgNotify.sendOaCreateStaffMsg(workTypeDto.getCommunityId(), workTaskDto.getCreateUserId(), content);
        }
    }

    /**
     * 派单或转单
     *
     * @param workTaskDto
     * @param workPoolDto
     * @param workTypeDto
     */
    private void todoStaff(WorkTaskDto workTaskDto, WorkPoolDto workPoolDto, WorkTypeDto workTypeDto) {

        JSONObject content = new JSONObject();
        content.put("flowName", workPoolDto.getWorkName());
        content.put("create_user_name", workTaskDto.getStaffName());
        content.put("create_time", workTaskDto.getCreateTime());
        content.put("date", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        content.put("orderId", workPoolDto.getWorkId());

        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        if(WorkTypeDto.SMS_WAY_WECHAT.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
            msgNotify.sendOaDistributeMsg(workTypeDto.getCommunityId(), workTaskDto.getStaffId(), content);
        }else if(WorkTypeDto.SMS_WAY_ALI.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
            msgNotify.sendOaDistributeMsg(workTypeDto.getCommunityId(), workTaskDto.getStaffId(), content);
        }
    }
}
