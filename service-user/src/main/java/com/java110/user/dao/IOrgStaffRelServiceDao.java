package com.java110.user.dao;


import com.java110.dto.org.OrgStaffRelDto;
import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 组织员工关系组件内部之间使用，没有给外围系统提供服务能力
 * 组织员工关系服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOrgStaffRelServiceDao {

    /**
     * 保存 组织员工关系信息
     * @param businessOrgStaffRelInfo 组织员工关系信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOrgStaffRelInfo(Map businessOrgStaffRelInfo) throws DAOException;



    /**
     * 查询组织员工关系信息（business过程）
     * 根据bId 查询组织员工关系信息
     * @param info bId 信息
     * @return 组织员工关系信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOrgStaffRelInfo(Map info) throws DAOException;




    /**
     * 保存 组织员工关系信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOrgStaffRelInfoInstance(Map info) throws DAOException;




    /**
     * 查询组织员工关系信息（instance过程）
     * 根据bId 查询组织员工关系信息
     * @param info bId 信息
     * @return 组织员工关系信息
     * @throws DAOException DAO异常
     */
    List<Map> getOrgStaffRelInfo(Map info) throws DAOException;



    /**
     * 修改组织员工关系信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOrgStaffRelInfoInstance(Map info) throws DAOException;


    /**
     * 查询组织员工关系总数
     *
     * @param info 组织员工关系信息
     * @return 组织员工关系数量
     */
    int queryOrgStaffRelsCount(Map info);

    List<OrgStaffRelDto> queryOrgInfoByStaffIds(Map info);

    List<OrgStaffRelDto> queryOrgInfoByStaffIdsNew(Map info);

}
