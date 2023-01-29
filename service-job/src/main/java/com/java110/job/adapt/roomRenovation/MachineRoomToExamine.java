package com.java110.job.adapt.roomRenovation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IRoomRenovationsInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 房屋审核通知适配器
 *
 * @author fqz
 * @date 2021-02-27 17:48
 */
@Component(value = "machineRoomToExamine")
public class MachineRoomToExamine extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private IRoomRenovationsInnerServiceSMO roomRenovationsInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    private static Logger logger = LoggerFactory.getLogger(MachineRoomToExamine.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(data.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        RoomRenovationDto renovationDto = new RoomRenovationDto();
        renovationDto.setrId(data.getString("rId"));
        //查询房屋装修信息
        List<RoomRenovationDto> roomRenovationDtoList = roomRenovationsInnerServiceSMO.queryRoomRenovations(renovationDto);
        //房间号
        String roomName = data.getString("roomName");
        //获取房屋id
        String roomId = data.getString("roomId");
        //装修起始时间
        String startTime = data.getString("startTime");
        String beginTime = startTime.split(" ")[0];
        //装修结束时间
        String endTime = data.getString("endTime");
        String finishTime = endTime.split(" ")[0];
        //装修详情
        String remark = roomRenovationDtoList.get(0).getRemark();
        //获取审核意见
        String examineRemark = data.getString("examineRemark");
        //审核结果
        String state = data.getString("state");
        String stateName = "";
        if (state.equals("3000")) {
            stateName = "已通过";
        } else if (state.equals("2000")) {
            stateName = "未通过";
        }
        //获取当前用户id
        String userId = data.getString("userId");
        JSONObject paramIn = new JSONObject();
        paramIn.put("startTime", beginTime);
        paramIn.put("endTime", finishTime);
        paramIn.put("remark", remark);
        paramIn.put("roomId", roomId);
        paramIn.put("roomName", roomName);
        paramIn.put("state", state);
        paramIn.put("stateName", stateName);
        paramIn.put("examineRemark", examineRemark);
        paramIn.put("userId", userId);
        //给业主推送信息
        sendMessage(paramIn, communityDtos.get(0));
    }

    /**
     * 审核通过/不通过给员工发送消息
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_RESULT_TEMPLATE);
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
        //获取房屋id
        String roomId = paramIn.getString("roomId");
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomId);
        //查询业主房屋关系表
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
        //取得业主id
        String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        //1001 业主本人 1002 家庭成员
        ownerDto.setOwnerTypeCd("1001");
        //查询业主
        List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
        //获得成员id
        String memberId = ownerDtos.get(0).getMemberId();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(memberId);
        ownerAppUserDto.setAppType("WECHAT");
        //查询绑定业主
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        //查询当前绑定业主所属小区
        String communityId = ownerAppUserDtos.get(0).getCommunityId();
        //通过小区id查询小区信息
        CommunityDto community = new CommunityDto();
        community.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(community);
        //获取物业联系方式
        String tel = null;
        if (communityDtos != null && communityDtos.size() > 0) {
            tel = communityDtos.get(0).getTel();
        }
        String[] roomNames = paramIn.getString("roomName").split("-",3);
        //获取状态
        String state = paramIn.getString("state");
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("您好，您的装修申请已处理，请查看："));
            data.setKeyword1(new Content(communityDto.getName() + roomNames[0] + "栋" + roomNames[1] + "单元" + roomNames[2] + "室"));
            data.setKeyword2(new Content(paramIn.getString("startTime") + "至" + paramIn.getString("endTime")));
            data.setKeyword3(new Content(paramIn.getString("remark")));
            if (state.equals("3000")) {
                data.setKeyword4(new Content(paramIn.getString("stateName")));
            } else if (state.equals("2000")) {
                data.setKeyword4(new Content(paramIn.getString("stateName") + "\n" + "审核意见：" + paramIn.getString("examineRemark")));
            }
            data.setRemark(new Content("如有其它问题，请联系" + communityDto.getName() + "客服，联系电话" + tel + "，感谢您的使用。"));
            templateMessage.setData(data);
            //获取员工公众号地址
            String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
