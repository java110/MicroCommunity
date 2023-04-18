package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.*;
import com.java110.intf.community.*;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.po.owner.VisitPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.*;

/**
 * 访客登记
 *
 * @author fqz
 * @date 2023-02-27 10:23
 */
@Java110Cmd(serviceCode = "visit.saveVisit")
public class SaveVisitCmd extends Cmd {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    //键
    public static final String IS_NEED_REVIEW = "IS_NEED_REVIEW";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "visitGender", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(reqJson, "visitTime", "必填，请填写访客拜访时间");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");
        reqJson.put("vId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vId));
        //随行人数
        if (StringUtil.isEmpty(reqJson.getString("entourage"))) {
            reqJson.put("entourage", "0");
        }
        reqJson.put("recordState", "0");
        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        visitPo.setState(VisitDto.STATE_W);
        visitPo.setUserId(userId);
        int flag = visitV1InnerServiceSMOImpl.saveVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("保存访客失败");
        }
        photoSMOImpl.savePhoto(reqJson, reqJson.getString("vId"), reqJson.getString("communityId"));
        // 访客记录是否需要审核
        if (hasAuditVisit(visitPo, reqJson, storeId, userId)) {
            return; // 需要审核结束，审核时处理 相应 送图片 和车牌数据
        }
        //查询访客配置信息
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        //有车辆信息的情况下判断车辆是否需要审核
        if (reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) { //有车辆
            if (visitSettingDtos != null && visitSettingDtos.size() == 1 && visitSettingDtos.get(0).getCarNumWay().equals("Y")) { //车辆同步 Y 是 N 否
                //获取预约车辆停车场ID、预约车辆免费时长、预约车限制次数、预约车是否审核
                JSONObject visitJson = getVisitCarOperate(reqJson);
                //车辆是否需要审核
//            String isNeedReview = CommunitySettingFactory.getValue(reqJson.getString("communityId"), IS_NEED_REVIEW); //获取小区配置里车辆是否需要审核的值
                String isNeedReview = visitJson.getString("isNeedReview"); //获取车辆是否需要审核的值
                if (!StringUtil.isEmpty(isNeedReview) && isNeedReview.equals("0")) { //0表示需要审核  1表示不需要审核
                    visitPo = new VisitPo();
                    visitPo.setCarState(VisitDto.CAR_STATE_W); //车辆状态为待审核状态
                    visitPo.setState(VisitDto.STATE_C); //访客记录为审核通过状态
                    visitPo.setvId(reqJson.getString("vId"));
                    visitPo.setCommunityId(reqJson.getString("communityId"));
                    flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                    if (flag < 1) {
                        throw new CmdException("修改访客状态失败");
                    }
                } else { //其他情况默认为车辆审核通过
                    visitPo = new VisitPo();
                    visitPo.setState(VisitDto.STATE_C); //访客记录状态变为审核通过
                    visitPo.setvId(reqJson.getString("vId"));
                    visitPo.setCommunityId(reqJson.getString("communityId"));
                    JSONObject param = dealVisitorRegistrationTimes(visitJson); //判断是否超过访客登记次数
                    if (param.containsKey("specifiedTimes") && !StringUtil.isEmpty(param.getString("specifiedTimes")) && param.getString("specifiedTimes").equals("true")) { //超过车辆登记次数
                        visitPo.setStateRemark("访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法审核！");
                        visitPo.setCarStateRemark("访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法审核！");
                        visitPo.setCarState(VisitDto.CAR_STATE_F); //车辆状态变为审核拒绝
                        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法自动审核！");
                        context.setResponseEntity(responseEntity);
                        flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                        if (flag < 1) {
                            throw new CmdException("保存访客失败");
                        }
                    } else { //未超过车辆登记次数
                        //获取车位
                        JSONObject paramJson = dealParkingSpace(visitJson);
                        if (paramJson.containsKey("freeSpace") && !StringUtil.isEmpty(paramJson.getString("freeSpace")) && paramJson.getString("freeSpace").equals("true")) { //无空闲车位
                            visitPo.setStateRemark("访客信息登记成功,当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。");
                            visitPo.setCarStateRemark("访客信息登记成功,当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。");
                            visitPo.setCarState(VisitDto.CAR_STATE_F); //车辆状态为审核拒绝状态
                            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。");
                            context.setResponseEntity(responseEntity);
                            flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                            if (flag < 1) {
                                throw new CmdException("保存访客失败");
                            }
                        } else { //有空闲车位
                            visitPo.setPsId(paramJson.getString("psId"));
                            //处理预约车免费时长
                            String freeTime = dealVisitCarFreeTime(visitJson);
                            reqJson.put("freeTime", freeTime);
                            visitPo.setFreeTime(freeTime); //预约车免费时长
                            visitPo.setCarState(VisitDto.CAR_STATE_C); //车辆状态为审核通过状态
                            flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                            if (flag < 1) {
                                throw new CmdException("修改访客状态失败");
                            }
                            //修改车位状态
                            modifyParkingSpaceSate(paramJson);
                        }
                    }
                }
            } else if (visitSettingDtos != null && visitSettingDtos.size() == 1 && visitSettingDtos.get(0).getCarNumWay().equals("N")) { //车辆不同步
                visitPo = new VisitPo();
                visitPo.setState(VisitDto.STATE_C);
                visitPo.setCarState(VisitDto.CAR_STATE_C); //车辆审核通过
                visitPo.setCarStateRemark("车辆不同步！");
                visitPo.setvId(reqJson.getString("vId"));
                visitPo.setCommunityId(reqJson.getString("communityId"));
                flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                if (flag < 1) {
                    throw new CmdException("修改访客状态失败");
                }
            }
        } else { //无车辆
            visitPo = new VisitPo();
            visitPo.setState(VisitDto.STATE_C);
            visitPo.setvId(reqJson.getString("vId"));
            visitPo.setCommunityId(reqJson.getString("communityId"));
            flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
            if (flag < 1) {
                throw new CmdException("修改访客状态失败");
            }
        }
    }

    /**
     * 是否需要审核
     *
     * @param visitPo
     * @param reqJson
     */
    private boolean hasAuditVisit(VisitPo visitPo, JSONObject reqJson, String storeId, String userId) {
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos == null || visitSettingDtos.size() < 1) {
            return false;
        }
        // 需要审核
        if (!VisitSettingDto.AUDIT_WAY_YES.equals(visitSettingDtos.get(0).getAuditWay())) {
            return false;
        }
        //触发 审批流程
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(storeId);
        oaWorkflowDto.setFlowId(visitSettingDtos.get(0).getFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }
        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }
        //启动任务
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId", userId);
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id", visitPo.getvId());
        flowJson.put("auditMessage", "提交审核");
        flowJson.put("storeId", storeId);
        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        JSONObject result = oaWorkflowActivitiInnerServiceSMOImpl.startProcess(flowJson);
        //提交者提交
        flowJson = new JSONObject();
        flowJson.put("processInstanceId", result.getString("processInstanceId"));
        flowJson.put("createUserId", userId);
        flowJson.put("nextUserId", nextAuditStaff(storeId, visitSettingDtos.get(0).getFlowId())); // 这里要求流程 下一处理人必须要指定
        flowJson.put("storeId", storeId);
        flowJson.put("id", visitPo.getvId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);
        visitPo = new VisitPo();
        visitPo.setState(VisitDto.STATE_D);
        visitPo.setvId(reqJson.getString("vId"));
        visitPo.setCommunityId(reqJson.getString("communityId"));
        if (reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            visitPo.setCarState(VisitDto.CAR_STATE_D);
        }
        int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("修改访客状态失败");
        }
        return true;
    }

    private String nextAuditStaff(String storeId, String flowId) {
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setFlowId(flowId);
        oaWorkflowDto.setStoreId(storeId);
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        OaWorkflowXmlDto oaWorkflowXmlDto = new OaWorkflowXmlDto();
        oaWorkflowXmlDto.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        List<OaWorkflowXmlDto> oaWorkflowXmlDtos = oaWorkflowXmlInnerServiceSMOImpl.queryOaWorkflowXmls(oaWorkflowXmlDto);
        Assert.listOnlyOne(oaWorkflowXmlDtos, "流程不存在");
        List<JSONObject> tasks = oaWorkflowActivitiInnerServiceSMOImpl.queryFirstAuditStaff(oaWorkflowXmlDtos.get(0));
        if (tasks == null || tasks.size() < 1) {
            throw new CmdException("流程未设置下一步审核人");
        }
        String assignee = tasks.get(0).getString("assignee");
        if (assignee.startsWith("-")) {
            throw new CmdException("流程未设置下一步审核人");
        }
        return assignee;
    }

    //处理车位id
    public JSONObject dealParkingSpace(JSONObject reqJson) {
        //是否有空闲车位 false 有空闲  true无空闲
        boolean freeSpace = false;
        //获取访客配置里配置的停车场id
//        String parkingAreaId = CommunitySettingFactory.getValue(reqJson.getString("communityId"), ASCRIPTION_CAR_AREA_ID);
        String ascriptionCarAreaId = reqJson.getString("ascriptionCarAreaId");
        if (StringUtil.isEmpty(ascriptionCarAreaId)) { //如果没有配置停车场id，就随便分配该小区下一个空闲车位
            ParkingSpaceDto parkingSpace = new ParkingSpaceDto();
            parkingSpace.setCommunityId(reqJson.getString("communityId"));
            parkingSpace.setState("F"); //车位状态 出售 S，出租 H ，空闲 F
            parkingSpace.setParkingType("1"); //1：普通车位  2：子母车位  3：豪华车位
            //查询小区空闲车位
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpace);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                freeSpace = true;
            } else {
                //随机生成一个不大于集合长度的整数
                Random random = new Random();
                int i = random.nextInt(parkingSpaceDtos.size());
                //获取车位id
                String psId = parkingSpaceDtos.get(i).getPsId();
                reqJson.put("psId", psId);
            }
        } else {
            ParkingSpaceDto parkingSpace = new ParkingSpaceDto();
            parkingSpace.setCommunityId(reqJson.getString("communityId"));
            parkingSpace.setPaId(ascriptionCarAreaId); //停车场id
            parkingSpace.setState("F"); //车位状态 出售 S，出租 H ，空闲 F
            parkingSpace.setParkingType("1"); //1：普通车位  2：子母车位  3：豪华车位
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpace);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                freeSpace = true;
            } else {
                //随机生成一个不大于集合长度的整数
                Random random = new Random();
                int i = random.nextInt(parkingSpaceDtos.size());
                //获取车位id
                String psId = parkingSpaceDtos.get(i).getPsId();
                reqJson.put("psId", psId);
            }
        }
        reqJson.put("freeSpace", freeSpace);
        return reqJson;
    }

    //更改车位状态
    public void modifyParkingSpaceSate(JSONObject reqJson) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        Assert.listOnlyOne(parkingSpaceDtos, "查询车位错误！");
        ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
        parkingSpacePo.setPsId(parkingSpaceDtos.get(0).getPsId());
        parkingSpacePo.setState("H"); //车位状态 出售 S，出租 H ，空闲 F
        parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
    }

    //判断是否超过访客登记次数
    public JSONObject dealVisitorRegistrationTimes(JSONObject reqJson) {
        //是否超过规定次数
        boolean specifiedTimes = false;
        //查询预约车辆登记次数
//        String visitNumber = CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER);
        String visitNumber = reqJson.getString("visitNumber"); //预约车限制次数
        if (StringUtil.isEmpty(visitNumber)) {
            reqJson.put("specifiedTimes", specifiedTimes);
            return reqJson;
        }
        int number = Integer.parseInt(visitNumber);
        VisitDto visitDto = new VisitDto();
        //查询当天车辆登记次数
        visitDto.setOwnerId(reqJson.getString("ownerId"));
        visitDto.setCommunityId(reqJson.getString("communityId"));
        visitDto.setCarNumNoEmpty("1");
        visitDto.setSameDay("1");
        visitDto.setCarState("1"); //车辆审核通过
        visitDto.setSign(reqJson.getString("vId"));
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        int count = visitDtos.size();
        //预约车辆登记次数0不做限制
        if (count >= number && number > 0) {
            reqJson.put("psId", null);
            reqJson.put("freeTime", null);
            specifiedTimes = true;
        }
        reqJson.put("specifiedTimes", specifiedTimes);
        return reqJson;
    }

    //处理预约车免费时长
    public String dealVisitCarFreeTime(JSONObject reqJson) {
        //获取预约车免费时长的值
//        String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
        String carFreeTime = reqJson.getString("carFreeTime");
        if (StringUtil.isEmpty(carFreeTime)) {
            carFreeTime = "120";
        }
        Date time = DateUtil.getDateFromStringA(reqJson.getString("visitTime"));
        Calendar newTime = Calendar.getInstance();
        newTime.setTime(time);
        newTime.add(Calendar.MINUTE, Integer.parseInt(carFreeTime));//日期加上分钟
        Date newDate = newTime.getTime();
        String finishFreeTime = DateUtil.getFormatTimeString(newDate, DateUtil.DATE_FORMATE_STRING_A);
        return finishFreeTime;
    }

    //获取预约车辆停车场ID、预约车辆免费时长、预约车限制次数、预约车是否审核
    public JSONObject getVisitCarOperate(JSONObject reqJson) {
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos != null && visitSettingDtos.size() > 0) {
            reqJson.put("ascriptionCarAreaId", visitSettingDtos.get(0).getPaId()); //预约车辆归属停车场ID
            reqJson.put("carFreeTime", visitSettingDtos.get(0).getCarFreeTime()); //预约车辆免费时长(单位为分钟)
            reqJson.put("visitNumber", visitSettingDtos.get(0).getVisitNumber()); //预约车限制次数
            reqJson.put("isNeedReview", visitSettingDtos.get(0).getIsNeedReview()); //预约车是否审核  0表示需要审核  1表示不需要审核
        }
        return reqJson;
    }
}
