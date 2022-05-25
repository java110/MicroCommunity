package com.java110.api.listener.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.visit.IVisitBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeVisitConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveVisitListener")
public class SaveVisitListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IVisitBMO visitBMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    //键
    public static final String IS_NEED_REVIEW = "IS_NEED_REVIEW";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String ASCRIPTION_CAR_AREA_ID = "ASCRIPTION_CAR_AREA_ID";

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "visitGender", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(reqJson, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(reqJson, "visitTime", "必填，请填写访客拜访时间");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        reqJson.put("vId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vId));
        //是否需要审核
        String isNeedReviewFlag = CommunitySettingFactory.getValue(reqJson.getString("communityId"), IS_NEED_REVIEW);
        //是否有空闲车位 false 有空闲  true无空闲
        boolean freeSpace = false;
        //是否超过规定次数
        boolean specifiedTimes = false;
        //是否存在车辆
        boolean existCar = false;
        if (!StringUtils.isEmpty(isNeedReviewFlag) && isNeedReviewFlag.equals("true") && reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            reqJson.put("state", "0"); //0表示未审核；1表示审核通过；2表示审核拒绝
        } else {
            reqJson.put("state", "1");
        }
        //随行人数
        if (reqJson.containsKey("entourage") && reqJson.getString("entourage").equals("")) {
            reqJson.put("entourage", "0");
        }
        reqJson.put("recordState", "0");
        if (reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            //获取预约车免费时长的值
            String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
            String visitTime = reqJson.getString("visitTime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = df.parse(visitTime);
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(time);
            newTime.add(Calendar.MINUTE, Integer.parseInt(freeTime));//日期加上分钟
            Date newDate = newTime.getTime();
            String finishFreeTime = df.format(newDate);
            reqJson.put("freeTime", finishFreeTime);
            if (!StringUtils.isEmpty(isNeedReviewFlag) && isNeedReviewFlag.equals("false")) { //不需要审核就随机自动分配车位
                //获取小区配置里配置的停车场id
                String parkingAreaId = CommunitySettingFactory.getValue(reqJson.getString("communityId"), ASCRIPTION_CAR_AREA_ID);
                if (StringUtil.isEmpty(parkingAreaId)) { //如果没有配置停车场id，就随便分配该小区下一个空闲车位
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
            } else { //需要审核就在审核通过时才分配车位
                reqJson.put("psId", "-1");
            }
            //查询预约车辆登记次数
            int number = Integer.parseInt(CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER));
            VisitDto visitDto = new VisitDto();
            //查询当天车辆登记次数
            visitDto.setOwnerId(reqJson.getString("ownerId"));
            visitDto.setCarNumNoEmpty("1");
            visitDto.setSameDay("1");
            List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
            int count = visitDtos.size();
            //预约车辆登记次数0不做限制
            if (count >= number && number > 0) {
                if (!StringUtils.isEmpty(isNeedReviewFlag) && isNeedReviewFlag.equals("false")) {
                    reqJson.put("state", "0");
                    reqJson.put("psId", null);
                    reqJson.put("freeTime", null);
                }
                specifiedTimes = true;
            }
            //校验车牌号是否存在
            OwnerCarDto ownerCarDto1 = new OwnerCarDto();
            ownerCarDto1.setCommunityId(reqJson.getString("communityId"));
            ownerCarDto1.setCarNum(reqJson.getString("carNum"));
            int existCarCount = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto1);
            if (existCarCount > 0) {
                existCar = true;
                reqJson.put("psId", "-1");
            }
        }

        String result = "";
        if (existCar) {
            result = "访客信息登记成功,车辆已经存在预约，请您在预约到期后，再次进行车辆预约，谢谢！";
        }
        if (specifiedTimes) {
            result = "访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法自动审核！";
        }
        if (freeSpace) {
            result = "访客信息登记成功,当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。";
        }
        reqJson.put("stateRemark", result);
        visitBMOImpl.addVisit(reqJson, context);
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", "-1");
            businessUnit.put("relTypeCd", "11000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", reqJson.getString("vId"));
            businessUnit.put("fileRealName", fileDto.getFileId());
            businessUnit.put("fileSaveName", fileName);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
        }
        commit(context);
        if (!StringUtil.isEmpty(reqJson.getString("state")) && reqJson.getString("state").equals("1")
                && reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))
                && !existCar && !StringUtil.isEmpty(reqJson.getString("psId"))) { //审核通过且有车位就更新车位状态
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(reqJson.getString("psId"));
            //查询停车位
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "访客登记,查询停车位错误！");
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
            ownerCarPo.setUserId(context.getUserId());
            ownerCarPo.setRemark("访客登记预约车");
            ownerCarPo.setCommunityId(reqJson.getString("communityId"));
            ownerCarPo.setStartTime(reqJson.getString("visitTime"));
            ownerCarPo.setEndTime(reqJson.getString("freeTime"));
            ownerCarPo.setState(OwnerCarDto.STATE_NORMAL); //1001 正常状态，2002 车位释放欠费状态  3003 车位释放
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_TEMP); //1001 业主车辆 1002 成员车辆 1003 临时车
            ownerCarPo.setMemberId(reqJson.getString("ownerId"));
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
            //更改车位状态
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setPsId(reqJson.getString("psId"));
            parkingSpacePo.setState("H"); //车位状态 出售 S，出租 H ，空闲 F
            parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        }
        if (existCar) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,车辆已经存在预约，请您在预约到期后，再次进行车辆预约，谢谢！");
            context.setResponseEntity(responseEntity);
            return;
        }
        if (specifiedTimes) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,您已经超过预约车辆登记次数限制，车辆将无法自动审核！");
            context.setResponseEntity(responseEntity);
            return;
        }
        if (freeSpace) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "访客信息登记成功,当前停车场已无空闲车位，登记车辆将暂时不能进入停车场，请您合理安排出行。");
            context.setResponseEntity(responseEntity);
            return;
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitConstant.ADD_VISIT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
