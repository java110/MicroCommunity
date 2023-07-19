package com.java110.job.adapt.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.Business;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.community.*;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fqz
 * @description 缴费修改预付期账单明细表
 * @date 2023-04-10 11:10
 */
@Component(value = "editReportFeeMonthStatisticsBillAdapt")
public class EditReportFeeMonthStatisticsBillAdapt extends DatabusAdaptImpl {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentInnerServiceSMO reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();
        JSONArray businessPayFeeDetails = null;
        if (data == null) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setbId(business.getbId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未查询到缴费记录");
            businessPayFeeDetails = JSONArray.parseArray(JSONArray.toJSONString(feeDetailDtos, SerializerFeature.WriteDateUseDateFormat));
        } else if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFeeDetails = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFeeDetails = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(data);
            }
        }
        for (int bPayFeeDetailIndex = 0; bPayFeeDetailIndex < businessPayFeeDetails.size(); bPayFeeDetailIndex++) {
            JSONObject businessPayFeeDetail = businessPayFeeDetails.getJSONObject(bPayFeeDetailIndex);
            editReportFeeMonthStatisticsBill(business, businessPayFeeDetail);
        }
    }

    private void editReportFeeMonthStatisticsBill(Business business, JSONObject businessPayFeeDetail) throws ParseException {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(businessPayFeeDetail.getString("feeId"));
        feeDto.setCommunityId(businessPayFeeDetail.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "未查询到费用信息！");
        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDtos.get(0).getConfigId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取费用标识
        String feeFlag = feeConfigDtos.get(0).getFeeFlag(); //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
        if (!StringUtil.isEmpty(feeConfigDtos.get(0).getPaymentCycle()) && !StringUtil.isEmpty(businessPayFeeDetail.getString("cycles"))
                && feeConfigDtos.get(0).getPaymentCycle().equals(businessPayFeeDetail.getString("cycles"))) { //缴费周期为一个周期的情况
            //查询预付期账单明细表未缴费的数据
            ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
            reportFeeMonthStatisticsPrepaymentDto.setFeeId(businessPayFeeDetail.getString("feeId"));
            reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(businessPayFeeDetail.getString("startTime")); //费用开始时间
            reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(businessPayFeeDetail.getString("endTime")); //费用结束时间
            List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments =
                    reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentDto);
            if (!ListUtil.isNull(reportFeeMonthStatisticsPrepayments)) { //有数据就更新数据
                ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayments.get(0).getPrepaymentId());
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("1"); //0 未缴费；1 已缴费
                reportFeeMonthStatisticsPrepaymentPo.setbillState("2"); //账单状态：0 正常；1 已逾期； 2已还清
                reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount(businessPayFeeDetail.getString("receivedAmount")); //实收
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(businessPayFeeDetail.getString("detailId")); //费用明细id
                reportFeeMonthStatisticsPrepaymentPo.setPayTime(businessPayFeeDetail.getString("createTime")); //缴费时间
                reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(sdf.format(new Date()));
                reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.updateReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentPo);
            } else { //没有数据就新增一条预付期账单信息
                saveReportFeeMonthStatisticsPrepayment(businessPayFeeDetail, feeDtos, feeConfigDtos); //添加预付期账单信息
            }
        } else if (!StringUtil.isEmpty(feeConfigDtos.get(0).getPaymentCycle()) && !StringUtil.isEmpty(businessPayFeeDetail.getString("cycles"))
                && !feeConfigDtos.get(0).getPaymentCycle().equals(businessPayFeeDetail.getString("cycles"))) { //缴费周期不是一个周期的情况
            //查询预付期账单明细表未缴费的数据
            ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
            reportFeeMonthStatisticsPrepaymentDto.setFeeId(businessPayFeeDetail.getString("feeId"));
            reportFeeMonthStatisticsPrepaymentDto.setNewBeginTime(businessPayFeeDetail.getString("startTime")); //费用开始时间
            reportFeeMonthStatisticsPrepaymentDto.setNewFinishTime(businessPayFeeDetail.getString("endTime")); //费用结束时间
            List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments =
                    reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentDto);
            if (reportFeeMonthStatisticsPrepayments != null && reportFeeMonthStatisticsPrepayments.size() > 0) {
                for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepayments) {
                    //删除预付期报表里在本次缴费周期里的数据
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                    reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.deleteReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentPo);
                }
            }
            saveReportFeeMonthStatisticsPrepayment(businessPayFeeDetail, feeDtos, feeConfigDtos); //添加预付期账单信息
        }
        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
        reportFeeMonthStatisticsPrepaymentDto.setFeeId(businessPayFeeDetail.getString("feeId"));
        reportFeeMonthStatisticsPrepaymentDto.setPrepaymentState("0"); //0 未缴费；1 已缴费
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentDto);
        if (reportFeeMonthStatisticsPrepaymentDtos != null && reportFeeMonthStatisticsPrepaymentDtos.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentDtos) {
                //获取该账单明细开始时间
                String feeBeginTime = reportFeeMonthStatisticsPrepayment.getFeeBeginTime();
                Date beginTime = format.parse(feeBeginTime);
                //获取该账单明细结束时间
                String feeFinishTime = reportFeeMonthStatisticsPrepayment.getFeeFinishTime();
                Date finishTime = format.parse(feeFinishTime);
                //获取本次缴费结束时间
                String endTime = businessPayFeeDetail.getString("endTime");
                Date newEndTime = format.parse(endTime);
                if (beginTime.before(newEndTime) && finishTime.after(newEndTime)) {
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                    reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.deleteReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentPo);
                }
            }
        }
    }

    public void saveReportFeeMonthStatisticsPrepayment(JSONObject businessPayFeeDetail, List<FeeDto> feeDtos, List<FeeConfigDto> feeConfigDtos) {
        //获取费用标识
        String feeFlag = feeConfigDtos.get(0).getFeeFlag(); //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
        ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        reportFeeMonthStatisticsPrepaymentPo.setCommunityId(businessPayFeeDetail.getString("communityId"));
        reportFeeMonthStatisticsPrepaymentPo.setObjType(feeDtos.get(0).getPayerObjType());
        reportFeeMonthStatisticsPrepaymentPo.setObjId(feeDtos.get(0).getPayerObjId());
        if (!StringUtil.isEmpty(feeDtos.get(0).getPayerObjType()) && feeDtos.get(0).getPayerObjType().equals("3333")) { //房屋
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDtos.get(0).getPayerObjId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "查询房屋错误！");
            UnitDto unitDto = new UnitDto();
            unitDto.setUnitId(roomDtos.get(0).getUnitId());
            List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
            Assert.listOnlyOne(unitDtos, "查询单元错误！");
            FloorDto floorDto = new FloorDto();
            floorDto.setFloorId(unitDtos.get(0).getFloorId());
            List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
            Assert.listOnlyOne(floorDtos, "查询楼栋错误！");
            if (RoomDto.ROOM_TYPE_ROOM.equals(roomDtos.get(0).getRoomType())) {
                reportFeeMonthStatisticsPrepaymentPo.setObjName(floorDtos.get(0).getFloorNum() + "栋" + unitDtos.get(0).getUnitNum() + "单元" + roomDtos.get(0).getRoomNum() + "室");
                reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(floorDtos.get(0).getFloorNum() + "-" + unitDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());
            } else {
                reportFeeMonthStatisticsPrepaymentPo.setObjName(floorDtos.get(0).getFloorNum() + "栋" + roomDtos.get(0).getRoomNum() + "室");
                reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(floorDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getRoomNum());
            }
        } else { //车辆
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(feeDtos.get(0).getPayerObjId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "查询车辆错误！");
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "查询车位错误！");
            ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
            parkingAreaDto.setPaId(parkingSpaceDtos.get(0).getPaId());
            List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
            Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
            reportFeeMonthStatisticsPrepaymentPo.setObjName(ownerCarDtos.get(0).getCarNum() + "(" + parkingAreaDtos.get(0).getNum() + "停车场" + parkingSpaceDtos.get(0).getNum() + "车位)");
            reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(parkingAreaDtos.get(0).getNum() + "-" + parkingSpaceDtos.get(0).getNum() + "-" + ownerCarDtos.get(0).getCarNum());
        }
        reportFeeMonthStatisticsPrepaymentPo.setFeeId(businessPayFeeDetail.getString("feeId"));
        reportFeeMonthStatisticsPrepaymentPo.setFeeName(feeDtos.get(0).getFeeName());
        reportFeeMonthStatisticsPrepaymentPo.setConfigId(feeDtos.get(0).getConfigId());
        reportFeeMonthStatisticsPrepaymentPo.setReceivableAmount(businessPayFeeDetail.getString("receivableAmount")); //应收
        reportFeeMonthStatisticsPrepaymentPo.setPayableAmount(businessPayFeeDetail.getString("payableAmount")); //应缴
        reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount(businessPayFeeDetail.getString("receivedAmount")); //实收
        reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("1"); //0 未缴费; 1 已缴费
        reportFeeMonthStatisticsPrepaymentPo.setbillState("2"); //账单状态：0 正常；1 已逾期； 2已还清
        reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单已缴费生成");
        reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(businessPayFeeDetail.getString("startTime"));//计费开始时间
        reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(businessPayFeeDetail.getString("endTime")); //计费结束时间
        reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        reportFeeMonthStatisticsPrepaymentPo.setPayTime(businessPayFeeDetail.getString("createTime"));
        if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
            //获取缴费周期
            String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(paymentCycle); //缴费周期
        } else {
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(""); //缴费周期
        }
        reportFeeMonthStatisticsPrepaymentPo.setPayFeeFlag(feeFlag); //费用标识
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(businessPayFeeDetail.getString("detailId")); //费用明细id
        reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.saveReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentPo);
    }
}
