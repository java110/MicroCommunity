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
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairSettingInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
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
        System.out.println("收到日志：>>>>>>>>>>>>>"+data.toJSONString());
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
        if (state.equals("10003")) {   //退单状态
            //退单人
            String staffName = repairUserDtos.get(0).getStaffName();
            //退单备注
            String returnContext = repairUserDtos.get(0).getContext();
            //上级操作人
            String preStaffId = repairUserDtos.get(0).getPreStaffId();
            //上级操作人姓名
            String preStaffName = repairUserDtos.get(0).getPreStaffName();
            paramIn.put("repairTypeName", repairTypeName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("staffName", staffName);
            paramIn.put("context", context);
            paramIn.put("returnContext", returnContext);
            paramIn.put("preStaffId", preStaffId);
            paramIn.put("preStaffName", preStaffName);
            sendReturnMessage(paramIn, communityDtos.get(0));
        } else if (state.equals("10002")) {     //结单
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
            //这里建议查询 报修所在费用的 固定费
//            for (Business bus : businesses) {
//                if (bus.getBusinessTypeCd().equals("600100030001")) {  //保存费用信息
//                    JSONObject data = bus.getData();
//                    JSONArray jsonArray = data.getJSONArray("PayFeePo");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    PayFeePo payFeePo = BeanConvertUtil.covertBean(jsonObject, PayFeePo.class);
//                    price = payFeePo.getAmount();
//                }
//            }
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
            publishReturnMsg(paramIn, communityDtos.get(0));
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
                sendMessage(paramIn, communityDtos.get(0));
            }
        }
    }

    /**
     * 退单给管理员推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendReturnMessage(JSONObject paramIn, CommunityDto communityDto) {
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
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        String url = sendMsgUrl + accessToken;
        //根据 userId 查询到openId
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(paramIn.getString("preStaffId"));
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos.size() > 0) {
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content(paramIn.getString("staffName") + "进行维修工单退单操作，请及时处理，信息如下："));
            data.setKeyword1(new Content(paramIn.getString("repairTypeName")));
            if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                data.setKeyword2(new Content(paramIn.getString("repairObjName")));
            } else {
                data.setKeyword2(new Content(communityDto.getName() + paramIn.getString("repairObjName")));
            }
            data.setKeyword3(new Content(paramIn.getString("context")));
            data.setRemark(new Content(paramIn.getString("returnContext")));
            templateMessage.setData(data);
            //获取员工公众号地址
            String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }

    /**
     * 结单给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishReturnMsg(JSONObject paramIn, CommunityDto communityDto) {
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
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        //获取用户id
        String staffId = paramIn.getString("userId");
        if (!StringUtil.isEmpty(staffId)) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setUserId(staffId);
            ownerAppUserDto.setAppType("WECHAT");
            //查询绑定业主
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
            if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
                //获取openId
                String openId = ownerAppUserDtos.get(0).getOpenId();
                String url = sendMsgUrl + accessToken;
                Data data = new Data();
                PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                templateMessage.setTemplate_id(templateId);
                templateMessage.setTouser(openId);
                data.setFirst(new Content("尊敬的" + paramIn.getString("repairName") + "先生/女士，您的报修问题，已经为您处理完毕。"));
                if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                    data.setKeyword1(new Content(paramIn.getString("repairObjName")));
                } else {
                    data.setKeyword1(new Content(communityDto.getName() + paramIn.getString("repairObjName")));
                }
                data.setKeyword2(new Content(paramIn.getString("context")));
                data.setKeyword3(new Content(paramIn.getString("staffName")));
                data.setKeyword4(new Content(paramIn.getString("time")));
                data.setRemark(new Content("请点击查看详情，对我们的工作进行评价，以便提供更优质的服务，感谢您的配合和使用，祝您生活愉快，阖家欢乐！"));
                templateMessage.setData(data);
                //获取业主公众号地址
                String wechatUrl = MappingCache.getValue("OWNER_WECHAT_URL");
                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }

    /**
     * 结单生成费用给业主推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto) {
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
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_REPAIR_CHARGE_SCENE_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        //获取房屋所属业主
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(paramIn.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
        Assert.listOnlyOne(ownerRoomRelDtos, "当前房屋没有所属业主");
        //获取业主id
        String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        //查询绑定业主
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(ownerId);
        ownerAppUserDto.setAppType("WECHAT");
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content(paramIn.getString("roomName") + "缴费信息如下："));
            data.setKeyword1(new Content(paramIn.getString("roomName")));
            data.setKeyword2(new Content(paramIn.getString("repairName")));
            data.setKeyword3(new Content("维修费"));
            data.setKeyword4(new Content(paramIn.getString("feeState")));
            data.setKeyword5(new Content(paramIn.getString("price") + "元"));
            data.setRemark(new Content("请您及时缴费，感谢您的配合和使用，祝您生活愉快，阖家欢乐！"));
            templateMessage.setData(data);
            //获取业主公众号地址
            String wechatUrl = MappingCache.getValue("OWNER_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
