package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 报修回访组件内部之间使用，没有给外围系统提供服务能力
 * 报修回访服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IRepairReturnVisitServiceDao {


    /**
     * 保存 报修回访信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveRepairReturnVisitInfo(Map info) throws DAOException;




    /**
     * 查询报修回访信息（instance过程）
     * 根据bId 查询报修回访信息
     * @param info bId 信息
     * @return 报修回访信息
     * @throws DAOException DAO异常
     */
    List<Map> getRepairReturnVisitInfo(Map info) throws DAOException;



    /**
     * 修改报修回访信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateRepairReturnVisitInfo(Map info) throws DAOException;


    /**
     * 查询报修回访总数
     *
     * @param info 报修回访信息
     * @return 报修回访数量
     */
    int queryRepairReturnVisitsCount(Map info);

}
