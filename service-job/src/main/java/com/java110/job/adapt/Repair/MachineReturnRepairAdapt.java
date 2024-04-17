package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.system.Business;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairSettingInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 退单(结单)通知适配器
 *
 * @author fqz
 * @date 2021-01-18 10:22
 */
@Component(value = "machineReturnRepairAdapt")
public class MachineReturnRepairAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineReturnRepairAdapt.class);

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private IRepairSettingInnerServiceSMO repairSettingInnerServiceSMO;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) throws Exception {
        JSONObject data = business.getData();
        JSONArray businessRepairUsers = new JSONArray();
        System.out.println("收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        if (data.containsKey(RepairUserPo.class.getSimpleName())) {
            Object bObj = data.get(RepairUserPo.class.getSimpleName());

            if (bObj instanceof JSONObject) {
                businessRepairUsers.add(bObj);
            } else if (bObj instanceof List) {
                businessRepairUsers = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessRepairUsers = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessRepairUsers.add(data);
            }
        }
        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bOwnerRepairIndex = 0; bOwnerRepairIndex < businessRepairUsers.size(); bOwnerRepairIndex++) {
            JSONObject businessRepairUser = businessRepairUsers.getJSONObject(bOwnerRepairIndex);
            doDealOwnerRepair(business, businessRepairUser);
        }
    }

    private void doDealOwnerRepair(Business business, JSONObject businessRepairUser) {
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRuId(businessRepairUser.getString("ruId"));
        repairUserDto.setStatusCd("0");
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理订单");
        //获取状态
        String state = repairUserDtos.get(0).getState();
        //获取报修id
        String repairId = repairUserDtos.get(0).getRepairId();
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        repairDto.setStatusCd("0");
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);

        Assert.listOnlyOne(repairDtos, "不存在这条报修信息");

        IMsgNotify msgNotify = null;
        if(RepairSettingDto.NOTIFY_WAY_SMS.equals(repairDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
        }else if(RepairSettingDto.NOTIFY_WAY_WECHAT.equals(repairDtos.get(0).getNotifyWay())){
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        }else{
            return;
        }

        //获取报修人姓名
        String repairName = repairDtos.get(0).getRepairName();
        //获取报修内容
        String context = repairDtos.get(0).getContext();
        //获取报修类型名
        String repairTypeName = repairDtos.get(0).getRepairTypeName();
        //报修地址
        String repairObjName = repairDtos.get(0).getRepairObjName();
        //获取小区id
        String communityId = repairUserDtos.get(0).getCommunityId();
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        //获取报修类型
        String repairType = repairDtos.get(0).getRepairType();
        RepairSettingDto repairSettingDto = new RepairSettingDto();
        repairSettingDto.setSettingId(repairType);
        List<RepairSettingDto> repairSettingDtos = repairSettingInnerServiceSMO.queryRepairSettings(repairSettingDto);
        Assert.listOnlyOne(repairSettingDtos, "信息错误");
        JSONObject paramIn = new JSONObject();
        if (RepairUserDto.STATE_BACK.equals(state)) {   //退单状态
            //退单人
            String staffName = repairUserDtos.get(0).getStaffName();
            //退单备注
            String returnContext = repairUserDtos.get(0).getContext();
            //上级操作人
            String preStaffId = repairUserDtos.get(0).getPreStaffId();
            //上级操作人姓名
            String preStaffName = repairUserDtos.get(0).getPreStaffName();
            paramIn.put("repairId",  repairUserDtos.get(0).getRepairId());
            paramIn.put("repairTypeName", repairTypeName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("staffName", staffName);
            paramIn.put("context", context);
            paramIn.put("returnContext", returnContext);
            paramIn.put("preStaffId", preStaffId);
            paramIn.put("preStaffName", preStaffName);
            paramIn.put("repairName", repairUserDtos.get(0).getRepairName());
            sendReturnMessage(paramIn, communityDtos.get(0),msgNotify);
        } else if (RepairUserDto.STATE_CLOSE.equals(state)) {     //结单
            //获取用户id
            String preStaffId = repairUserDtos.get(0).getPreStaffId();
            //获取维修师傅姓名
            String staffName = repairUserDtos.get(0).getStaffName();
            //获取报修对象id
            String repairObjId = repairDtos.get(0).getRepairObjId();
            //获取结单日期
            Date endTime = repairUserDtos.get(0).getEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(endTime);
            //获取价格
            String price = "";
            paramIn.put("repairName", repairName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("context", context);
            paramIn.put("staffName", staffName);
            paramIn.put("time", time);
            paramIn.put("repairObjId", repairObjId);
            paramIn.put("preStaffId", preStaffId);
            RepairUserDto repairUser = new RepairUserDto();
            repairUser.setRepairId(repairId);
            repairUser.setState("10005");
            //查询报修用户记录
            List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUser);
            //获取报修用户id
            String staffId = repairUserDtoList.get(0).getStaffId();
            paramIn.put("userId", staffId);
            sendFinishRepairOwnerMsg(paramIn, communityDtos.get(0),msgNotify);
            if (!StringUtil.isEmpty(price)) {
                paramIn.put("price", price);
                paramIn.put("feeState", "未缴费");
                //获取付款方id
                String roomId = repairDtos.get(0).getRepairObjId();
                //获取付款方名称
                String roomName = repairDtos.get(0).getRepairObjName();
                paramIn.put("roomId", roomId);
                paramIn.put("roomName", roomName);
                //生成费用给业主发送信息
                sendMessage(paramIn, communityDtos.get(0),msgNotify);
            }
        }
    }

    /**
     * 退单给管理员推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendReturnMessage(JSONObject paramIn, CommunityDto communityDto,IMsgNotify msgNotify) {

        JSONObject content = new JSONObject();
        content.put("repairTypeName", paramIn.getString("repairTypeName"));
        content.put("repairId", paramIn.getString("repairId"));
        if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
            content.put("repairObjName", paramIn.getString("repairObjName"));
        } else {
            content.put("repairObjName", communityDto.getName() + paramIn.getString("repairObjName"));
        }
        content.put("repairName", paramIn.getString("repairName"));
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);

        msgNotify.sendReturnRepairMsg(communityDto.getCommunityId(), paramIn.getString("preStaffId"), content);

    }

    /**
     * 结单给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendFinishRepairOwnerMsg(JSONObject paramIn, CommunityDto communityDto,IMsgNotify msgNotify) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }

        JSONObject content = new JSONObject();
        content.put("repairObjName", paramIn.getString("repairObjName"));
        content.put("staffName", paramIn.getString("staffName"));
        content.put("time", paramIn.getString("time"));
        String wechatUrl = UrlCache.getOwnerUrl();
        if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
            wechatUrl += ("&wAppId=" + smallWeChatDtos.get(0).getAppId());
        } else {
            wechatUrl += ("?wAppId=" + smallWeChatDtos.get(0).getAppId());
        }
        content.put("url", wechatUrl);

        msgNotify.sendFinishRepairOwnerMsg(communityDto.getCommunityId(), paramIn.getString("userId"), content);


    }

    /**
     * 结单生成费用给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto,IMsgNotify msgNotify) {

        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }

        //获取房屋所属业主
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(paramIn.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
        Assert.listOnlyOne(ownerRoomRelDtos, "当前房屋没有所属业主");
        //获取业主id
        String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        String userId = "";
        //查询绑定业主
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(ownerId);
        ownerAppUserDto.setAppType("WECHAT");
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            userId = ownerAppUserDtos.get(0).getUserId();
        }
        List<JSONObject> contents = new ArrayList<>();
        JSONObject content = new JSONObject();
        content.put("feeTypeName", "维修费");
        content.put("payerObjName", paramIn.getString("roomName"));
        content.put("billAmountOwed", paramIn.getString("price") + "元");
        content.put("date", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        String wechatUrl = UrlCache.getOwnerUrl();
        if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
            wechatUrl += ("&wAppId=" + smallWeChatDtos.get(0).getAppId());
        } else {
            wechatUrl += ("?wAppId=" + smallWeChatDtos.get(0).getAppId());
        }
        content.put("url", wechatUrl);
        contents.add(content);
        msgNotify.sendOweFeeMsg(communityDto.getCommunityId(), userId, ownerId, contents);

    }
}
