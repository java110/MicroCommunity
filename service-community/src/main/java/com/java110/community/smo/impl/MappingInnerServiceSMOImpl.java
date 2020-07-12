package com.java110.community.smo.impl;


import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IMappingServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IMappingInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.mapping.MappingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 映射内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MappingInnerServiceSMOImpl extends BaseServiceSMO implements IMappingInnerServiceSMO {

    @Autowired
    private IMappingServiceDao mappingServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MappingDto> queryMappings(@RequestBody MappingDto mappingDto) {

        //校验是否传了 分页信息

        int page = mappingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            mappingDto.setPage((page - 1) * mappingDto.getRow());
        }

        List<MappingDto> mappings = BeanConvertUtil.covertBeanList(mappingServiceDaoImpl.getMappingInfo(BeanConvertUtil.beanCovertMap(mappingDto)), MappingDto.class);


        return mappings;
    }

    @Override
    public int queryMappingsCount(@RequestBody MappingDto mappingDto) {
        return mappingServiceDaoImpl.queryMappingsCount(BeanConvertUtil.beanCovertMap(mappingDto));
    }

    @Override
    public int updateMapping(@RequestBody MappingDto serviceDto) {
        return mappingServiceDaoImpl.updateMappingInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    @Override
    public int saveMapping(@RequestBody MappingDto serviceDto) {
        return mappingServiceDaoImpl.saveMappingInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    @Override
    public int deleteMapping(@RequestBody MappingDto serviceDto) {
        serviceDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        return mappingServiceDaoImpl.updateMappingInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    public IMappingServiceDao getMappingServiceDaoImpl() {
        return mappingServiceDaoImpl;
    }

    public void setMappingServiceDaoImpl(IMappingServiceDao mappingServiceDaoImpl) {
        this.mappingServiceDaoImpl = mappingServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
