package com.java110.common.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 考勤班次组件内部之间使用，没有给外围系统提供服务能力
 * 考勤班次服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAttendanceClassesServiceDao {

    /**
     * 保存 考勤班次信息
     * @param businessAttendanceClassesInfo 考勤班次信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAttendanceClassesInfo(Map businessAttendanceClassesInfo) throws DAOException;



    /**
     * 查询考勤班次信息（business过程）
     * 根据bId 查询考勤班次信息
     * @param info bId 信息
     * @return 考勤班次信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAttendanceClassesInfo(Map info) throws DAOException;




    /**
     * 保存 考勤班次信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAttendanceClassesInfoInstance(Map info) throws DAOException;




    /**
     * 查询考勤班次信息（instance过程）
     * 根据bId 查询考勤班次信息
     * @param info bId 信息
     * @return 考勤班次信息
     * @throws DAOException DAO异常
     */
    List<Map> getAttendanceClassesInfo(Map info) throws DAOException;



    /**
     * 修改考勤班次信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAttendanceClassesInfoInstance(Map info) throws DAOException;


    /**
     * 查询考勤班次总数
     *
     * @param info 考勤班次信息
     * @return 考勤班次数量
     */
    int queryAttendanceClassessCount(Map info);

}
