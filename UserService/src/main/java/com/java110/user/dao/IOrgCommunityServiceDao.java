package com.java110.user.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 隶属小区组件内部之间使用，没有给外围系统提供服务能力
 * 隶属小区服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOrgCommunityServiceDao {

    /**
     * 保存 隶属小区信息
     * @param businessOrgCommunityInfo 隶属小区信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOrgCommunityInfo(Map businessOrgCommunityInfo) throws DAOException;



    /**
     * 查询隶属小区信息（business过程）
     * 根据bId 查询隶属小区信息
     * @param info bId 信息
     * @return 隶属小区信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOrgCommunityInfo(Map info) throws DAOException;




    /**
     * 保存 隶属小区信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOrgCommunityInfoInstance(Map info) throws DAOException;




    /**
     * 查询隶属小区信息（instance过程）
     * 根据bId 查询隶属小区信息
     * @param info bId 信息
     * @return 隶属小区信息
     * @throws DAOException DAO异常
     */
    List<Map> getOrgCommunityInfo(Map info) throws DAOException;



    /**
     * 修改隶属小区信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOrgCommunityInfoInstance(Map info) throws DAOException;


    /**
     * 查询隶属小区总数
     *
     * @param info 隶属小区信息
     * @return 隶属小区数量
     */
    int queryOrgCommunitysCount(Map info);

}
