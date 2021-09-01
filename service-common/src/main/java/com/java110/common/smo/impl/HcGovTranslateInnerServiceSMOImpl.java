package com.java110.common.smo.impl;


import com.java110.common.dao.IHcGovTranslateServiceDao;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 社区政务同步内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class HcGovTranslateInnerServiceSMOImpl extends BaseServiceSMO implements IHcGovTranslateInnerServiceSMO {

    @Autowired
    private IHcGovTranslateServiceDao hcGovTranslateServiceDaoImpl;


    @Override
    public int saveHcGovTranslate(@RequestBody  HcGovTranslatePo hcGovTranslatePo) {
        int saveFlag = 1;
        hcGovTranslateServiceDaoImpl.saveHcGovTranslateInfo(BeanConvertUtil.beanCovertMap(hcGovTranslatePo));
        return saveFlag;
    }

     @Override
    public int updateHcGovTranslate(@RequestBody HcGovTranslatePo hcGovTranslatePo) {
        int saveFlag = 1;
         hcGovTranslateServiceDaoImpl.updateHcGovTranslateInfo(BeanConvertUtil.beanCovertMap(hcGovTranslatePo));
        return saveFlag;
    }

     @Override
    public int deleteHcGovTranslate(@RequestBody  HcGovTranslatePo hcGovTranslatePo) {
        int saveFlag = 1;
        hcGovTranslatePo.setStatusCd("1");
        hcGovTranslateServiceDaoImpl.updateHcGovTranslateInfo(BeanConvertUtil.beanCovertMap(hcGovTranslatePo));
        return saveFlag;
    }

    @Override
    public List<HcGovTranslateDto> queryHcGovTranslates(@RequestBody  HcGovTranslateDto hcGovTranslateDto) {

        //校验是否传了 分页信息

        int page = hcGovTranslateDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            hcGovTranslateDto.setPage((page - 1) * hcGovTranslateDto.getRow());
        }

        List<HcGovTranslateDto> hcGovTranslates = BeanConvertUtil.covertBeanList(hcGovTranslateServiceDaoImpl.getHcGovTranslateInfo(BeanConvertUtil.beanCovertMap(hcGovTranslateDto)), HcGovTranslateDto.class);

        return hcGovTranslates;
    }


    @Override
    public int queryHcGovTranslatesCount(@RequestBody HcGovTranslateDto hcGovTranslateDto) {
        return hcGovTranslateServiceDaoImpl.queryHcGovTranslatesCount(BeanConvertUtil.beanCovertMap(hcGovTranslateDto));    }

    public IHcGovTranslateServiceDao getHcGovTranslateServiceDaoImpl() {
        return hcGovTranslateServiceDaoImpl;
    }

    public void setHcGovTranslateServiceDaoImpl(IHcGovTranslateServiceDao hcGovTranslateServiceDaoImpl) {
        this.hcGovTranslateServiceDaoImpl = hcGovTranslateServiceDaoImpl;
    }
}
