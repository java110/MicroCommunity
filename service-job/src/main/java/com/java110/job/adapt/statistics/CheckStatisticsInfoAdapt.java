package com.java110.job.adapt.statistics;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.system.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 数据统计适配器
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "checkStatisticsInfoAdapt")
public class CheckStatisticsInfoAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(CheckStatisticsInfoAdapt.class);

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

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public final static String ALI_SMS_DOMAIN = "ALI_SMS";

    @Override
    public void execute(Business business, List<Business> businesses) {
        
    }

    private void doSendPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {

    }
}
