package com.java110.user.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 旧货市场组件内部之间使用，没有给外围系统提供服务能力
 * 旧货市场服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IJunkRequirementServiceDao {

    /**
     * 保存 旧货市场信息
     * @param businessJunkRequirementInfo 旧货市场信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessJunkRequirementInfo(Map businessJunkRequirementInfo) throws DAOException;



    /**
     * 查询旧货市场信息（business过程）
     * 根据bId 查询旧货市场信息
     * @param info bId 信息
     * @return 旧货市场信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessJunkRequirementInfo(Map info) throws DAOException;




    /**
     * 保存 旧货市场信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveJunkRequirementInfoInstance(Map info) throws DAOException;




    /**
     * 查询旧货市场信息（instance过程）
     * 根据bId 查询旧货市场信息
     * @param info bId 信息
     * @return 旧货市场信息
     * @throws DAOException DAO异常
     */
    List<Map> getJunkRequirementInfo(Map info) throws DAOException;



    /**
     * 修改旧货市场信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateJunkRequirementInfoInstance(Map info) throws DAOException;


    /**
     * 查询旧货市场总数
     *
     * @param info 旧货市场信息
     * @return 旧货市场数量
     */
    int queryJunkRequirementsCount(Map info);

}
