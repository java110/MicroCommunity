package com.java110.job.adapt.oa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflow.OaWorkflowFormDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.system.Business;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
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
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.oaWorkflow.OaWorkflowDataPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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

        JSONObject content = new JSONObject();
        content.put("flowName", oaWorkflowDtos.get(0).getFlowName());
        content.put("create_user_name", formDatas.get(0).get("create_user_name").toString());
        content.put("create_time", formDatas.get(0).get("create_time").toString());
        content.put("date", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        content.put("orderId", oaWorkflowDataPo.getBusinessKey());

        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        MsgNotifyFactory.sendOaDistributeMsg(communityMemberDto.getCommunityId(), oaWorkflowDataPo.getStaffId(), content);

        String createUserId = formDatas.get(0).get("create_user_id").toString();
        if (createUserId.equals(oaWorkflowDataPo.getStaffId())) {
            return;
        }

        //todo 给申请人发消息
        content = new JSONObject();
        content.put("flowName", oaWorkflowDtos.get(0).getFlowName());
        content.put("staffName", oaWorkflowDataPo.getStaffName());
        content.put("orderId", oaWorkflowDataPo.getBusinessKey());
        wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        MsgNotifyFactory.sendOaCreateStaffMsg(communityMemberDto.getCommunityId(), createUserId, content);

    }
}
