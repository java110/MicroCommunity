package com.java110.job.adapt.market.executor;


import com.alibaba.fastjson.JSON;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.market.MarketSmsValueDto;
import com.java110.dto.market.MarketTextDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.common.IMarketSmsValueV1InnerServiceSMO;
import com.java110.job.adapt.market.DefaultSendExecutor;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * https://console.shlianlu.com/#/document/api_4_2
 * <p>
 * 联麓信息
 */
@Service("sendExecutorWechatTemplate")
public class SendExecutorWechatTemplate extends DefaultSendExecutor {

    private static Logger logger = LoggerFactory.getLogger(SendExecutorWechatTemplate.class);


    //短信地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    private static final String APP_ID = "AppId";
    private static final String APP_SECURE = "AppSecure";
    private static final String TEMPLATE_ID = "TemplateId";


    @Autowired
    private IMarketSmsValueV1InnerServiceSMO marketSmsValueV1InnerServiceSMOImpl;

    @Autowired
    private OutRestTemplate outRestTemplate;

    @Override
    public ResultVo doSend(MarketTextDto marketTextDto, String tel, String communityId, String openId) {

        MarketSmsValueDto marketSmsValueDto = new MarketSmsValueDto();
        marketSmsValueDto.setSmsId(marketTextDto.getSmsId());
        List<MarketSmsValueDto> marketSmsValueDtos = marketSmsValueV1InnerServiceSMOImpl.queryMarketSmsValues(marketSmsValueDto);

        if (marketSmsValueDtos == null || marketSmsValueDtos.size() < 1) {
            throw new IllegalArgumentException("未包含 sms 配置信息");
        }

        String sendTemplate = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.SEND_TEMPLATE_URL);
        if (StringUtil.isEmpty(sendTemplate)) {
            sendTemplate = sendMsgUrl;
        }

        String accessToken = WechatFactory.getAccessToken(getMarketValue(marketSmsValueDtos, APP_ID), getMarketValue(marketSmsValueDtos, APP_SECURE));

        String url = sendTemplate + accessToken;

        Data data = new Data();
        data.setFirst(new Content(marketTextDto.getName()));
        data.setKeyword1(new Content(marketTextDto.getName()));
        data.setKeyword2(new Content(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A)));
        data.setKeyword3(new Content(marketTextDto.getTextContent()));
        data.setRemark(new Content("如有疑问请联系相关物业人员"));
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(getMarketValue(marketSmsValueDtos, TEMPLATE_ID));
        templateMessage.setTouser(openId);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);
        //if ("00".equals(result.getString("status"))) {
            return new ResultVo(ResultVo.CODE_OK, "成功", "2002");
        //}


    }

}
