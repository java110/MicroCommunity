package com.java110.oa.smo.impl;


import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.oa.dao.IOaWorkflowServiceDao;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.po.oaWorkflow.OaWorkflowPo;
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
 * @Description OA工作流内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OaWorkflowInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowInnerServiceSMO {

    @Autowired
    private IOaWorkflowServiceDao oaWorkflowServiceDaoImpl;


    @Override
    public int saveOaWorkflow(@RequestBody OaWorkflowPo oaWorkflowPo) {
        int saveFlag = 1;
        oaWorkflowServiceDaoImpl.saveOaWorkflowInfo(BeanConvertUtil.beanCovertMap(oaWorkflowPo));
        return saveFlag;
    }

     @Override
    public int updateOaWorkflow(@RequestBody  OaWorkflowPo oaWorkflowPo) {
        int saveFlag = 1;
         oaWorkflowServiceDaoImpl.updateOaWorkflowInfo(BeanConvertUtil.beanCovertMap(oaWorkflowPo));
        return saveFlag;
    }

     @Override
    public int deleteOaWorkflow(@RequestBody  OaWorkflowPo oaWorkflowPo) {
        int saveFlag = 1;
        oaWorkflowPo.setStatusCd("1");
        oaWorkflowServiceDaoImpl.updateOaWorkflowInfo(BeanConvertUtil.beanCovertMap(oaWorkflowPo));
        return saveFlag;
    }

    @Override
    public List<OaWorkflowDto> queryOaWorkflows(@RequestBody  OaWorkflowDto oaWorkflowDto) {

        //校验是否传了 分页信息

        int page = oaWorkflowDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            oaWorkflowDto.setPage((page - 1) * oaWorkflowDto.getRow());
        }

        List<OaWorkflowDto> oaWorkflows = BeanConvertUtil.covertBeanList(oaWorkflowServiceDaoImpl.getOaWorkflowInfo(BeanConvertUtil.beanCovertMap(oaWorkflowDto)), OaWorkflowDto.class);

        return oaWorkflows;
    }


    @Override
    public int queryOaWorkflowsCount(@RequestBody OaWorkflowDto oaWorkflowDto) {
        return oaWorkflowServiceDaoImpl.queryOaWorkflowsCount(BeanConvertUtil.beanCovertMap(oaWorkflowDto));    }

    public IOaWorkflowServiceDao getOaWorkflowServiceDaoImpl() {
        return oaWorkflowServiceDaoImpl;
    }

    public void setOaWorkflowServiceDaoImpl(IOaWorkflowServiceDao oaWorkflowServiceDaoImpl) {
        this.oaWorkflowServiceDaoImpl = oaWorkflowServiceDaoImpl;
    }
}
