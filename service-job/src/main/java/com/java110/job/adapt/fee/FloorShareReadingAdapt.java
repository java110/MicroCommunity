package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.floorShareMeter.FloorShareMeterDto;
import com.java110.dto.floorShareReading.FloorShareReadingDto;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.Business;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.floorShareFee.FloorShareFeePo;
import com.java110.po.floorShareMeter.FloorShareMeterPo;
import com.java110.po.floorShareReading.FloorShareReadingPo;
import com.java110.po.importFee.ImportFeeDetailPo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼栋费用公摊databus
 */
@Component(value = "floorShareReadingAdapt")
public class FloorShareReadingAdapt extends DatabusAdaptImpl {

    @Autowired
    private IFloorShareReadingV1InnerServiceSMO floorShareReadingV1InnerServiceSMOImpl;

    @Autowired
    private IFloorShareMeterV1InnerServiceSMO floorShareMeterV1InnerServiceSMOImpl;

    @Autowired
    private IFloorShareFeeV1InnerServiceSMO floorShareFeeV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();
        Assert.hasKeyAndValue(data, "readingId", "未包含抄表记录");

        FloorShareReadingDto floorShareReadingDto = new FloorShareReadingDto();
        floorShareReadingDto.setReadingId(data.getString("readingId"));
        List<FloorShareReadingDto> floorShareReadingDtos = floorShareReadingV1InnerServiceSMOImpl.queryFloorShareReadings(floorShareReadingDto);

        Assert.listOnlyOne(floorShareReadingDtos, "未查询到抄表记录");

        FloorShareMeterDto floorShareMeterDto = new FloorShareMeterDto();
        floorShareMeterDto.setFsmId(floorShareReadingDtos.get(0).getFsmId());
        floorShareMeterDto.setCommunityId(floorShareReadingDtos.get(0).getCommunityId());
        List<FloorShareMeterDto> floorShareMeterDtos = floorShareMeterV1InnerServiceSMOImpl.queryFloorShareMeters(floorShareMeterDto);
        if (ListUtil.isNull(floorShareMeterDtos)) {
            modifyShareMsg(floorShareReadingDtos.get(0), "未包含公摊表");
            return;
        }


        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(floorShareReadingDtos.get(0).getCommunityId());
        roomDto.setFloorId(floorShareMeterDtos.get(0).getFloorId());
        roomDto.setStates(new String[]{
                RoomDto.STATE_SELL,
                RoomDto.STATE_DELIVERY,//已交房
                RoomDto.STATE_NO_HOME,//未入住
                RoomDto.STATE_RENOVATION,//已装修
                RoomDto.STATE_SHOP_FIRE,//已出租
                RoomDto.STATE_SHOP_SELL,//已售
                RoomDto.STATE_SHOP_FREE,//空闲
                RoomDto.STATE_SHOP_REPAIR//装修中
        });
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (ListUtil.isNull(roomDtos)) {
            modifyShareMsg(floorShareReadingDtos.get(0), "楼栋下没有房屋");
            return;
        }


        //房屋刷入业主信息
        List<String> roomIds = new ArrayList<>();
        for (RoomDto tmpRoomDto : roomDtos) {
            roomIds.add(tmpRoomDto.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(roomDtos.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (RoomDto tmpRoomDto : roomDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (tmpRoomDto.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    tmpRoomDto.setOwnerId(tmpOwnerDto.getOwnerId());
                    tmpRoomDto.setOwnerName(tmpOwnerDto.getName());
                    tmpRoomDto.setLink(tmpOwnerDto.getLink());
                }
            }
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(floorShareMeterDtos.get(0).getConfigId());
        feeConfigDto.setCommunityId(floorShareMeterDtos.get(0).getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos)) {
            modifyShareMsg(floorShareReadingDtos.get(0), "费用项不存在");
            return;
        }

        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(floorShareReadingDtos.get(0).getCommunityId());
        payFeeBatchPo.setCreateUserId(data.getString("staffId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(data.getString("staffId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            modifyShareMsg(floorShareReadingDtos.get(0), "生成批次失败");
            return;
        }

        String batchId = payFeeBatchPo.getBatchId();

        sharingFeeToRoom(floorShareMeterDtos.get(0), roomDtos, feeConfigDtos.get(0), floorShareReadingDtos.get(0), batchId);

    }

    private void sharingFeeToRoom(FloorShareMeterDto floorShareMeterDto,
                                  List<RoomDto> roomDtos,
                                  FeeConfigDto feeConfigDto,
                                  FloorShareReadingDto floorShareReadingDto,
                                  String batchId) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<FloorShareFeePo> floorShareFeePos = new ArrayList<>();
        double roomDegree = 0.00;

        for (RoomDto roomDto : roomDtos) {
            // todo 计算房屋公摊度数
            roomDegree = computeRoomDegree(roomDtos, roomDto, floorShareMeterDto, floorShareReadingDto);

            doSharingFeeToRoom(floorShareMeterDto, floorShareReadingDto, roomDto, roomDegree, payFeePos, feeConfigDto, feeAttrPos,
                    floorShareFeePos);
        }

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(floorShareMeterDto.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "未找到小区信息");

        for (PayFeePo payFeePo : payFeePos) {
            payFeePo.setBatchId(batchId);
            payFeePo.setIncomeObjId(communityDtos.get(0).getStoreId());
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        floorShareFeeV1InnerServiceSMOImpl.saveFloorShareFees(floorShareFeePos);

        modifyShareMsg(floorShareReadingDto,"公摊完成");

    }

    private double computeRoomDegree(List<RoomDto> roomDtos, RoomDto curRoomDto, FloorShareMeterDto floorShareMeterDto, FloorShareReadingDto floorShareReadingDto) {
        BigDecimal totalDegree = new BigDecimal(floorShareReadingDto.getCurDegrees());
        totalDegree = totalDegree.subtract(new BigDecimal(floorShareReadingDto.getPreDegrees()));
        if (FloorShareMeterDto.SHARE_TYPE_ROOM_COUNT.equals(floorShareMeterDto.getShareType())) { // 按户公摊
            totalDegree = totalDegree.divide(new BigDecimal(roomDtos.size() + ""), 4, BigDecimal.ROUND_HALF_UP);
            return totalDegree.doubleValue();
        } else if (FloorShareMeterDto.SHARE_TYPE_ROOM_AREA.equals(floorShareMeterDto.getShareType())) { //按面积公摊
            BigDecimal totalRoomArea = new BigDecimal("0");
            for (RoomDto roomDto : roomDtos) {
                totalRoomArea = totalRoomArea.add(new BigDecimal(roomDto.getBuiltUpArea()));
            }
            totalDegree = totalDegree.divide(totalRoomArea, 4, BigDecimal.ROUND_HALF_UP);
            totalDegree = totalDegree.multiply(new BigDecimal(curRoomDto.getBuiltUpArea())).setScale(4, BigDecimal.ROUND_HALF_UP);
            return totalDegree.doubleValue();
        }
        return 0.00;
    }

    private void doSharingFeeToRoom(FloorShareMeterDto floorShareMeterDto, FloorShareReadingDto floorShareReadingDto,
                                    RoomDto roomDto, double roomDegree,
                                    List<PayFeePo> payFeePos,
                                    FeeConfigDto feeConfigDto,
                                    List<FeeAttrPo> feeAttrPos,
                                    List<FloorShareFeePo> floorShareFeePos) {

        // todo 收费金额
        double roomAmount = new BigDecimal(roomDegree)
                .multiply(new BigDecimal(floorShareMeterDto.getSharePrice()))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setEndTime(floorShareReadingDto.getPreReadingTime());
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(floorShareMeterDto.getCommunityId());
        payFeePo.setConfigId(feeConfigDto.getConfigId());
        payFeePo.setPayerObjId(roomDto.getRoomId());
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setUserId("-1");
        payFeePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setAmount(roomAmount + "");

        //payFeePo.setStartTime(importRoomFee.getStartTime());
        payFeePo.setStartTime(floorShareReadingDto.getPreReadingTime());

        payFeePos.add(payFeePo);

        // 导入费用名称
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME);
        feeAttrPo.setValue(roomDto.getRoomName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        // 公摊用量
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_SHARE_DEGREES);
        feeAttrPo.setValue(roomDegree + "");
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        // 公摊总用量
        BigDecimal totalDegree = new BigDecimal(floorShareReadingDto.getCurDegrees());
        totalDegree = totalDegree.subtract(new BigDecimal(floorShareReadingDto.getPreDegrees()));
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_TOTAL_DEGREES);
        feeAttrPo.setValue(totalDegree.doubleValue() + "");
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);


        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
        feeAttrPo.setValue(floorShareReadingDto.getCurReadingTime());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        String ownerName = "无";
        if (!StringUtil.isEmpty(roomDto.getOwnerId())) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
            feeAttrPo.setValue(roomDto.getOwnerId());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(roomDto.getOwnerName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
            ownerName = roomDto.getOwnerName();
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(roomDto.getLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
        }

        // 公摊公式
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(floorShareMeterDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_SHARE_FORMULA);
        feeAttrPo.setValue(floorShareMeterDto.getShareTypeName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        FloorShareFeePo floorShareFeePo = new FloorShareFeePo();
        floorShareFeePo.setAmount(roomAmount + "");
        floorShareFeePo.setOwnerName(ownerName);
        floorShareFeePo.setFsfId(GenerateCodeFactory.getGeneratorId("11"));
        floorShareFeePo.setFsmId(floorShareMeterDto.getFsmId());
        floorShareFeePo.setFeeName(feeConfigDto.getFeeName());
        floorShareFeePo.setRemark("");
        floorShareFeePo.setReadingId(floorShareReadingDto.getReadingId());
        floorShareFeePo.setFeeId(payFeePo.getFeeId());
        floorShareFeePo.setRoomName(roomDto.getRoomName());
        floorShareFeePo.setDegrees(roomDegree + "");
        floorShareFeePos.add(floorShareFeePo);

    }

    private void modifyShareMsg(FloorShareReadingDto floorShareReadingDto, String shareMsg) {
        FloorShareReadingPo floorShareReadingPo = new FloorShareReadingPo();
        floorShareReadingPo.setReadingId(floorShareReadingDto.getReadingId());
        floorShareReadingPo.setShareMsg("公摊进度：" + shareMsg);
        floorShareReadingV1InnerServiceSMOImpl.updateFloorShareReading(floorShareReadingPo);
    }


}
