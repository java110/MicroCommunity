package com.java110.common.smo.impl;


import com.java110.common.dao.ISysDocumentServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.sysDocument.SysDocumentDto;
import com.java110.intf.common.ISysDocumentInnerServiceSMO;
import com.java110.po.sysDocument.SysDocumentPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 系统文档内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class SysDocumentInnerServiceSMOImpl extends BaseServiceSMO implements ISysDocumentInnerServiceSMO {

    @Autowired
    private ISysDocumentServiceDao sysDocumentServiceDaoImpl;


    @Override
    public int saveSysDocument(@RequestBody SysDocumentPo sysDocumentPo) {
        int saveFlag = 1;
        sysDocumentServiceDaoImpl.saveSysDocumentInfo(BeanConvertUtil.beanCovertMap(sysDocumentPo));
        return saveFlag;
    }

    @Override
    public int updateSysDocument(@RequestBody SysDocumentPo sysDocumentPo) {
        int saveFlag = 1;
        sysDocumentServiceDaoImpl.updateSysDocumentInfo(BeanConvertUtil.beanCovertMap(sysDocumentPo));
        return saveFlag;
    }

    @Override
    public int deleteSysDocument(@RequestBody SysDocumentPo sysDocumentPo) {
        int saveFlag = 1;
        sysDocumentPo.setStatusCd("1");
        sysDocumentServiceDaoImpl.updateSysDocumentInfo(BeanConvertUtil.beanCovertMap(sysDocumentPo));
        return saveFlag;
    }

    @Override
    public List<SysDocumentDto> querySysDocuments(@RequestBody SysDocumentDto sysDocumentDto) {

        //校验是否传了 分页信息

        int page = sysDocumentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            sysDocumentDto.setPage((page - 1) * sysDocumentDto.getRow());
        }

        List<SysDocumentDto> sysDocuments = BeanConvertUtil.covertBeanList(sysDocumentServiceDaoImpl.getSysDocumentInfo(BeanConvertUtil.beanCovertMap(sysDocumentDto)), SysDocumentDto.class);

        return sysDocuments;
    }


    @Override
    public int querySysDocumentsCount(@RequestBody SysDocumentDto sysDocumentDto) {
        return sysDocumentServiceDaoImpl.querySysDocumentsCount(BeanConvertUtil.beanCovertMap(sysDocumentDto));
    }

    public ISysDocumentServiceDao getSysDocumentServiceDaoImpl() {
        return sysDocumentServiceDaoImpl;
    }

    public void setSysDocumentServiceDaoImpl(ISysDocumentServiceDao sysDocumentServiceDaoImpl) {
        this.sysDocumentServiceDaoImpl = sysDocumentServiceDaoImpl;
    }
}
