package com.java110.oa.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.oa.dao.IOaWorkflowXmlServiceDao;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description OA流程图内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OaWorkflowXmlInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowXmlInnerServiceSMO {

    @Autowired
    private IOaWorkflowXmlServiceDao oaWorkflowXmlServiceDaoImpl;


    @Override
    public int saveOaWorkflowXml(@RequestBody OaWorkflowXmlPo oaWorkflowXmlPo) {
        int saveFlag = 1;
        oaWorkflowXmlServiceDaoImpl.saveOaWorkflowXmlInfo(BeanConvertUtil.beanCovertMap(oaWorkflowXmlPo));
        return saveFlag;
    }

    @Override
    public int updateOaWorkflowXml(@RequestBody OaWorkflowXmlPo oaWorkflowXmlPo) {
        int saveFlag = 1;
        oaWorkflowXmlServiceDaoImpl.updateOaWorkflowXmlInfo(BeanConvertUtil.beanCovertMap(oaWorkflowXmlPo));
        return saveFlag;
    }

    @Override
    public int deleteOaWorkflowXml(@RequestBody OaWorkflowXmlPo oaWorkflowXmlPo) {
        int saveFlag = 1;
        oaWorkflowXmlPo.setStatusCd("1");
        oaWorkflowXmlServiceDaoImpl.updateOaWorkflowXmlInfo(BeanConvertUtil.beanCovertMap(oaWorkflowXmlPo));
        return saveFlag;
    }

    @Override
    public List<OaWorkflowXmlDto> queryOaWorkflowXmls(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto) {

        //校验是否传了 分页信息

        int page = oaWorkflowXmlDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            oaWorkflowXmlDto.setPage((page - 1) * oaWorkflowXmlDto.getRow());
        }

        List<OaWorkflowXmlDto> oaWorkflowXmls = BeanConvertUtil.covertBeanList(oaWorkflowXmlServiceDaoImpl.getOaWorkflowXmlInfo(BeanConvertUtil.beanCovertMap(oaWorkflowXmlDto)), OaWorkflowXmlDto.class);

        return oaWorkflowXmls;
    }


    @Override
    public int queryOaWorkflowXmlsCount(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto) {
        return oaWorkflowXmlServiceDaoImpl.queryOaWorkflowXmlsCount(BeanConvertUtil.beanCovertMap(oaWorkflowXmlDto));
    }

    public IOaWorkflowXmlServiceDao getOaWorkflowXmlServiceDaoImpl() {
        return oaWorkflowXmlServiceDaoImpl;
    }

    public void setOaWorkflowXmlServiceDaoImpl(IOaWorkflowXmlServiceDao oaWorkflowXmlServiceDaoImpl) {
        this.oaWorkflowXmlServiceDaoImpl = oaWorkflowXmlServiceDaoImpl;
    }
}
