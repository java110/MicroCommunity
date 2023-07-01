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

        int count = payFeeDetailMonthInnerServiceSMOImpl.queryPagePayFeeDetailMonthsCount(payFeeDetailMonthDto);

        List<PayFeeDetailMonthDto> payFeeDetailMonthDtos = null;
        if (count > 0) {
            payFeeDetailMonthDtos = payFeeDetailMonthInnerServiceSMOImpl.queryPagePayFeeDetailMonths(payFeeDetailMonthDto);//查询费用项目
            //todo 根据配置处理小数点
            doScale(payFeeDetailMonthDtos);
            //todo 将 费用下的属性刷入进去，方便前段展示使用
            freshFeeAttrs(payFeeDetailMonthDtos);
        } else {
            payFeeDetailMonthDtos = new ArrayList<>();
        }

        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, payFeeDetailMonthDtos));
    }

    /**
     * 处理应收小数点
     *
     * @param payFeeDetailMonthDtos
     */
    private void doScale(List<PayFeeDetailMonthDto> payFeeDetailMonthDtos) {
        if (payFeeDetailMonthDtos == null || payFeeDetailMonthDtos.size() < 1) {
            return;
        }
        double amount = 0.0;
        for (PayFeeDetailMonthDto payFeeDetailMonthDto : payFeeDetailMonthDtos) {
            amount = MoneyUtil.computePriceScale(Double.parseDouble(payFeeDetailMonthDto.getReceivableAmount()),
                    payFeeDetailMonthDto.getScale(),
                    Integer.parseInt(payFeeDetailMonthDto.getDecimalPlace()));
            payFeeDetailMonthDto.setReceivableAmount(amount + "");
        }
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

    private void freshFeeAttrs(List<PayFeeDetailMonthDto> payFeeDetailMonthDtos) {
        List<String> feeIds = new ArrayList<>();
        for (PayFeeDetailMonthDto feeDto : payFeeDetailMonthDtos) {
            feeIds.add(feeDto.getFeeId());
        }

        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return;
        }

        List<FeeAttrDto> tmpFeeAttrDtos = null;
        for (PayFeeDetailMonthDto feeDto : payFeeDetailMonthDtos) {
            tmpFeeAttrDtos = new ArrayList<>();
            for (FeeAttrDto tmpFeeAttrDto : feeAttrDtos) {
                if (!feeDto.getFeeId().equals(tmpFeeAttrDto.getFeeId())) {
                    continue;
                }
                tmpFeeAttrDtos.add(tmpFeeAttrDto);
            }
            feeDto.setFeeAttrs(tmpFeeAttrDtos);
        }
    }
}
