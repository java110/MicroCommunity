package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.po.owner.VisitPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 审核 放行
 */
@Java110Cmd(serviceCode = "visit.auditUndoVisit")
public class AuditUndoVisitCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "vId", "未包含访客ID");
        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含状态说明");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(reqJson.getString("vId"));
        visitDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitDto> visitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
        Assert.listOnlyOne(visitDtos, "访客不存在");
        //状态 W待审核 D 审核中 C 审核完成 D 审核失败
        VisitPo visitPo = new VisitPo();
        visitPo.setMark("1");
        visitPo.setvId(visitDtos.get(0).getvId());
        visitPo.setCommunityId(reqJson.getString("communityId"));
        reqJson.put("id", reqJson.getString("vId"));
        reqJson.put("storeId", storeId);
        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))
                || "1500".equals(reqJson.getString("auditCode"))) { //办理操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                visitPo.setState(VisitDto.STATE_C);
            } else {
                visitPo.setState(VisitDto.STATE_D);
            }
            if (!StringUtil.isEmpty(visitPo.getState()) && visitPo.getState().equals(VisitDto.STATE_C)) { //访客审核通过
                //查询访客配置信息
                VisitSettingDto visitSettingDto = new VisitSettingDto();
                visitSettingDto.setCommunityId(reqJson.getString("communityId"));
                List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
                if (!StringUtil.isEmpty(visitDtos.get(0).getCarNum())) { //有车辆

                    if (visitSettingDtos != null && visitSettingDtos.size() == 1 && visitSettingDtos.get(0).getCarNumWay().equals("Y")) { //车辆同步 Y 是 N 否
                        //获取预约车辆停车场ID、预约车辆免费时长、预约车限制次数、预约车是否审核
                        JSONObject visitJson = getVisitCarOperate(reqJson);
                        //车辆是否需要审核
//                    String isNeedReview = CommunitySettingFactory.getValue(reqJson.getString("communityId"), IS_NEED_REVIEW); //获取小区配置里车辆是否需要审核的值
                        String isNeedReview = visitJson.getString("isNeedReview"); //获取访客配置里车辆是否需要审核的值
                        if (!StringUtil.isEmpty(isNeedReview) && isNeedReview.equals("0")) { //0表示需要审核；1表示不需要审核
                            visitPo.setCarState(VisitDto.CAR_STATE_W); //车辆状态为待审核状态
                            int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                            if (flag < 1) {
                                throw new CmdException("修改访客状态失败");
                            }
                        } else { //其他情况默认为车辆审核通过
                            reqJson.put("ownerId", visitDtos.get(0).getOwnerId());
                            JSONObject param = dealVisitorRegistrationTimes(visitJson); //判断是否超过访客登记次数
                            if (param.containsKey("specifiedTimes") && !StringUtil.isEmpty(param.getString("specifiedTimes")) && param.getString("specifiedTimes").equals("true")) { //超过车辆登记次数
                                visitPo.setStateRemark("访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法审核！");
                                visitPo.setCarStateRemark("访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法审核！");
                                visitPo.setCarState(VisitDto.CAR_STATE_F); //车辆状态变为审核拒绝
                                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法自动审核！");
                                context.setResponseEntity(responseEntity);
                                int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
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
                                    int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                                    if (flag < 1) {
                                        throw new CmdException("保存访客失败");
                                    }
                                } else { //有空闲车位
                                    visitPo.setPsId(paramJson.getString("psId"));
                                    reqJson.put("visitTime", visitDtos.get(0).getVisitTime());
                                    //处理预约车免费时长
                                    String freeTime = dealVisitCarFreeTime(visitJson);
                                    visitPo.setFreeTime(freeTime); //预约车免费时长
                                    visitPo.setCarState(VisitDto.CAR_STATE_C); //车辆状态为审核通过状态
                                    int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                                    if (flag < 1) {
                                        throw new CmdException("修改访客状态失败");
                                    }
                                    //修改车位状态
                                    modifyParkingSpaceSate(paramJson.getString("psId"));
                                    VisitDto visit = new VisitDto();
                                    visit.setvId(reqJson.getString("vId"));
                                    List<VisitDto> visits = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
                                    Assert.listOnlyOne(visits, "查询访客信息错误！");
                                }
                            }
                        }
                    } else if (visitSettingDtos != null && visitSettingDtos.size() == 1 && visitSettingDtos.get(0).getCarNumWay().equals("N")) { //车辆不同步
                        visitPo.setCarState(VisitDto.CAR_STATE_C); //车辆审核通过
                        visitPo.setCarStateRemark("车辆不同步！");
                        int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                        if (flag < 1) {
                            throw new CmdException("修改访客状态失败");
                        }
                    }
                } else { //无车辆
                    int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                    if (flag < 1) {
                        throw new CmdException("修改访客状态失败");
                    }
                }
            } else { //其他状态
                int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                if (flag < 1) {
                    throw new CmdException("修改访客状态失败");
                }
            }
            //完成当前流程 插入下一处理人
        } else if ("1300".equals(reqJson.getString("auditCode"))) { //转单操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            //reqJson.put("state", "1004"); //工单转单
            visitPo.setState(VisitDto.STATE_D);
            visitPo.setSign("1");
            visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        } else if ("1200".equals(reqJson.getString("auditCode"))
                || "1400".equals(reqJson.getString("auditCode"))) { //退回操作
            reqJson.put("startUserId", reqJson.getString("userId"));
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            //reqJson.put("state", "1003"); //工单退单
            visitPo.setState(VisitDto.STATE_F);
            visitPo.setSign("1");
            visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }
    }

    //处理车位id
    public JSONObject dealParkingSpace(JSONObject reqJson) {
        //是否有空闲车位 false 有空闲  true无空闲
        boolean freeSpace = false;
        //获取小区配置里配置的停车场id
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
    public void modifyParkingSpaceSate(String psId) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(psId);
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
        String visitNumber = reqJson.getString("visitNumber");
        if (StringUtil.isEmpty(visitNumber)) {
            reqJson.put("specifiedTimes", specifiedTimes);
            return reqJson;
        }
        int number = Integer.parseInt(visitNumber);
        VisitDto visitDto = new VisitDto();
        //查询当天车辆登记次数
        visitDto.setOwnerId(reqJson.getString("ownerId"));
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
