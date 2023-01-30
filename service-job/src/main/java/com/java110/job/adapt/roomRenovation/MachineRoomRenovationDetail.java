package com.java110.job.adapt.roomRenovation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IRoomRenovationsInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 房屋验收通知适配器
 *
 * @author fqz
 * @date 2021-03-01 13:50
 */
@Component(value = "machineRoomRenovationDetail")
public class MachineRoomRenovationDetail extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private IRoomRenovationsInnerServiceSMO roomRenovationsInnerServiceSMO;

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMO;

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

    private static Logger logger = LoggerFactory.getLogger(MachineRoomRenovationDetail.class);

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
        //查询房屋装修验收信息
        RoomRenovationDetailDto roomRenovationDetailDto = new RoomRenovationDetailDto();
        roomRenovationDetailDto.setrId(data.getString("rId"));
        List<RoomRenovationDetailDto> roomRenovationDetailDtos = roomRenovationDetailInnerServiceSMO.queryRoomRenovationDetails(roomRenovationDetailDto);
        //获得联系人信息
        String personName = roomRenovationDtoList.get(0).getPersonName();
        //获得装修房屋号
        String roomName = roomRenovationDtoList.get(0).getRoomName();
        //获取房屋id
        String roomId = data.getString("roomId");
        //获得状态
        String state = data.getString("state");
        //获取状态名称
        String stateName = roomRenovationDtoList.get(0).getStateName();
        //获取当前用户id
        String userId = data.getString("userId");
        String stateMark = "";
        if (state.equals("5000")) {
            stateMark = "通过";
        } else if (state.equals("6000")) {
            stateMark = "不通过";
        }
        //获得验收备注
        String remark = data.getString("remark");
        //获取验收时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(roomRenovationDetailDtos.get(0).getCreateTime());
        JSONObject paramIn = new JSONObject();
        paramIn.put("personName", personName);
        paramIn.put("stateMark", stateMark);
        paramIn.put("state", state);
        paramIn.put("stateName", stateName);
        paramIn.put("remark", remark);
        paramIn.put("createTime", createTime);
        paramIn.put("roomName", roomName);
        paramIn.put("roomId", roomId);
        paramIn.put("userId", userId);
        //给业主推送信息
        sendMessage(paramIn, communityDtos.get(0));
    }

    /**
     * 验收成功/失败给业主推送消息
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_HOUSE_DECORATION_COMPLETED_TEMPLATE);
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
        //获取商户手机号
        String tel = null;
        if (communityDtos != null && communityDtos.size() > 0) {
            tel = communityDtos.get(0).getTel();
        }
        String[] roomNames = paramIn.getString("roomName").split("-",3);
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("尊敬的" + paramIn.getString("personName") + "先生/女士，您好！" + communityDto.getName() + roomNames[0] + "栋" + roomNames[1] + "单元" + roomNames[2] + "室" +
                    "的装修验收申请信息如下："));
            data.setKeyword1(new Content(paramIn.getString("stateName")));
            data.setKeyword2(new Content(paramIn.getString("remark")));
            data.setKeyword3(new Content(paramIn.getString("createTime")));
            data.setRemark(new Content("如有任何疑问，请咨询" + communityDto.getName() + "客服部，咨询电话：" + tel + "。"));
            templateMessage.setData(data);
            //获取业主公众号地址
            String wechatUrl = UrlCache.getOwnerUrl();
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
