package com.java110.community.dao;


import com.java110.common.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 小区楼组件内部之间使用，没有给外围系统提供服务能力
 * 小区楼服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFloorServiceDao {

    /**
     * 保存 小区楼信息
     * @param businessFloorInfo 小区楼信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessFloorInfo(Map businessFloorInfo) throws DAOException;



    /**
     * 查询小区楼信息（business过程）
     * 根据bId 查询小区楼信息
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException
     */
    public List<Map> getBusinessFloorInfo(Map info) throws DAOException;




    /**
     * 保存 小区楼信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveFloorInfoInstance(Map info) throws DAOException;




    /**
     * 查询小区楼信息（instance过程）
     * 根据bId 查询小区楼信息
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException
     */
    public List<Map> getFloorInfo(Map info) throws DAOException;



    /**
     * 修改小区楼信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateFloorInfoInstance(Map info) throws DAOException;

}
