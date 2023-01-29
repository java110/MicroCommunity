package com.java110.job.adapt.purchase.purchaseApply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 物品采购、领用申请适配器
 *
 * @author fqz
 * @date 2021-06-03 17:44
 */
@Component(value = "machinePurchaseApplyAdapt")
public class MachinePurchaseApplyAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachinePurchaseApplyAdapt.class);

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void customExchange(CustomBusinessDatabusDto customBusinessDatabusDto) {
        JSONObject data = customBusinessDatabusDto.getData();
        //获取申请id
        String applyOrderId = data.getString("applyOrderId");
        //获取下级处理人id
        String purchaseUserId = data.getString("purchaseUserId");
        //获取出入库状态
        String resOrderType = data.getString("resOrderType");
        //获取状态标识
        String noticeState = data.getString("noticeState");
        //获取拒绝理由
        String auditMessage = data.getString("auditMessage");
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(applyOrderId);
        List<PurchaseApplyDto> purchaseApplys = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplys, "查询物品申请表错误！");
        //获取申请人
        String userName = purchaseApplys.get(0).getUserName();
        //获取申请人id
        String userId = purchaseApplys.get(0).getUserId();
        //获取申请状态
        String state = purchaseApplys.get(0).getState();
        //获取申请状态名称
        String stateName = purchaseApplys.get(0).getStateName();
        //获取申请人小区id
        String communityId = purchaseApplys.get(0).getCommunityId();
        //获取申请时间
        String createTime = purchaseApplys.get(0).getCreateTime();
        JSONObject paramIn = new JSONObject();
        paramIn.put("applyOrderId", applyOrderId);
        paramIn.put("userId", userId);
        paramIn.put("userName", userName);
        paramIn.put("state", state);
        paramIn.put("stateName", stateName);
        paramIn.put("purchaseUserId", purchaseUserId);
        paramIn.put("communityId", communityId);
        paramIn.put("createTime", createTime);
        paramIn.put("resOrderType", resOrderType);
        paramIn.put("noticeState", noticeState);
        paramIn.put("auditMessage", auditMessage);
        sendMessage(paramIn);
    }

    /**
     * 给下级用户推送信息
     *
     * @param paramIn
     */
    private void sendMessage(JSONObject paramIn) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(paramIn.getString("communityId"));
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_PROCESS_TEMPLATE);
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
        staffAppAuthDto.setStaffId(paramIn.getString("purchaseUserId"));
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        //获取出入库状态
        String resOrderType = paramIn.getString("resOrderType");
        //获取状态标识
        String noticeState = paramIn.getString("noticeState");
        if (staffAppAuthDtos.size() > 0) {
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("10000")) {  //采购入库
                data.setFirst(new Content("您有新的采购审批通知，信息如下："));
            } else {  //领用出库
                data.setFirst(new Content("您有新的领用审批通知，信息如下："));
            }
            data.setKeyword1(new Content(paramIn.getString("applyOrderId")));
            if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("10000")) {
                data.setKeyword2(new Content("采购申请"));
            } else {
                data.setKeyword2(new Content("领用申请"));
            }
            data.setKeyword3(new Content(paramIn.getString("userName")));
            if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("10000")) {  //采购入库
                data.setKeyword4(new Content(paramIn.getString("userName") + "提交的采购申请" + "\r\n" + "申请时间：" + paramIn.getString("createTime")));
            } else {
                data.setKeyword4(new Content(paramIn.getString("userName") + "提交的领用申请" + "\r\n" + "申请时间：" + paramIn.getString("createTime")));
            }
            if (!StringUtil.isEmpty(noticeState) && noticeState.equals("1001")) { //审核中
                data.setKeyword5(new Content("审核中"));
            } else if (!StringUtil.isEmpty(noticeState) && noticeState.equals("1004")) {
                data.setKeyword5(new Content("未通过" + "\r\n" + paramIn.getString("auditMessage")));
            } else if (!StringUtil.isEmpty(noticeState) && noticeState.equals("1002")) {
                data.setKeyword5(new Content("已审核"));
            } else {
                data.setKeyword5(new Content(paramIn.getString("stateName")));
            }
            if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("10000") && !StringUtil.isEmpty(noticeState) && noticeState.equals("1004")) {  //采购审核不通过
                data.setRemark(new Content("您的采购申请审核未通过，请结束流程！"));
            } else if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("20000") && !StringUtil.isEmpty(noticeState) && noticeState.equals("1004")) {  //领用审核不通过
                data.setRemark(new Content("您的领用申请审核未通过，请结束流程！"));
            } else if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("10000") && !StringUtil.isEmpty(noticeState) && noticeState.equals("1002")) {
                data.setRemark(new Content("您的采购申请已审核，请结束流程！"));
            } else if (!StringUtil.isEmpty(resOrderType) && resOrderType.equals("20000") && !StringUtil.isEmpty(noticeState) && noticeState.equals("1002")) {
                data.setRemark(new Content("您的领用申请已审核，请结束流程！"));
            } else {
                data.setRemark(new Content("请及时处理！"));
            }
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
