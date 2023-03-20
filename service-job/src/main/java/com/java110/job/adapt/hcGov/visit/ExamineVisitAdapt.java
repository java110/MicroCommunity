package com.java110.job.adapt.hcGov.visit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.visit.VisitDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.*;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.owner.VisitPo;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 访客登记审核适配器
 *
 * @author fqz
 * @date 2022-04-12
 */
@Component(value = "examineVisitAdapt")
public class ExamineVisitAdapt extends DatabusAdaptImpl {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    private static Logger logger = LoggerFactory.getLogger(ExamineVisitAdapt.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessVisits = new JSONArray();
        if (data.containsKey(VisitPo.class.getSimpleName())) {
            Object bObj = data.get(VisitPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessVisits.add(bObj);
            } else if (bObj instanceof List) {
                businessVisits = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessVisits = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessVisits.add(data);
            }
        }
        for (int bVisitIndex = 0; bVisitIndex < businessVisits.size(); bVisitIndex++) {
            JSONObject businessVisit = businessVisits.getJSONObject(bVisitIndex);
            if (!StringUtil.isEmpty(businessVisit.getString("carNum"))) {
                if (!StringUtil.isEmpty(businessVisit.getString("state")) && businessVisit.getString("state").equals("1")) { //访客记录为审核通过状态
                    if (!StringUtil.isEmpty(businessVisit.getString("carState")) &&
                            (businessVisit.getString("carState").equals("1") || businessVisit.getString("carState").equals("2"))) { //车辆必须为审核通过或审核拒绝状态
                        sendMessage(business, businessVisit); //给业主推送消息
                    }
                }
            }
        }
    }

    /**
     * 给业主推送消息
     *
     * @param business
     * @param businessVisit
     */
    private void sendMessage(Business business, JSONObject businessVisit) {
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(businessVisit.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");
        //查询访客登记信息
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(businessVisit.getString("vId"));
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        Assert.listOnlyOne(visitDtos, "查询访客信息错误！");
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(businessVisit.getString("communityId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(businessVisit.getString("communityId"));
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE);
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
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(businessVisit.getString("ownerId"));
        //1001 业主本人 1002 家庭成员
        ownerDto.setOwnerTypeCd("1001");
        //查询业主
        List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在！");
        //获得成员id
        String memberId = ownerDtos.get(0).getMemberId();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(memberId);
        ownerAppUserDto.setAppType("WECHAT");
        //查询绑定业主
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("您提交的访客预约车辆-" + visitDtos.get(0).getCarNum() + "，申请结果如下："));
            data.setKeyword1(new Content("访客预约车辆-审核"));
            if (!StringUtil.isEmpty(visitDtos.get(0).getCarState()) && visitDtos.get(0).getCarState().equals("1")) { //车辆审核状态 0表示未审核；1表示审核通过；2表示审核拒绝；3审核中
                data.setKeyword2(new Content("审核通过"));
                //查询停车位
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setPsId(visitDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");
                //查询停车场
                ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
                parkingAreaDto.setPaId(parkingSpaceDtos.get(0).getPaId());
                List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
                Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
                data.setKeyword3(new Content("请将车辆(" + visitDtos.get(0).getCarNum() + ")停放于" +
                        parkingAreaDtos.get(0).getNum() + "-" + parkingSpaceDtos.get(0).getNum() +
                        "号停车位，" + "停车免费时间截至到" + visitDtos.get(0).getFreeTime()));
                data.setRemark(new Content("访客预约车辆审核已通过，请您合理安排出行！"));
            } else {
                data.setKeyword2(new Content("审核不通过"));
                data.setKeyword3(new Content(visitDtos.get(0).getCarStateRemark()));
                data.setRemark(new Content("访客预约车辆审核未通过，请您合理安排出行！"));
            }
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
