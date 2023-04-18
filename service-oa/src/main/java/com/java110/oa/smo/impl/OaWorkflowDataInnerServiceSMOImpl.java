package com.java110.oa.smo.impl;


import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflow.OaWorkflowDataDto;
import com.java110.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.java110.oa.dao.IOaWorkflowDataServiceDao;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description OA表单审批数据内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OaWorkflowDataInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowDataInnerServiceSMO {

    @Autowired
    private IOaWorkflowDataServiceDao oaWorkflowDataServiceDaoImpl;


    @Override
    @Java110Transactional
    public int saveOaWorkflowData(@RequestBody OaWorkflowDataPo oaWorkflowDataPo) {
        int saveFlag = 1;
        oaWorkflowDataServiceDaoImpl.saveOaWorkflowDataInfo(BeanConvertUtil.beanCovertMap(oaWorkflowDataPo));
        return saveFlag;
    }

    @Override
    public int updateOaWorkflowData(@RequestBody OaWorkflowDataPo oaWorkflowDataPo) {
        int saveFlag = 1;
        oaWorkflowDataServiceDaoImpl.updateOaWorkflowDataInfo(BeanConvertUtil.beanCovertMap(oaWorkflowDataPo));
        return saveFlag;
    }

    @Override
    public int deleteOaWorkflowData(@RequestBody OaWorkflowDataPo oaWorkflowDataPo) {
        int saveFlag = 1;
        oaWorkflowDataPo.setStatusCd("1");
        oaWorkflowDataServiceDaoImpl.updateOaWorkflowDataInfo(BeanConvertUtil.beanCovertMap(oaWorkflowDataPo));
        return saveFlag;
    }

    @Override
    public List<OaWorkflowDataDto> queryOaWorkflowDatas(@RequestBody OaWorkflowDataDto oaWorkflowDataDto) {

        //校验是否传了 分页信息

        int page = oaWorkflowDataDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            oaWorkflowDataDto.setPage((page - 1) * oaWorkflowDataDto.getRow());
        }

        List<OaWorkflowDataDto> oaWorkflowDatas = BeanConvertUtil.covertBeanList(oaWorkflowDataServiceDaoImpl.getOaWorkflowDataInfo(BeanConvertUtil.beanCovertMap(oaWorkflowDataDto)), OaWorkflowDataDto.class);

        return oaWorkflowDatas;
    }


    @Override
    public int queryOaWorkflowDatasCount(@RequestBody OaWorkflowDataDto oaWorkflowDataDto) {
        return oaWorkflowDataServiceDaoImpl.queryOaWorkflowDatasCount(BeanConvertUtil.beanCovertMap(oaWorkflowDataDto));
    }

    public IOaWorkflowDataServiceDao getOaWorkflowDataServiceDaoImpl() {
        return oaWorkflowDataServiceDaoImpl;
    }

    public void setOaWorkflowDataServiceDaoImpl(IOaWorkflowDataServiceDao oaWorkflowDataServiceDaoImpl) {
        this.oaWorkflowDataServiceDaoImpl = oaWorkflowDataServiceDaoImpl;
    }
}
