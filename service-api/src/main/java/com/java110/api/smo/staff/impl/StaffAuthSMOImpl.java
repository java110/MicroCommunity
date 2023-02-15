package com.java110.api.smo.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.staff.IStaffAuthSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * wx登录
 */
@Service("staffAuthSMOImpl")
public class StaffAuthSMOImpl extends DefaultAbstractComponentSMO implements IStaffAuthSMO {

    private final static Logger logger = LoggerFactory.getLogger(StaffAuthSMOImpl.class);

    private final static int expireTime = 7200;

    private final static int LOGIN_PAGE = 1;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> getPageAccessToken(IPageData pd, HttpServletRequest request) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String staffId = paramIn.getString("staffId");
        String storeId = paramIn.getString("storeId");
        String wId = paramIn.getString("wId");
        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wId)) {
            paramIn.put("appId", WechatFactory.getAppId(wId));
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String url = WechatConstant.APP_GET_ACCESS_TOKEN_URL.replace("APPID", smallWeChatDto.getAppId())
                .replace("SECRET", smallWeChatDto.getAppSecret())
                .replace("CODE", authCode);

        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId {}", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");

        }

        JSONObject paramObj = JSONObject.parseObject(paramOut.getBody());
        //获取 openId
        String openId = paramObj.getString("openid");
        url = WechatConstant.APP_GET_USER_INFO_URL
                .replace("ACCESS_TOKEN", paramObj.getString("access_token"))
                .replace("OPENID", openId);

        paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId {}", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");
        }
        paramObj = JSONObject.parseObject(paramOut.getBody());
        JSONObject paramAuth = new JSONObject();
        paramAuth.put("openId", openId);
        paramAuth.put("staffId", staffId);
        paramAuth.put("storeId", storeId);
        paramAuth.put("appType", "WECHAT");
        paramAuth.put("state", "2002");
        paramAuth.put("openName", paramObj.getString("nickname"));

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, paramAuth.toJSONString(),
                "/staff/updateStaffAppAuth", HttpMethod.POST);
        url = UrlCache.getOwnerUrl();
        if(url.contains("/#/")){
            if(url.contains("?")){
                url += ("&wAppId="+smallWeChatDto.getAppId());
            }else{
                url += ("?wAppId="+smallWeChatDto.getAppId());
            }
        }else{
            url += ("/#/?wAppId="+smallWeChatDto.getAppId());
        }
        return ResultVo.redirectPage(url);

    }

    /**
     * 刷新token
     *
     * @param pd
     * @param request
     * @param response
     * @return
     * @throws SMOException
     */
    @Override
    public ResponseEntity<String> refreshToken(IPageData pd, String communityId, String staffId, String storeId,
                                               HttpServletRequest request, HttpServletResponse response) throws SMOException {
        SmallWeChatDto smallWeChatDto = null;
        JSONObject paramIn = new JSONObject();
        paramIn.put("communityId", communityId);
        smallWeChatDto = getSmallWechat(pd, paramIn);
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String openUrl = "";
        String url = UrlCache.getOwnerUrl();
        url = url
                + "/app/loginStaffWechatAuth?appId=992020061452450002&staffId="
                + staffId + "&storeId=" + storeId + "&wId=" + WechatFactory.getWId(smallWeChatDto.getAppId())
        + "&communityId=" + communityId;

        if (url.contains("?")) {
            url += ("&wAppId=" + smallWeChatDto.getAppId());
        } else {
            url += ("?wAppId=" + smallWeChatDto.getAppId());
        }
        try {
            openUrl = WechatConstant.OPEN_AUTH
                    .replace("APPID", smallWeChatDto.getAppId())
                    //.replace("SCOPE", "snsapi_base")
                    .replace("SCOPE", "snsapi_userinfo")
                    .replace(
                            "REDIRECT_URL",
                            URLEncoder
                                    .encode(url,
                                            "UTF-8")).replace("STATE", "1");
            response.sendRedirect(openUrl);
        } catch (Exception e) {
            logger.error("微信公众号鉴权 redirectUrl 错误 " + url, e);
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, e.getLocalizedMessage());
        }

        JSONObject urlObj = new JSONObject();
        urlObj.put("openUrl", openUrl);

        return ResultVo.createResponseEntity(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, urlObj);
    }

    /**
     * 判断 绑定表里是否存在当前 端 绑定信息
     *
     * @param ownerAppUserDtos
     * @param appType
     * @return
     */
    private OwnerAppUserDto judgeCurrentOwnerBind(List<OwnerAppUserDto> ownerAppUserDtos, String appType) {

        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            if (appType.equals(ownerAppUserDto.getAppType())) {
                return ownerAppUserDto;
            }
        }
        return null;
    }

    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?communityId="
                        + paramIn.getString("communityId") + "&page=1&row=1&weChatType=" + SmallWeChatDto.WECHAT_TYPE_PUBLIC, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");

        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }

        return BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
