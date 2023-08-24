package com.java110.store.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 仓库组件内部之间使用，没有给外围系统提供服务能力
 * 仓库服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IStorehouseServiceDao {



    /**
     * 查询仓库信息（instance过程）
     * 根据bId 查询仓库信息
     * @param info bId 信息
     * @return 仓库信息
     * @throws DAOException DAO异常
     */
    List<Map> getStorehouseInfo(Map info) throws DAOException;




    /**
     * 查询仓库总数
     *
     * @param info 仓库信息
     * @return 仓库数量
     */
    int queryStorehousesCount(Map info);

}
