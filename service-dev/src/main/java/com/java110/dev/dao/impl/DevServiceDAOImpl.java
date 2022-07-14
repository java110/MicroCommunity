package com.java110.dev.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dev.dao.IDevServiceDAO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.entity.mapping.Mapping;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 中心服务 数据操作类
 * Created by wuxw on 2018/4/14.
 */
@Service("devServiceDAOImpl")
@Transactional
public class DevServiceDAOImpl extends BaseServiceDao implements IDevServiceDAO {

    protected final static Logger logger = LoggerFactory.getLogger(DevServiceDAOImpl.class);


    @Override
    public List<Map> getAppRouteAndServiceInfoAll() {
        return sqlSessionTemplate.selectList("devServiceDAOImpl.getAppRouteAndServiceInfoAll");
    }


    /**
     * 查询映射表
     *
     * @return
     */
    @Override
    public List<Mapping> getMappingInfoAll() {
        return sqlSessionTemplate.selectList("devServiceDAOImpl.getMappingInfoAll");
    }

    @Override
    public List<BasePrivilegeDto> getPrivilegeAll() {
        return sqlSessionTemplate.selectList("devServiceDAOImpl.getPrivilegeAll");
    }

    @Override
    public List<BusinessDatabusDto> getDatabusAll() {
        return sqlSessionTemplate.selectList("devServiceDAOImpl.getDatabusAll");
    }

    @Override
    public List<BusinessTableHisDto> getBusinessTableHisAll() {
        return sqlSessionTemplate.selectList("devServiceDAOImpl.getBusinessTableHisAll");
    }

}
