package com.java110.common.smo.impl;


import com.java110.common.dao.IHcGovTranslateDetailServiceDao;
import com.java110.dto.hcGovTranslate.HcGovTranslateDetailDto;
import com.java110.intf.common.IHcGovTranslateDetailInnerServiceSMO;
import com.java110.po.hcGovTranslateDetail.HcGovTranslateDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 信息分类内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class HcGovTranslateDetailInnerServiceSMOImpl extends BaseServiceSMO implements IHcGovTranslateDetailInnerServiceSMO {

    @Autowired
    private IHcGovTranslateDetailServiceDao hcGovTranslateDetailServiceDaoImpl;


    @Override
    public int saveHcGovTranslateDetail(@RequestBody HcGovTranslateDetailPo hcGovTranslateDetailPo) {
        int saveFlag = 1;
        hcGovTranslateDetailServiceDaoImpl.saveHcGovTranslateDetailInfo(BeanConvertUtil.beanCovertMap(hcGovTranslateDetailPo));
        return saveFlag;
    }

     @Override
    public int updateHcGovTranslateDetail(@RequestBody  HcGovTranslateDetailPo hcGovTranslateDetailPo) {
        int saveFlag = 1;
         hcGovTranslateDetailServiceDaoImpl.updateHcGovTranslateDetailInfo(BeanConvertUtil.beanCovertMap(hcGovTranslateDetailPo));
        return saveFlag;
    }

     @Override
    public int deleteHcGovTranslateDetail(@RequestBody  HcGovTranslateDetailPo hcGovTranslateDetailPo) {
        int saveFlag = 1;
        hcGovTranslateDetailPo.setStatusCd("1");
        hcGovTranslateDetailServiceDaoImpl.updateHcGovTranslateDetailInfo(BeanConvertUtil.beanCovertMap(hcGovTranslateDetailPo));
        return saveFlag;
    }

    @Override
    public List<HcGovTranslateDetailDto> queryHcGovTranslateDetails(@RequestBody  HcGovTranslateDetailDto hcGovTranslateDetailDto) {

        //校验是否传了 分页信息

        int page = hcGovTranslateDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            hcGovTranslateDetailDto.setPage((page - 1) * hcGovTranslateDetailDto.getRow());
        }

        List<HcGovTranslateDetailDto> hcGovTranslateDetails = BeanConvertUtil.covertBeanList(hcGovTranslateDetailServiceDaoImpl.getHcGovTranslateDetailInfo(BeanConvertUtil.beanCovertMap(hcGovTranslateDetailDto)), HcGovTranslateDetailDto.class);

        return hcGovTranslateDetails;
    }


    @Override
    public int queryHcGovTranslateDetailsCount(@RequestBody HcGovTranslateDetailDto hcGovTranslateDetailDto) {
        return hcGovTranslateDetailServiceDaoImpl.queryHcGovTranslateDetailsCount(BeanConvertUtil.beanCovertMap(hcGovTranslateDetailDto));    }

    public IHcGovTranslateDetailServiceDao getHcGovTranslateDetailServiceDaoImpl() {
        return hcGovTranslateDetailServiceDaoImpl;
    }

    public void setHcGovTranslateDetailServiceDaoImpl(IHcGovTranslateDetailServiceDao hcGovTranslateDetailServiceDaoImpl) {
        this.hcGovTranslateDetailServiceDaoImpl = hcGovTranslateDetailServiceDaoImpl;
    }
}
