package com.java110.dev.dao;

import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.entity.mapping.Mapping;
import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2018/4/14.
 */
public interface IDevServiceDAO {

    /**
     * 查询所有组件
     *
     * @return
     */
    public List<Map> getAppRouteAndServiceInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<Mapping> getMappingInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<BasePrivilegeDto> getPrivilegeAll();
    /**
     * 查询映射表
     *
     * @return
     */
    public List<BusinessDatabusDto> getDatabusAll();
    /**
     * 查询映射表
     *
     * @return
     */
    public List<BusinessTableHisDto> getBusinessTableHisAll();




}
