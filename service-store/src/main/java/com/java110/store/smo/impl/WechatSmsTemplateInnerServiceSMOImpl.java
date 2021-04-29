package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.wechatSmsTemplate.WechatSmsTemplateDto;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.store.dao.IWechatSmsTemplateServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 微信消息模板内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WechatSmsTemplateInnerServiceSMOImpl extends BaseServiceSMO implements IWechatSmsTemplateInnerServiceSMO {

    @Autowired
    private IWechatSmsTemplateServiceDao wechatSmsTemplateServiceDaoImpl;


    @Override
    public int saveWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo) {
        int saveFlag = 1;
        wechatSmsTemplateServiceDaoImpl.saveWechatSmsTemplateInfo(BeanConvertUtil.beanCovertMap(wechatSmsTemplatePo));
        return saveFlag;
    }

    @Override
    public int updateWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo) {
        int saveFlag = 1;
        wechatSmsTemplateServiceDaoImpl.updateWechatSmsTemplateInfo(BeanConvertUtil.beanCovertMap(wechatSmsTemplatePo));
        return saveFlag;
    }

    @Override
    public int deleteWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo) {
        int saveFlag = 1;
        wechatSmsTemplatePo.setStatusCd("1");
        wechatSmsTemplateServiceDaoImpl.updateWechatSmsTemplateInfo(BeanConvertUtil.beanCovertMap(wechatSmsTemplatePo));
        return saveFlag;
    }

    @Override
    public List<WechatSmsTemplateDto> queryWechatSmsTemplates(@RequestBody WechatSmsTemplateDto wechatSmsTemplateDto) {

        //校验是否传了 分页信息

        int page = wechatSmsTemplateDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            wechatSmsTemplateDto.setPage((page - 1) * wechatSmsTemplateDto.getRow());
        }

        List<WechatSmsTemplateDto> wechatSmsTemplates = BeanConvertUtil.covertBeanList(wechatSmsTemplateServiceDaoImpl.getWechatSmsTemplateInfo(BeanConvertUtil.beanCovertMap(wechatSmsTemplateDto)), WechatSmsTemplateDto.class);

        return wechatSmsTemplates;
    }


    @Override
    public int queryWechatSmsTemplatesCount(@RequestBody WechatSmsTemplateDto wechatSmsTemplateDto) {
        return wechatSmsTemplateServiceDaoImpl.queryWechatSmsTemplatesCount(BeanConvertUtil.beanCovertMap(wechatSmsTemplateDto));
    }

    public IWechatSmsTemplateServiceDao getWechatSmsTemplateServiceDaoImpl() {
        return wechatSmsTemplateServiceDaoImpl;
    }

    public void setWechatSmsTemplateServiceDaoImpl(IWechatSmsTemplateServiceDao wechatSmsTemplateServiceDaoImpl) {
        this.wechatSmsTemplateServiceDaoImpl = wechatSmsTemplateServiceDaoImpl;
    }
}
