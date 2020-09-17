package com.java110.front.configuration;

import com.java110.front.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/5/2.
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public FilterRegistrationBean jwtFilter() {
        StringBuffer exclusions = new StringBuffer();
        exclusions.append("/callComponent/login/*,");
        exclusions.append("/callComponent/register/*,");
        exclusions.append("/callComponent/validate-code/*,");
        exclusions.append("/callComponent/validate-tel/*,");
        exclusions.append("/flow/login,");
        exclusions.append("/flow/register,");
        exclusions.append("/flow/advertVedioFlow,");//放开 广告页面是不需要登录的
        exclusions.append("/callComponent/advertVedioView/*,");//放开 广告页面是不需要登录的
        exclusions.append("/callComponent/download/getFile/file,");//放开 下载图片也不需要登录
        exclusions.append("/callComponent/download/getFile/fileByObjId,");//放开 下载图片也不需要登录
        exclusions.append("/app/payment/notify,");//微信支付通知
        exclusions.append("/app/payment/rentingNotify,");//微信支付通知
        exclusions.append("/app/loginWx,");// 登录跳过
        exclusions.append("/app/getWxPhoto,");// 登录跳过
        exclusions.append("/app/loginProperty,");// 物业APP登录跳过
        exclusions.append("/app/loginOwner,");// 业主APP登录跳过
        exclusions.append("/app/loginOwnerByKey,");// 根据key登录业主
        exclusions.append("/app/area.listAreas,");// 加载地区
        exclusions.append("/app/community.listCommunitys,");// 加载小区
        exclusions.append("/app/user.userSendSms,");// 发送短信验证码
        exclusions.append("/app/owner.ownerRegister,");// 业主注册
        exclusions.append("/app/owner.ownerRegisterWxPhoto,");// 业主注册
        exclusions.append("/app/activities.listActivitiess,");//小区广告
        exclusions.append("/app/advert.listAdvertPhoto,");//小区广告图片
        exclusions.append("/app/junkRequirement.listJunkRequirements,");//市场
        exclusions.append("/app/wechat/gateway,");//微信公众号对接接口
        exclusions.append("/app/loginOwnerWechatAuth,");//微信公众号对接接口
        exclusions.append("/app/refreshToken");//微信公众号对接接口

        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/");
        registrationBean.addUrlPatterns("/callComponent/*");
        registrationBean.addUrlPatterns("/flow/*");
        registrationBean.addUrlPatterns("/app/*");
        registrationBean.addInitParameter("excludedUri",exclusions.toString());

        return registrationBean;
    }

}
