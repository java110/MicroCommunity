package com.java110.oa.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.oa.dao.IOaWorkflowFormServiceDao;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description OA表单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OaWorkflowFormInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowFormInnerServiceSMO {

    @Autowired
    private IOaWorkflowFormServiceDao oaWorkflowFormServiceDaoImpl;


    @Override
    public int saveOaWorkflowForm(@RequestBody OaWorkflowFormPo oaWorkflowFormPo) {
        int saveFlag = 1;
        oaWorkflowFormServiceDaoImpl.saveOaWorkflowFormInfo(BeanConvertUtil.beanCovertMap(oaWorkflowFormPo));
        return saveFlag;
    }

    @Override
    public int updateOaWorkflowForm(@RequestBody OaWorkflowFormPo oaWorkflowFormPo) {
        int saveFlag = 1;
        oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormInfo(BeanConvertUtil.beanCovertMap(oaWorkflowFormPo));
        return saveFlag;
    }

    @Override
    public int deleteOaWorkflowForm(@RequestBody OaWorkflowFormPo oaWorkflowFormPo) {
        int saveFlag = 1;
        oaWorkflowFormPo.setStatusCd("1");
        oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormInfo(BeanConvertUtil.beanCovertMap(oaWorkflowFormPo));
        return saveFlag;
    }

    @Override
    public List<OaWorkflowFormDto> queryOaWorkflowForms(@RequestBody OaWorkflowFormDto oaWorkflowFormDto) {

        //校验是否传了 分页信息

        int page = oaWorkflowFormDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            oaWorkflowFormDto.setPage((page - 1) * oaWorkflowFormDto.getRow());
        }

        List<OaWorkflowFormDto> oaWorkflowForms = BeanConvertUtil.covertBeanList(oaWorkflowFormServiceDaoImpl.getOaWorkflowFormInfo(BeanConvertUtil.beanCovertMap(oaWorkflowFormDto)), OaWorkflowFormDto.class);

        return oaWorkflowForms;
    }


    @Override
    public int queryOaWorkflowFormsCount(@RequestBody OaWorkflowFormDto oaWorkflowFormDto) {
        return oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormsCount(BeanConvertUtil.beanCovertMap(oaWorkflowFormDto));
    }

    public IOaWorkflowFormServiceDao getOaWorkflowFormServiceDaoImpl() {
        return oaWorkflowFormServiceDaoImpl;
    }

    public void setOaWorkflowFormServiceDaoImpl(IOaWorkflowFormServiceDao oaWorkflowFormServiceDaoImpl) {
        this.oaWorkflowFormServiceDaoImpl = oaWorkflowFormServiceDaoImpl;
    }
}
