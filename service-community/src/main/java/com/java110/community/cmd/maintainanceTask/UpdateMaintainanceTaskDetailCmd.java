/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.community.cmd.maintainanceTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.maintainance.MaintainanceTaskDto;
import com.java110.dto.maintainance.MaintainanceTaskDetailDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMaintainanceTaskDetailV1InnerServiceSMO;
import com.java110.intf.community.IMaintainanceTaskV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.maintainanceTask.MaintainanceTaskPo;
import com.java110.po.maintainanceTaskDetail.MaintainanceTaskDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：maintainanceTaskDetail.updateMaintainanceTaskDetail
 * 请求路劲：/app/maintainanceTaskDetail.UpdateMaintainanceTaskDetail
 * add by 吴学文 at 2022-11-08 16:02:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "maintainanceTask.updateMaintainanceTaskDetail")
public class UpdateMaintainanceTaskDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMaintainanceTaskDetailCmd.class);


    @Autowired
    private IMaintainanceTaskDetailV1InnerServiceSMO maintainanceTaskDetailV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainanceTaskV1InnerServiceSMO maintainanceTaskV1InnerServiceSMOImpl;





    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskDetailId", "taskDetailId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        if (reqJson.containsKey("photos")) {
            dealPhotos(reqJson);
        }

        //添加单元信息
        reqJson.put("state", "20200407");//巡检点完成
        try {
            updateMaintainanceTaskDetail(reqJson);
        } catch (ParseException e) {
            logger.error("更新明细失败", e);
            throw new CmdException(e.getMessage());
        }

        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setTaskId(reqJson.getString("taskId"));
        maintainanceTaskDto.setCommunityId(reqJson.getString("communityId"));
        maintainanceTaskDto.setState("20200405");
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);

        if (maintainanceTaskDtos != null && maintainanceTaskDtos.size() > 0) {
            reqJson.put("state", "20200406");
            updateMaintainanceTask(reqJson);
        }

        MaintainanceTaskDetailDto maintainanceTaskDetailDto = new MaintainanceTaskDetailDto();
        maintainanceTaskDetailDto.setCommunityId(reqJson.getString("communityId"));
        maintainanceTaskDetailDto.setTaskId(reqJson.getString("taskId"));
        maintainanceTaskDetailDto.setState("20200405");
        int count = maintainanceTaskDetailV1InnerServiceSMOImpl.queryMaintainanceTaskDetailsCount(maintainanceTaskDetailDto);

        if (count > 0) {//说明还没有巡检完
            return;
        }
        reqJson.put("state", "20200407");//巡检完成
        updateMaintainanceTask(reqJson);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }


    private void dealPhotos(JSONObject reqJson) {
        JSONArray photos = reqJson.getJSONArray("photos");
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            Object photo = photos.get(photoIndex);
            reqJson.put("fileName", photo.toString());
            addPhoto(reqJson);
        }
    }

    /**
     * 添加图片
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId("10"));
        businessUnit.put("relTypeCd", "90000");
        businessUnit.put("saveWay", "ftp");
        businessUnit.put("objId", paramInJson.getString("taskDetailId"));
        businessUnit.put("fileRealName", paramInJson.getString("fileName"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if (flag < 1) {
            throw new CmdException("保存图片失败");
        }
    }

    public void updateMaintainanceTaskDetail(JSONObject paramInJson) throws ParseException {
        MaintainanceTaskDetailDto maintainanceTaskDetailDto = new MaintainanceTaskDetailDto();
        maintainanceTaskDetailDto.setTaskDetailId(paramInJson.getString("taskDetailId"));
        maintainanceTaskDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<MaintainanceTaskDetailDto> maintainanceTaskDetailDtos = maintainanceTaskDetailV1InnerServiceSMOImpl.queryMaintainanceTaskDetails(maintainanceTaskDetailDto);
        Assert.listOnlyOne(maintainanceTaskDetailDtos, "未找到需要修改的活动 或多条数据");
        JSONObject businessMaintainanceTaskDetail = new JSONObject();
        businessMaintainanceTaskDetail.putAll(BeanConvertUtil.beanCovertMap(maintainanceTaskDetailDtos.get(0)));
        businessMaintainanceTaskDetail.putAll(paramInJson);
        MaintainanceTaskDetailPo maintainanceTaskDetailPoPo = BeanConvertUtil.covertBean(businessMaintainanceTaskDetail, MaintainanceTaskDetailPo.class);
        maintainanceTaskDetailPoPo.setInspectionTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        maintainanceTaskDetailPoPo.setActUserId(paramInJson.getString("userId"));
        maintainanceTaskDetailPoPo.setActUserName(paramInJson.getString("userName"));
        //获取任务id
        String taskId = paramInJson.getString("taskId");
        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setTaskId(taskId);
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);
        Assert.listOnlyOne(maintainanceTaskDtos, "查询巡检任务数据错误！");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf1.format(date);

        //获取巡检点的巡检点时间限制
        String maintainanceStartTime = "";
        String maintainanceEndTime = "";
        long planStartTime;
        long planFinishTime;
        String pointStartTime = maintainanceTaskDetailDtos.get(0).getPointStartTime();
        String pointEndTime = maintainanceTaskDetailDtos.get(0).getPointEndTime();
        if (!StringUtil.isEmpty(pointStartTime)) {
            maintainanceStartTime =   pointStartTime ;
            Date startTime = format.parse(maintainanceStartTime);
            planStartTime = startTime.getTime();
        } else {
            //获取计划巡检开始时间
            String planInsTime = maintainanceTaskDtos.get(0).getPlanInsTime();
            Date startTime = format.parse(planInsTime);
            planStartTime = startTime.getTime();
        }
        if (!StringUtil.isEmpty(pointEndTime)) {
            maintainanceEndTime =  pointEndTime ;
            Date endTime = format.parse(maintainanceEndTime);
            planFinishTime = endTime.getTime();
        } else {
            //获取计划巡检结束时间
            String planEndTime = maintainanceTaskDtos.get(0).getPlanEndTime();
            Date endTime = format.parse(planEndTime);
            planFinishTime = endTime.getTime();
        }

        //获取计划巡检开始时间
//        String planInsTime = maintainanceTaskDtos.get(0).getPlanInsTime();
//        Date startTime = format.parse(planInsTime);
//        long planStartTime = startTime.getTime();
//        //获取计划巡检结束时间
//        String planEndTime = maintainanceTaskDtos.get(0).getPlanEndTime();
//        Date endTime = format.parse(planEndTime);
//        long planFinishTime = endTime.getTime();
        //获取巡检点签到时间
        String maintainanceTime = maintainanceTaskDetailPoPo.getInspectionTime();
        Date maintainanceDetailTime = format.parse(maintainanceTime);
        long detailTime = maintainanceDetailTime.getTime();
        maintainanceTaskDetailPoPo.setState(MaintainanceTaskDetailDto.STATE_FINISH);

//        if (detailTime < planStartTime) {  //如果巡检点签到时间小于巡检计划开始时间，签到状态就是早到
//            maintainanceTaskDetailPoPo.setState(MaintainanceTaskDetailDto.STATE_FINISH);
//        } else if (detailTime > planFinishTime) {  //如果巡检点签到时间大于巡检计划结束时间，签到状态就是迟到
//            maintainanceTaskDetailPoPo.setState("50000");
//        } else {  //如果巡检点签到时间在巡检计划开始时间和巡检计划结束时间之间，签到状态就是正常
//            maintainanceTaskDetailPoPo.setState("60000");
//        }

        int flag = maintainanceTaskDetailV1InnerServiceSMOImpl.updateMaintainanceTaskDetail(maintainanceTaskDetailPoPo);

        if (flag < 1) {
            throw new CmdException("更新任务明细失败");
        }
        //巡检完成后更改巡检任务表状态
//        MaintainanceTaskDto maintainanceTask= new MaintainanceTaskDto();
//        maintainanceTask.setTaskId(maintainanceTaskDetailPoPo.getTaskId());
//        maintainanceTask.setState(maintainanceTaskDetailPoPo.getState());
//        maintainanceTaskInnerServiceSMOImpl.updateMaintainanceTask(maintainanceTask);
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateMaintainanceTask(JSONObject paramInJson) {

        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setTaskId(paramInJson.getString("taskId"));
        maintainanceTaskDto.setCommunityId(paramInJson.getString("communityId"));
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);

        Assert.listOnlyOne(maintainanceTaskDtos, "未找到需要修改的巡检任务 或多条数据");

        JSONObject businessMaintainanceTask = new JSONObject();
        businessMaintainanceTask.putAll(BeanConvertUtil.beanCovertMap(maintainanceTaskDtos.get(0)));

        MaintainanceTaskPo maintainanceTaskPo = BeanConvertUtil.covertBean(businessMaintainanceTask, MaintainanceTaskPo.class);
        maintainanceTaskPo.setActInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        maintainanceTaskPo.setActUserId(paramInJson.getString("userId"));
        maintainanceTaskPo.setActUserName(paramInJson.getString("userName"));
        maintainanceTaskPo.setState(paramInJson.getString("state"));
        if (!StringUtil.isEmpty(paramInJson.getString("taskType")) && paramInJson.getString("taskType").equals("2000")) {
//            maintainanceTaskPo.setOriginalPlanUserId(maintainanceTaskPo.getPlanUserId());
//            maintainanceTaskPo.setOriginalPlanUserName(maintainanceTaskPo.getPlanUserName());
            maintainanceTaskPo.setPlanUserId(paramInJson.getString("staffId"));
            maintainanceTaskPo.setPlanUserName(paramInJson.getString("staffName"));
            maintainanceTaskPo.setTaskType(paramInJson.getString("taskType"));
            maintainanceTaskPo.setTransferDesc(paramInJson.getString("transferDesc"));
        }
        int flag = maintainanceTaskV1InnerServiceSMOImpl.updateMaintainanceTask(maintainanceTaskPo);
        if (flag < 1) {
            throw new CmdException("修改任务失败");
        }
    }
}
