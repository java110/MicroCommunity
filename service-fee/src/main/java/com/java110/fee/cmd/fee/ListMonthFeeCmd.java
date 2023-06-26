package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeDetailMonthDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import com.java110.vo.api.fee.ApiFeeDataVo;
import com.java110.vo.api.fee.ApiFeeVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询 月费用信息
 */
@Java110Cmd(serviceCode = "fee.listMonthFee")
public class ListMonthFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListMonthFeeCmd.class);

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // todo 房屋名称 刷入 房屋ID
        freshPayerObjIdByRoomNum(reqJson);

        PayFeeDetailMonthDto payFeeDetailMonthDto = BeanConvertUtil.covertBean(reqJson, PayFeeDetailMonthDto.class);

        // todo 处理 多个房屋
        morePayerObjIds(reqJson, payFeeDetailMonthDto);

        int count = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonthsCount(payFeeDetailMonthDto);
        List<PayFeeDetailMonthDto> payFeeDetailMonthDtos = null;
        if (count > 0) {
            payFeeDetailMonthDtos = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonths(payFeeDetailMonthDto);//查询费用项目
            //freshFeeAttrs(fees, feeDtos);
        } else {
            payFeeDetailMonthDtos = new ArrayList<>();
        }

        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, payFeeDetailMonthDtos));
    }

    /**
     * 处理 多对象 费用
     *
     * @param reqJson
     * @param payFeeDetailMonthDto
     */
    private void morePayerObjIds(JSONObject reqJson, PayFeeDetailMonthDto payFeeDetailMonthDto) {
        if (!reqJson.containsKey("payerObjIds") || StringUtil.isEmpty(reqJson.getString("payerObjIds"))) {
            return;
        }

        String payerObjIds = reqJson.getString("payerObjIds");
        payFeeDetailMonthDto.setObjIds(payerObjIds.split(","));
    }

    /**
     * 根据房屋名称 刷入 payerObjId
     *
     * @param reqJson
     */
    private void freshPayerObjIdByRoomNum(JSONObject reqJson) {
        if (!reqJson.containsKey("roomNum") || StringUtil.isEmpty(reqJson.getString("roomNum"))) {
            return;
        }

        String[] roomNums = reqJson.getString("roomNum").split("-", 3);
        if (roomNums == null || roomNums.length != 3) {
            throw new IllegalArgumentException("房屋编号格式错误！");
        }

        String floorNum = roomNums[0];
        String unitNum = roomNums[1];
        String roomNum = roomNums[2];
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorNum(floorNum);
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
        if (floorDtos == null || floorDtos.size() < 1) {
            return;
        }
        for (FloorDto floor : floorDtos) {
            UnitDto unitDto = new UnitDto();
            unitDto.setFloorId(floor.getFloorId());
            unitDto.setUnitNum(unitNum);
            List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
            if (unitDtos == null || unitDtos.size() < 1) {
                continue;
            }
            for (UnitDto unit : unitDtos) {
                RoomDto roomDto = new RoomDto();
                roomDto.setUnitId(unit.getUnitId());
                roomDto.setRoomNum(roomNum);
                roomDto.setCommunityId(reqJson.getString("communityId"));
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                Assert.listOnlyOne(roomDtos, "查询不到房屋！");
                reqJson.put("payerObjId", roomDtos.get(0).getRoomId());
            }
        }
    }

    private void freshFeeAttrs(List<ApiFeeDataVo> fees, List<FeeDto> feeDtos) {
        String link = "";
        for (FeeDto feeDto : feeDtos) {
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋
                OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                ownerRoomRelDto.setRoomId(feeDto.getPayerObjId());
                List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
                if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
                    continue;
                }
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                Assert.listOnlyOne(ownerDtos, "查询业主错误！");
                link = ownerDtos.get(0).getLink();
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
                OwnerCarDto ownerCarDto = new OwnerCarDto();
                ownerCarDto.setMemberId(feeDto.getPayerObjId());
                List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
                Assert.listOnlyOne(ownerCarDtos, "查询业主车辆表错误！");
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                Assert.listOnlyOne(ownerDtos, "查询业主错误！");
                link = ownerDtos.get(0).getLink();
            }
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setFeeId(feeDto.getFeeId());
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
                continue;
            }
            for (FeeAttrDto feeAttr : feeAttrDtos) {
                if (!FeeAttrDto.SPEC_CD_OWNER_LINK.equals(feeAttr.getSpecCd())) { //联系方式
                    continue;
                }
                if (feeAttr.getValue().equals(link)) {
                    continue;
                }
                FeeAttrPo feeAttrPo = new FeeAttrPo();
                feeAttrPo.setAttrId(feeAttr.getAttrId());
                feeAttrPo.setValue(link);
                int flag = feeAttrInnerServiceSMOImpl.updateFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("更新业主联系方式失败");
                }

            }
        }
        for (ApiFeeDataVo apiFeeDataVo : fees) {
            for (FeeDto feeDto : feeDtos) {
                if (apiFeeDataVo.getFeeId().equals(feeDto.getFeeId())) {
                    apiFeeDataVo.setFeeAttrs(feeDto.getFeeAttrDtos());
                }
            }
        }
    }
}
