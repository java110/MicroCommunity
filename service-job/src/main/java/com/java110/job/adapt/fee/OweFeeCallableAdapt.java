package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.Business;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IOweFeeCallableV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 欠费催缴databus
 */
@Component(value = "oweFeeCallableAdapt")
public class OweFeeCallableAdapt extends DatabusAdaptImpl {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOweFeeCallableV1InnerServiceSMO oweFeeCallableV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();


        JSONArray roomIds = data.getJSONArray("roomIds");
        List<OweFeeCallablePo> oweFeeCallablePos = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < roomIds.size(); roomIndex++) {
            try {
                doDealRoomId(data, roomIds.getString(roomIndex), oweFeeCallablePos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (oweFeeCallablePos.isEmpty()) {
            return;
        }

        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallables(oweFeeCallablePos);
        if (flag < 1) {
            throw new IllegalArgumentException("保存催缴记录失败");
        }
        JSONObject content = null;
        String oweRoomUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_FEE_PAGE);
        String oweCarUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_CAR_FEE_PAGE);
        String oweUrl = "";
        OweFeeCallablePo updateOweFeeCallablePo = null;
        OwnerAppUserDto ownerAppUserDto = null;
        String userId = "";
        List<JSONObject> contents = null;

        List<OweFeeCallablePo> sendOweFeeCallablePos = null;

        //todo 按房屋发送
        for (int roomIndex = 0; roomIndex < roomIds.size(); roomIndex++) {
            contents = new ArrayList<>();
            String notifyWay = MsgNotifyFactory.NOTIFY_WAY_WECHAT;
            userId = "";
            String ofcId = "";
            String ownerId = "";
            try {
                sendOweFeeCallablePos = new ArrayList<>();
                for (OweFeeCallablePo oweFeeCallablePo : oweFeeCallablePos) {
                    if (!oweFeeCallablePo.getPayerObjId().equals(roomIds.getString(roomIndex))) {
                        continue;
                    }

                    ofcId = oweFeeCallablePo.getOfcId();
                    ownerId = oweFeeCallablePo.getOwnerId();

                    //todo 业主不存在
                    if (StringUtil.isEmpty(oweFeeCallablePo.getOwnerId()) || oweFeeCallablePo.getOwnerId().startsWith("-")) {
                        updateOweFeeCallablePo = new OweFeeCallablePo();
                        updateOweFeeCallablePo.setOfcId(oweFeeCallablePo.getOfcId());
                        updateOweFeeCallablePo.setCommunityId(oweFeeCallablePo.getCommunityId());
                        updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                        updateOweFeeCallablePo.setRemark(oweFeeCallablePo.getRemark() + "-业主不存在");
                        oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
                        continue;
                    }


                    ownerAppUserDto = new OwnerAppUserDto();
                    ownerAppUserDto.setMemberId(oweFeeCallablePo.getOwnerId());
                    ownerAppUserDto.setCommunityId(oweFeeCallablePo.getCommunityId());
                    ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
                    List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
                    if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
                        userId = ownerAppUserDtos.get(0).getUserId();
                    }

                    oweUrl = FeeDto.PAYER_OBJ_TYPE_ROOM.equals(oweFeeCallablePo.getPayerObjType()) ? oweRoomUrl : oweCarUrl;
                    content = new JSONObject();
                    content.put("feeTypeName", oweFeeCallablePo.getFeeName());
                    content.put("payerObjName", oweFeeCallablePo.getPayerObjName());
                    content.put("billAmountOwed", oweFeeCallablePo.getAmountdOwed());
                    content.put("date", DateUtil.dateTimeToDate(oweFeeCallablePo.getStartTime()) + "~" + DateUtil.dateTimeToDate(oweFeeCallablePo.getEndTime()));
                    content.put("url", oweUrl + oweFeeCallablePo.getPayerObjId());

                    if (OweFeeCallableDto.CALLABLE_WAY_SMS.equals(oweFeeCallablePo.getCallableWay())) {
                        notifyWay = MsgNotifyFactory.NOTIFY_WAY_ALI;
                    }
                    contents.add(content);
                    sendOweFeeCallablePos.add(oweFeeCallablePo);

                }

                if (contents.isEmpty()) {
                    continue;
                }

                //todo 催缴
                ResultVo resultVo = MsgNotifyFactory.sendOweFeeMsg(data.getString("communityId"), userId, ownerId, contents, notifyWay);

                for (OweFeeCallablePo oweFeeCallablePo : sendOweFeeCallablePos) {
                    if (StringUtil.isEmpty(oweFeeCallablePo.getOwnerId()) || oweFeeCallablePo.getOwnerId().startsWith("-")) {
                        continue;
                    }
                    ofcId = oweFeeCallablePo.getOfcId();
                    updateOweFeeCallablePo = new OweFeeCallablePo();
                    updateOweFeeCallablePo.setOfcId(oweFeeCallablePo.getOfcId());
                    updateOweFeeCallablePo.setCommunityId(oweFeeCallablePo.getCommunityId());
                    if (resultVo.getCode() != ResultVo.CODE_OK) {
                        updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                        updateOweFeeCallablePo.setRemark(oweFeeCallablePo.getRemark() + "-" + resultVo.getMsg());
                    } else {
                        updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_COMPLETE);
                    }
                    oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
                }
            }catch (Exception e){
                e.printStackTrace();

                if(!StringUtil.isEmpty(ofcId)){
                    updateOweFeeCallablePo = new OweFeeCallablePo();
                    updateOweFeeCallablePo.setOfcId(ofcId);
                    updateOweFeeCallablePo.setCommunityId(data.getString("communityId"));
                    updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                    updateOweFeeCallablePo.setRemark(e.getMessage());
                    oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
                }

            }

        }
    }

    /**
     * 单个房屋处理
     *
     * @param data
     * @param roomId
     */
    private void doDealRoomId(JSONObject data, String roomId, List<OweFeeCallablePo> oweFeeCallablePos) {

        String communityId = data.getString("communityId");

        String staffId = data.getString("staffId");

        //todo 3.0 查询费用信息
        List<ReportOweFeeDto> feeDtos = getRoomFees(data, roomId);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        //todo 4.0 查询人员信息
        UserDto userDto = getStaffInfo(staffId);
        OweFeeCallablePo oweFeeCallablePo = null;

        for (ReportOweFeeDto reportOweFeeDto : feeDtos) {

            // todo  校验 时间范围
            if (!hasInTime(reportOweFeeDto, data)) {
                continue;
            }
            oweFeeCallablePo = new OweFeeCallablePo();

            oweFeeCallablePo.setAmountdOwed(reportOweFeeDto.getAmountOwed());
            oweFeeCallablePo.setCallableWay(data.getString("callableWay"));
            oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId("11"));
            oweFeeCallablePo.setFeeId(reportOweFeeDto.getFeeId());
            oweFeeCallablePo.setFeeName(reportOweFeeDto.getFeeName());
            oweFeeCallablePo.setCommunityId(communityId);
            oweFeeCallablePo.setConfigId(reportOweFeeDto.getConfigId());
            oweFeeCallablePo.setOwnerId(reportOweFeeDto.getOwnerId());
            oweFeeCallablePo.setOwnerName(reportOweFeeDto.getOwnerName());
            oweFeeCallablePo.setPayerObjId(roomId);
            oweFeeCallablePo.setPayerObjName(reportOweFeeDto.getPayerObjName());
            oweFeeCallablePo.setPayerObjType(reportOweFeeDto.getPayerObjType());
            oweFeeCallablePo.setRemark(data.getString("remark"));
            oweFeeCallablePo.setStaffId(userDto.getUserId());
            oweFeeCallablePo.setStaffName(userDto.getName());
            oweFeeCallablePo.setState(OweFeeCallableDto.STATE_WAIT);
            oweFeeCallablePo.setStartTime(reportOweFeeDto.getEndTime());
            oweFeeCallablePo.setEndTime(reportOweFeeDto.getDeadlineTime());
            oweFeeCallablePos.add(oweFeeCallablePo);
        }


    }

    private UserDto getStaffInfo(String staffId) {

        UserDto userDto = new UserDto();
        userDto.setUserId(staffId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos == null || userDtos.size() < 1) {
            userDto.setUserId("-1");
            userDto.setName("系统");
            return userDto;
        }

        return userDtos.get(0);
    }

    /**
     * 查询房屋费用
     *
     * @return
     */
    private List<ReportOweFeeDto> getRoomFees(JSONObject data, String roomId) {

        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPayerObjId(roomId);
        reportOweFeeDto.setHasOweFee("Y");
        if (data.containsKey("feeId")) {
            reportOweFeeDto.setFeeId(data.getString("feeId"));
        }
        List<String> feeIds = new ArrayList<>();

        if (data.containsKey("feeIds")) {
            JSONArray feeIdAs = data.getJSONArray("feeIds");
            for (int feeIndex = 0; feeIndex < feeIdAs.size(); feeIndex++) {
                feeIds.add(feeIdAs.getString(feeIndex));
            }
            if (feeIds.size() > 0) {
                reportOweFeeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
            }
        }
        List<String> configIdss = new ArrayList<>();
        if (data.containsKey("configIds")) {
            JSONArray configIds = data.getJSONArray("configIds");
            for (int configIndex = 0; configIndex < configIds.size(); configIndex++) {
                configIdss.add(configIds.getString(configIndex));
            }
            if (configIdss.size() > 0) {
                reportOweFeeDto.setConfigIds(configIdss.toArray(new String[configIdss.size()]));
            }
        }
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);

        return reportOweFeeDtos;

    }

    private boolean hasInTime(ReportOweFeeDto tempFeeDto, JSONObject reqJson) {
        if (!reqJson.containsKey("startTime") || !reqJson.containsKey("endTime")) {
            return true;
        }

        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");

        if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
            return true;
        }
        if (StringUtil.isEmpty(tempFeeDto.getDeadlineTime())) {
            return true;
        }
        if (StringUtil.isEmpty(tempFeeDto.getEndTime())) {
            return true;
        }

        if (DateUtil.getDateFromStringB(tempFeeDto.getEndTime()).before(DateUtil.getDateFromStringB(startTime)) && DateUtil.getDateFromStringB(tempFeeDto.getDeadlineTime()).after(DateUtil.getDateFromStringB(endTime))) {
            return true;
        }

        return false;
    }


}
