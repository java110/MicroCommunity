package com.java110.common.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 社区政务同步组件内部之间使用，没有给外围系统提供服务能力
 * 社区政务同步服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IHcGovTranslateServiceDao {


    /**
     * 保存 社区政务同步信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveHcGovTranslateInfo(Map info) throws DAOException;




    /**
     * 查询社区政务同步信息（instance过程）
     * 根据bId 查询社区政务同步信息
     * @param info bId 信息
     * @return 社区政务同步信息
     * @throws DAOException DAO异常
     */
    List<Map> getHcGovTranslateInfo(Map info) throws DAOException;



    /**
     * 修改社区政务同步信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateHcGovTranslateInfo(Map info) throws DAOException;


    /**
     * 查询社区政务同步总数
     *
     * @param info 社区政务同步信息
     * @return 社区政务同步数量
     */
    int queryHcGovTranslatesCount(Map info);

}
