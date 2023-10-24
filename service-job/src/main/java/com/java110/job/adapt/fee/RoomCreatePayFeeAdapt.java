package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.Business;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 欠费催缴databus
 */
@Component(value = "roomCreatePayFeeAdapt")
public class RoomCreatePayFeeAdapt extends DatabusAdaptImpl {
    private static final int DEFAULT_ADD_FEE_COUNT = 200;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOweFeeCallableV1InnerServiceSMO oweFeeCallableV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;


    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;
    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();


        JSONArray roomIds = data.getJSONArray("roomIds");

        List<String> tmpRoomIds = new ArrayList<>();


        for (int roomIndex = 0; roomIndex < roomIds.size(); roomIndex++) {
            tmpRoomIds.add(roomIds.getString(roomIndex));
            if (tmpRoomIds.size() == DEFAULT_ADD_FEE_COUNT) {
                doCreateRoomPayFee(data, tmpRoomIds);
                tmpRoomIds = new ArrayList<>();
            }
        }
        if (!tmpRoomIds.isEmpty()) {
            doCreateRoomPayFee(data, tmpRoomIds);
        }
    }

    private void doCreateRoomPayFee(JSONObject data, List<String> roomIds) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(data.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        //todo 房屋不存在，这种场景一般执行不到
        if (roomDtos == null || roomDtos.isEmpty()) {
            throw new IllegalArgumentException("房屋不存在");
        }


        //todo 查询业主信息
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(data.getString("communityId"));
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

        // todo 封装 payFeePo 和 attrs

        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(data.getString("communityId"));
        feeConfigDto.setConfigId(data.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + data.getString("configId"));


        int saveFlag = 0;
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {
            //todo 加入 房屋费用
            feePos.add(addRoomFee(roomDtos.get(roomIndex), data, feeConfigDtos.get(0)));
            if (!StringUtil.isEmpty(roomDtos.get(roomIndex).getOwnerId())) {
                if (!FeeDto.FEE_FLAG_CYCLE.equals(data.getString("feeFlag"))) {
                    feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME, data.containsKey("endTime") ? data.getString("endTime") : data.getString("configEndTime")));
                }
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_OWNER_ID, roomDtos.get(roomIndex).getOwnerId()));
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_OWNER_LINK, roomDtos.get(roomIndex).getLink()));
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_OWNER_NAME, roomDtos.get(roomIndex).getOwnerName()));
            }

            //todo 定制开发 加入
            //1、对合同约定的租金递增比例、递增年限各不相同的问题，支持按合同到期日期设租金递增比例。
            //2、能自动设置递增的租金实行自动计算当月的租金。
            if (data.containsKey("configComputingFormula") && FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(data.getString("configComputingFormula"))) {
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_RATE_CYCLE, data.getString("rateCycle")));
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_RATE, data.getString("rate")));
                feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_RATE_START_TIME, data.getString("rateStartTime")));
            }

            //todo 付费对象名称
            feeAttrsPos.add(addFeeAttr(data, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME, roomDtos.get(roomIndex).getFloorNum() + "-" + roomDtos.get(roomIndex).getUnitNum() + "-" + roomDtos.get(roomIndex).getRoomNum()));

            if (feePos.size() == DEFAULT_ADD_FEE_COUNT) {
                saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
                feePos = new ArrayList<>();
                feeAttrsPos = new ArrayList<>();
            }
        }
        if (!feePos.isEmpty()) {
            saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
        }

    }

    private PayFeePo addRoomFee(RoomDto roomDto, JSONObject paramInJson, FeeConfigDto feeConfigDto) {

        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }
        //查询费用项
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
            businessUnit.put("amount", paramInJson.getString("amount"));
        }
        businessUnit.put("startTime", time);
        businessUnit.put("endTime", time);
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", roomDto.getRoomId());
        businessUnit.put("payerObjType", FeeDto.PAYER_OBJ_TYPE_ROOM);
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("batchId", paramInJson.getString("batchId"));
        businessUnit.put("userId", paramInJson.getString("userId"));
        paramInJson.put("feeId", businessUnit.getString("feeId"));
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDto.getFeeFlag())
                && !StringUtil.isEmpty(paramInJson.getString("endTime"))) {
            paramInJson.put("maxTime", paramInJson.getString("endTime"));
        } else {
            paramInJson.put("maxTime", feeConfigDto.getEndTime());
        }
        return BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
    }

    private FeeAttrPo addFeeAttr(JSONObject paramInJson, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(paramInJson.getString("communityId"));
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(paramInJson.getString("feeId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        return feeAttrPo;

    }

    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        if (feePos == null || feePos.isEmpty()) {
            return 1;
        }

        //todo 账单模式
        String billModal = ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.needGeneratorBillData(feePos);

        if ("Y".equals(billModal)) {
            return 1;
        }


        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        // todo 这里异步的方式计算 月数据 和欠费数据
        List<String> feeIds = new ArrayList<>();
        for (PayFeePo feePo : feePos) {
            feeIds.add(feePo.getFeeId());
        }

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(feePos.get(0).getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setFeeIds(feeIds);

        payFeeMonthInnerServiceSMOImpl.doGeneratorFeeMonths(payFeeDetailRefreshFeeMonthDto);

        payFeeMonthInnerServiceSMOImpl.doGeneratorOweFees(payFeeDetailRefreshFeeMonthDto);
        return flag;
    }

}
