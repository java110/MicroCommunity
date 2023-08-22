package com.java110.job.msgNotify;

/**
 * 微信模板处理类
 */
public interface IWechatTemplate {


    /**
     * 获取模板ID
     * @param communityId 小区ID
     * @param templateIdShort  模板库中模板的编号
     * @param title 模板标题
     * @return
     */
    String getTemplateId(String communityId, String templateIdShort,String title,String[] keys);

    /**
     * 获取 accessToken
     *
     * @param communityId
     * @return
     */
    String getAccessToken(String communityId);

    String getAppId(String communityId);
}
