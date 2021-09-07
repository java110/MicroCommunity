package com.java110.user.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 业主属性组件内部之间使用，没有给外围系统提供服务能力
 * 业主属性服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerAttrServiceDao {

    /**
     * 保存 业主属性信息
     * @param businessOwnerAttrInfo 业主属性信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerAttrInfo(Map businessOwnerAttrInfo) throws DAOException;



    /**
     * 查询业主属性信息（business过程）
     * 根据bId 查询业主属性信息
     * @param info bId 信息
     * @return 业主属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerAttrInfo(Map info) throws DAOException;




    /**
     * 保存 业主属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerAttrInfoInstance(Map info) throws DAOException;




    /**
     * 查询业主属性信息（instance过程）
     * 根据bId 查询业主属性信息
     * @param info bId 信息
     * @return 业主属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getOwnerAttrInfo(Map info) throws DAOException;



    /**
     * 修改业主属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateOwnerAttrInfoInstance(Map info) throws DAOException;


    /**
     * 查询业主属性总数
     *
     * @param info 业主属性信息
     * @return 业主属性数量
     */
    int queryOwnerAttrsCount(Map info);

    int saveOwnerAttr(Map beanCovertMap);
}
