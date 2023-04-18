package com.java110.oa.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.oaWorkflow.OaWorkflowFormDto;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.oa.dao.IOaWorkflowFormServiceDao;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public int hasTable(@RequestBody String table) {
        Map info = new HashMap();
        info.put("tableName", table);
        List<Map> rows = oaWorkflowFormServiceDaoImpl.hasTable(info);
        if (rows == null) {
            return 0;
        }

        return rows.size();
    }

    @Override
    public int createTable(@RequestBody String sql) {
        Map info = new HashMap();
        info.put("sql", sql);
        int flag = oaWorkflowFormServiceDaoImpl.createTable(info);

        return flag;
    }

    @Override
    public int queryOaWorkflowFormDataCount(@RequestBody Map paramIn) {
        return oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormDataCount(paramIn);
    }

    @Override
    public List<Map> queryOaWorkflowFormDatas(@RequestBody Map paramIn) {

        //校验是否传了 分页信息

        int page = (int) paramIn.get("page");

        if (page != PageDto.DEFAULT_PAGE) {
            paramIn.put("page", (page - 1) * (int) paramIn.get("row"));
        }
        List<Map> info = oaWorkflowFormServiceDaoImpl.queryOaWorkflowFormDatas(paramIn);

        return info;
    }

    @Override
    public int saveOaWorkflowFormData(@RequestBody JSONObject reqJson) {
        return oaWorkflowFormServiceDaoImpl.saveOaWorkflowFormDataInfo(reqJson);
    }

    @Override
    public int updateOaWorkflowFormData(@RequestBody JSONObject reqJson) {
        return oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormData(reqJson);
    }

    @Override
    public int updateOaWorkflowFormDataAll(@RequestBody JSONObject reqJson) {
        return oaWorkflowFormServiceDaoImpl.updateOaWorkflowFormDataAll(reqJson);
    }

    public IOaWorkflowFormServiceDao getOaWorkflowFormServiceDaoImpl() {
        return oaWorkflowFormServiceDaoImpl;
    }

    public void setOaWorkflowFormServiceDaoImpl(IOaWorkflowFormServiceDao oaWorkflowFormServiceDaoImpl) {
        this.oaWorkflowFormServiceDaoImpl = oaWorkflowFormServiceDaoImpl;
    }
}
