package com.java110.job.msgNotify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WechatTemplateImpl implements IWechatTemplate {

    public static final String industry_id1 = "30"; // 房地产物业
    public static final String industry_id2 = "2"; // IT 科技 IT软件与服务


    private static Logger logger = LoggerFactory.getLogger(WechatTemplateImpl.class);

    /**
     * 获取 行业
     */
    public static final String GET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";


    /**
     * 设置 行业
     */
    public static final String SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";


    /**
     * 获取模板列表
     */
    public static final String GET_ALL_PRIVATE_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";


    /**
     * 添加模板
     */
    public static final String ADD_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";


    public static final String WECHAT_TEMPLATE = "WECHAT_TEMPLATE_";


    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    /**
     * 设置行业为 物业
     *
     * @param communityId 小区ID
     */
    private void setIndustry(String communityId) {

        //todo 查询公众号设置的行业
        String url = GET_INDUSTRY.replace("ACCESS_TOKEN", getAccessToken(communityId));

        ResponseEntity<String> responseEntity = outRestTemplate.getForEntity(url, String.class);

        logger.debug("查询行业返回参数：{}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("获取公众号行业失败");
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());

       // String industryName = paramOut.getJSONObject("primary_industry").getString("second_class");

        JSONObject primaryIndustry = paramOut.getJSONObject("primary_industry");
        if (primaryIndustry != null && "物业".equals(primaryIndustry.getString("second_class"))) { //如果是物业 直接 返回 无需设置
            return;
        }

        //todo 设置公众号设置的行业
        url = SET_INDUSTRY.replace("ACCESS_TOKEN", getAccessToken(communityId));

        JSONObject paramIn = new JSONObject();
        paramIn.put("industry_id1", industry_id1);
        paramIn.put("industry_id2", industry_id2);

        responseEntity = outRestTemplate.postForEntity(url, paramIn.toJSONString(), String.class);

        logger.debug("设置行业返回参数：{}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("设置公众号行业失败");
        }

    }

    /**
     * 获取模板列表
     *
     * @param communityId
     * @return
     */
    private String getAllPrivateTemplate(String communityId) {
        String url = GET_ALL_PRIVATE_TEMPLATE.replace("ACCESS_TOKEN", getAccessToken(communityId));

        ResponseEntity<String> responseEntity = outRestTemplate.getForEntity(url, String.class);

        logger.debug("查询行业返回参数：{}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("获取模板列表失败");
        }

        String templateList = responseEntity.getBody();
        CommonCache.setValue(WECHAT_TEMPLATE + communityId, templateList, CommonCache.TOKEN_EXPIRE_TIME);
        return templateList;
    }

    /**
     * 添加模板
     *
     * @param communityId
     * @param templateIdShort
     * @param keys
     */
    private void addTemplate(String communityId, String templateIdShort, String[] keys) {

        //todo 设置行业
        setIndustry(communityId);

        //todo 设置公众号设置的行业
        String url = ADD_TEMPLATE.replace("ACCESS_TOKEN", getAccessToken(communityId));

        JSONObject paramIn = new JSONObject();
        paramIn.put("template_id_short", templateIdShort);
        paramIn.put("keyword_name_list", keys);

        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, paramIn.toJSONString(), String.class);

        logger.debug("添加模板返回参数：{}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("添加模板失败");
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());

        if (paramOut.getIntValue("errcode") != 0) {
            throw new IllegalArgumentException(paramOut.getString("errmsg"));
        }

    }

    private void deletePrivateTemplate(String communityId, String templateId) {

    }

    /**
     * 获取模板ID
     *
     * @param communityId     小区ID
     * @param templateIdShort 模板库中模板的编号
     * @param title           模板标题
     * @return
     */
    @Override
    public String getTemplateId(String communityId, String templateIdShort, String title, String[] keys) {

        String templateList = CommonCache.getValue(WECHAT_TEMPLATE + communityId);
        //todo 不存在 调用微信查询
        if (StringUtil.isEmpty(templateList)) {
            templateList = getAllPrivateTemplate(communityId);
        }

        //todo 如果还是空 则直接 添加
        if (StringUtil.isEmpty(templateList)) {
            addTemplate(communityId, templateIdShort, keys);
            templateList = getAllPrivateTemplate(communityId);
        }

        //todo 循环校验
        JSONObject templateListObj = JSONObject.parseObject(templateList);
        if (templateListObj == null || !templateListObj.containsKey("template_list") || templateListObj.getJSONArray("template_list").size() < 1) {
            addTemplate(communityId, templateIdShort, keys);
            templateList = getAllPrivateTemplate(communityId);
            templateListObj = JSONObject.parseObject(templateList);
        }

        //todo 寻找 templateId
        JSONArray templateLists = templateListObj.getJSONArray("template_list");
        JSONObject template = null;
        for (int templateIndex = 0; templateIndex < templateLists.size(); templateIndex++) {
            template = templateLists.getJSONObject(templateIndex);
            if (title.equals(template.getString("title")) && templateHasKey(template.getString("content"), keys)) {
                return template.getString("template_id");
            }
        }

        //todo 说明没有寻找到
        addTemplate(communityId, templateIdShort, keys);
        templateList = getAllPrivateTemplate(communityId);
        templateListObj = JSONObject.parseObject(templateList);

        templateLists = templateListObj.getJSONArray("template_list");
        for (int templateIndex = 0; templateIndex < templateLists.size(); templateIndex++) {
            template = templateLists.getJSONObject(templateIndex);
            if (title.equals(template.getString("title")) && templateHasKey(template.getString("content"), keys)) {
                return template.getString("template_id");
            }
        }


        return "-1";
    }

    /**
     * 关键字是否都包含
     * @param content
     * @param keys
     * @return
     */
    private boolean templateHasKey(String content, String[] keys) {
        for (String key : keys) {
            if (!content.contains(key)) {
                return false;
            }
        }

        return true;
    }

    public String getAccessToken(String communityId) {
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            String appIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId");
            String appSecretCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret");
            String mchIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "mchId");
            String keyCache = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key");
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(appIdCache);
            smallWeChatDto.setAppSecret(appSecretCache);
            smallWeChatDto.setMchId(mchIdCache);
            smallWeChatDto.setPayPassword(keyCache);
        } else {
            smallWeChatDto = smallWeChatDtos.get(0);
        }

        String accessToken = WechatFactory.getAccessToken(smallWeChatDto.getAppId(), smallWeChatDto.getAppSecret());

        return accessToken;
    }

    @Override
    public String getAppId(String communityId) {
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            String appIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId");
            String appSecretCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret");
            String mchIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "mchId");
            String keyCache = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key");
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(appIdCache);
            smallWeChatDto.setAppSecret(appSecretCache);
            smallWeChatDto.setMchId(mchIdCache);
            smallWeChatDto.setPayPassword(keyCache);
        } else {
            smallWeChatDto = smallWeChatDtos.get(0);
        }
        return smallWeChatDto.getAppId();
    }
}
