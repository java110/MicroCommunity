package com.java110.dev.dao;

import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.system.BusinessDatabusDto;
import com.java110.dto.system.BusinessTableHisDto;
import com.java110.dto.mapping.Mapping;

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
