package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.VisitPo;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

//@Java110Cmd(serviceCode = "visit.updateVisit")
public class UpdateVisitSdCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;



    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "vId", "访客记录ID不能为空");
        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "visitGender", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(reqJson, "visitTime", "必填，请填写访客拜访时间");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        //是否有空闲车位
        boolean freeSpace = false;
        //是否存在车辆
        boolean existCar = false;
        //校验车牌号是否存在
        OwnerCarDto ownerCarDto1 = new OwnerCarDto();
        ownerCarDto1.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto1.setCarNum(reqJson.getString("carNum"));
        int count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto1);
        if (count > 0) {
            existCar = true;
            reqJson.put("psId", "-1");
        }
        //flag审核操作 并且 审核通过 state=1 并且业主车辆不存在的情况先existCar=false
        if ("1".equals(reqJson.getString("flag"))
                && "1".equals(reqJson.getString("state"))
                && !existCar) {
            //获取预约车免费时长的值
            String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
            if (StringUtil.isEmpty(freeTime)) {
                freeTime = "120";
            }
            Date time = DateUtil.getDateFromStringA(reqJson.getString("visitTime"));
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(time);
            newTime.add(Calendar.MINUTE, Integer.parseInt(freeTime));//日期加上分钟
            Date newDate = newTime.getTime();
            String finishFreeTime = DateUtil.getFormatTimeString(newDate, DateUtil.DATE_FORMATE_STRING_A);
            reqJson.put("freeTime", finishFreeTime);
            //获取小区配置里配置的停车场id
            String parkingAreaId = CommunitySettingFactory.getValue(reqJson.getString("communityId"), ASCRIPTION_CAR_AREA_ID);
            if (StringUtil.isEmpty(parkingAreaId)) { //如果没有配置停车场id，就随便分配该小区下一个空闲车位
                ParkingSpaceDto parkingSpace = new ParkingSpaceDto();
                parkingSpace.setCommunityId(reqJson.getString("communityId"));
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
            } else {
                ParkingSpaceDto parkingSpace = new ParkingSpaceDto();
                parkingSpace.setCommunityId(reqJson.getString("communityId"));
                parkingSpace.setPaId(parkingAreaId); //停车场id
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
        }
        String result = "";
        if ("1".equals(reqJson.getString("state"))) {
            result = "审核通过！";
        } else if ("2".equals(reqJson.getString("state"))) {
            result = "审核不通过！";
        }
        if (existCar) {
            result = "访客信息审核成功,车辆已经存在预约，请您在预约到期后，再次进行车辆预约，谢谢！";
        }
        if (freeSpace) {
            result = "访客信息审核成功，当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。";
        }
        reqJson.put("stateRemark", result);
        updateVisit(reqJson);

        if (existCar) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "访客信息审核成功,车辆已经存在预约，请您在预约到期后，再次进行车辆预约，谢谢！");
            context.setResponseEntity(responseEntity);
            return;
        }
        if (freeSpace) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "访客信息审核成功，当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。");
            context.setResponseEntity(responseEntity);
            return;
        }
        //审核通过且有车位就更新车位状态
        if (reqJson.containsKey("state") && "1".equals(reqJson.getString("state"))
                && "1".equals(reqJson.getString("flag"))
                && !existCar) {
            ParkingSpaceDto parkingSpace = new ParkingSpaceDto();
            parkingSpace.setPsId(reqJson.getString("psId"));
            //查询停车位
            List<ParkingSpaceDto> parkingSpaces = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpace);
            Assert.listOnlyOne(parkingSpaces, "查询停车位错误！");
            //添加车辆信息
            OwnerCarPo ownerCarPo = new OwnerCarPo();
            ownerCarPo.setCarId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
            ownerCarPo.setOwnerId(reqJson.getString("ownerId"));
            ownerCarPo.setbId("-1");
            ownerCarPo.setCarNum(reqJson.getString("carNum"));
            ownerCarPo.setCarBrand("无（预约车）");
            ownerCarPo.setCarType("9901");
            ownerCarPo.setCarColor("无（预约车）");
            ownerCarPo.setPsId(reqJson.getString("psId"));
            ownerCarPo.setUserId(userId);
            ownerCarPo.setRemark("访客登记预约车");
            ownerCarPo.setCommunityId(reqJson.getString("communityId"));
            ownerCarPo.setStartTime(reqJson.getString("visitTime"));
            ownerCarPo.setEndTime(reqJson.getString("freeTime"));
            ownerCarPo.setState(OwnerCarDto.STATE_NORMAL); //1001 正常状态，2002 车位释放欠费状态  3003 车位释放
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_TEMP); //1001 业主车辆 1002 成员车辆 1003 临时车
            ownerCarPo.setMemberId(reqJson.getString("ownerId"));
            ownerCarPo.setLeaseType("R"); //H 月租车  S 出售车  I 内部车  NM 免费车  R 预约车
            ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
            //添加车辆属性
            OwnerCarAttrPo ownerCarAttrPo = new OwnerCarAttrPo();
            ownerCarAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_cartId));
            ownerCarAttrPo.setCarId(ownerCarPo.getCarId());
            ownerCarAttrPo.setCommunityId(ownerCarPo.getCommunityId());
            ownerCarAttrPo.setSpecCd("6443000036"); //6443000036业主车辆
            ownerCarAttrPo.setValue("true"); //预约车
            ownerCarAttrPo.setbId("-1");
            ownerCarAttrInnerServiceSMOImpl.saveOwnerCarAttr(ownerCarAttrPo);
            //更新车位状态
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setPsId(reqJson.getString("psId"));
            parkingSpacePo.setState("H"); //车位状态 出售 S，出租 H ，空闲 F
            parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        }
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateVisit(JSONObject paramInJson) {

        JSONObject businessVisit = new JSONObject();
        businessVisit.putAll(paramInJson);

        VisitPo visitPo = BeanConvertUtil.covertBean(businessVisit, VisitPo.class);
        int flag = visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        if (flag < 1) {
            throw new CmdException("修改访客失败");
        }
    }
}
