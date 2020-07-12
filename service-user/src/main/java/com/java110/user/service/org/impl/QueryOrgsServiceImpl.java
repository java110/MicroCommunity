package com.java110.user.service.org.impl;

import com.java110.dto.org.OrgDto;
import com.java110.user.dao.IOrgServiceDao;
import com.java110.user.service.org.IQueryOrgsService;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryOrgsServiceImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/12 20:06
 * @Version 1.0
 * add by wuxw 2020/7/12
 **/
@Service(value = "queryOrgsServiceImpl")
public class QueryOrgsServiceImpl implements IQueryOrgsService {

    @Autowired
    private IOrgServiceDao orgServiceDaoImpl;

    @Override
    public List<OrgDto> queryOrgs(OrgDto orgDto) {
        List<Map> orgs = orgServiceDaoImpl.getOrgInfo(BeanConvertUtil.beanCovertMap(orgDto));
        return BeanConvertUtil.covertBeanList(orgs, OrgDto.class);
    }
}
