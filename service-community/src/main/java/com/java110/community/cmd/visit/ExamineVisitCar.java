package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 访客登记车辆审核
 *
 * @author fqz
 * @date 2023-02-23 14:47
 */
@Java110Cmd(serviceCode = "visit.examineVisitCar")
public class ExamineVisitCar extends Cmd {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "vId", "访客记录ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(reqJson.getString("vId"));
        visitDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitDto> visitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);
        Assert.listOnlyOne(visitDtos, "访客不存在");
        //获取预约车辆停车场ID、预约车辆免费时长、预约车限制次数、预约车是否审核
        JSONObject visitJson = getVisitCarOperate(reqJson);
        VisitPo visitPo = BeanConvertUtil.covertBean(reqJson, VisitPo.class);
        if (reqJson.containsKey("carState") && reqJson.getString("carState").equals("1")) { //审核通过
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
                    //处理预约车免费时长
                    String freeTime = dealVisitCarFreeTime(visitJson);
                    reqJson.put("freeTime", freeTime);
                    visitPo.setFreeTime(freeTime); //预约车免费时长
                    int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
                    if (flag < 1) {
                        throw new CmdException("修改访客状态失败");
                    }
                    //修改车位状态
                    modifyParkingSpaceSate(paramJson);
                }
            }
        } else { //审核不通过
            int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
            if (flag < 1) {
                throw new CmdException("修改访客状态失败");
            }
        }
    }

    //处理车位id
    public JSONObject dealParkingSpace(JSONObject reqJson) {
        //是否有空闲车位 false 有空闲  true无空闲
        boolean freeSpace = false;
        //获取访客配置里配置的停车场id
//        String parkingAreaId = CommunitySettingFactory.getValue(reqJson.getString("communityId"), ASCRIPTION_CAR_AREA_ID);
        String ascriptionCarAreaId = reqJson.getString("ascriptionCarAreaId"); //预约车辆归属停车场ID
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
        String carFreeTime = reqJson.getString("carFreeTime"); //预约车辆免费时长(单位为分钟)
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

    //获取预约车辆停车场ID、预约车辆免费时长、预约车限制次数、预约车是否审核
    public JSONObject getVisitCarOperate(JSONObject reqJson) {
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos != null && visitSettingDtos.size() > 0) {
            //如果车辆同步，就用访客配置里的停车场id；如果车辆不同步，就用小区配置的停车场id
            if (!StringUtil.isEmpty(visitSettingDtos.get(0).getCarNumWay()) && visitSettingDtos.get(0).getCarNumWay().equals("Y")) { //车辆是否同步 Y 是 N 否
                reqJson.put("ascriptionCarAreaId", visitSettingDtos.get(0).getPaId()); //预约车辆归属停车场ID
            } else {
                String parkingAreaId = CommunitySettingFactory.getValue(reqJson.getString("communityId"), ASCRIPTION_CAR_AREA_ID); //获取小区配置里配置的停车场id
                reqJson.put("ascriptionCarAreaId", parkingAreaId); //预约车辆归属停车场ID
            }
            reqJson.put("carFreeTime", visitSettingDtos.get(0).getCarFreeTime()); //预约车辆免费时长(单位为分钟)
            reqJson.put("visitNumber", visitSettingDtos.get(0).getVisitNumber()); //预约车限制次数
            reqJson.put("isNeedReview", visitSettingDtos.get(0).getIsNeedReview()); //预约车是否审核  0表示需要审核  1表示不需要审核
        }
        return reqJson;
    }
}
