package com.java110.job.adapt.oa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OA派单
 * <p>
 * 模版ID
 * 3v5Pou3r7KklV9Q8d3fXahjxl-g7zJzxUcRRZ1IiLfk
 * 开发者调用模版消息接口时需提供模版ID
 * 标题
 * 流程待审批通知
 * 行业
 * 房地产 - 物业
 * 详细内容
 * {{first.DATA}}
 * 流程名称：{{keyword1.DATA}}
 * 流程申请人：{{keyword2.DATA}}
 * 流程申请时间：{{keyword3.DATA}}
 * 流程摘要：{{keyword4.DATA}}
 * {{remark.DATA}}
 *
 * @author fqz
 * @date 2021-01-13 13:55
 */
@Component(value = "oaWorkflowDistributeOrder")
public class OaWorkflowDistributeOrder extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowDistributeOrder.class);

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
    private IUserInnerServiceSMO userInnerServiceSMO;


    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {

        JSONObject data = business.getData();
        OaWorkflowDataPo oaWorkflowDataPo = BeanConvertUtil.covertBean(data, OaWorkflowDataPo.class);
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(oaWorkflowDataPo.getStoreId());
        List<CommunityMemberDto> memberDtos = communityInnerServiceSMO.getCommunityMembers(communityMemberDto);
        if(memberDtos == null || memberDtos.size()<1){
            throw new IllegalArgumentException("物业未入住小区");
        }
        //给维修师傅推送信息
        sendMsg(oaWorkflowDataPo, memberDtos.get(0));
    }

    /**
     * 派单给维修师傅推送信息
     *
     * @param oaWorkflowDataPo
     * @param communityMemberDto
     */
    private void sendMsg(OaWorkflowDataPo oaWorkflowDataPo, CommunityMemberDto communityMemberDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityMemberDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityMemberDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE);
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
        staffAppAuthDto.setStaffId(oaWorkflowDataPo.getStaffId());
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            return;
        }
        //查询流程是否存在
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(oaWorkflowDataPo.getStoreId());
        oaWorkflowDto.setFlowId(oaWorkflowDataPo.getFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        //流程表单是否存在
        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(oaWorkflowDataPo.getFlowId());
        oaWorkflowFormDto.setStoreId(oaWorkflowDataPo.getStoreId());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");
        //流程数据是否存在
        Map paramMap = new HashMap();
        paramMap.put("storeId", oaWorkflowDataPo.getStoreId());
        paramMap.put("id", oaWorkflowDataPo.getBusinessKey());
        paramMap.put("tableName", oaWorkflowFormDtos.get(0).getTableName());
        paramMap.put("page", 1);
        paramMap.put("row", 1);
        List<Map> formDatas = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDatas(paramMap);
        Assert.listOnlyOne(formDatas, "工单数据不存在");
        String openId = staffAppAuthDtos.get(0).getOpenId();
        Data data = new Data();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.setFirst(new Content("您有新的OA工单，工单信息如下："));
        data.setKeyword1(new Content(oaWorkflowDtos.get(0).getFlowName()));
        data.setKeyword2(new Content(formDatas.get(0).get("create_user_name").toString()));
        data.setKeyword3(new Content(formDatas.get(0).get("create_time").toString()));
        data.setKeyword4(new Content("请进入工单查看"));
        data.setRemark(new Content("请及时登录公众号接单确认！"));
        templateMessage.setData(data);
        //获取员工公众号地址
        String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

        String createUserId = formDatas.get(0).get("create_user_id").toString();
        if (createUserId.equals(oaWorkflowDataPo.getStaffId())) {
            return;
        }

        //给申请人发消息

        //根据 userId 查询到openId
        staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(createUserId);
        staffAppAuthDto.setAppType("WECHAT");
        staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            return;
        }

        weChatDto = smallWeChatDtos.get(0);
        smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityMemberDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE);
        smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }

        openId = staffAppAuthDtos.get(0).getOpenId();
        data = new Data();
        templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.setFirst(new Content("您好，您有新的流程待处理通知"));
        data.setKeyword1(new Content(oaWorkflowDtos.get(0).getFlowName()));
        data.setKeyword2(new Content(oaWorkflowDataPo.getStaffName()));
        data.setRemark(new Content("请及时查看！"));
        templateMessage.setData(data);
        //获取员工公众号地址
        wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
        templateMessage.setUrl(wechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

    }
}
