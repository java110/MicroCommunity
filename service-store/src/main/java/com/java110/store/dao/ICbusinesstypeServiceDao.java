package com.java110.store.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * cbusinesstype组件内部之间使用，没有给外围系统提供服务能力
 * cbusinesstype服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICbusinesstypeServiceDao {

    /**
     * 保存 cbusinesstype信息
     * @param businessCbusinesstypeInfo cbusinesstype信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCbusinesstypeInfo(Map businessCbusinesstypeInfo) throws DAOException;



    /**
     * 查询cbusinesstype信息（business过程）
     * 根据bId 查询cbusinesstype信息
     * @param info bId 信息
     * @return cbusinesstype信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessCbusinesstypeInfo(Map info) throws DAOException;




    /**
     * 保存 cbusinesstype信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCbusinesstypeInfoInstance(Map info) throws DAOException;




    /**
     * 查询cbusinesstype信息（instance过程）
     * 根据bId 查询cbusinesstype信息
     * @param info bId 信息
     * @return cbusinesstype信息
     * @throws DAOException DAO异常
     */
    List<Map> getCbusinesstypeInfo(Map info) throws DAOException;



    /**
     * 修改cbusinesstype信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCbusinesstypeInfoInstance(Map info) throws DAOException;


    /**
     * 查询cbusinesstype总数
     *
     * @param info cbusinesstype信息
     * @return cbusinesstype数量
     */
    int queryCbusinesstypesCount(Map info);

}
