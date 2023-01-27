package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 合同房屋组件内部之间使用，没有给外围系统提供服务能力
 * 合同房屋服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IContractRoomServiceDao {


    /**
     * 保存 合同房屋信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveContractRoomInfo(Map info) throws DAOException;




    /**
     * 查询合同房屋信息（instance过程）
     * 根据bId 查询合同房屋信息
     * @param info bId 信息
     * @return 合同房屋信息
     * @throws DAOException DAO异常
     */
    List<Map> getContractRoomInfo(Map info) throws DAOException;



    /**
     * 修改合同房屋信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateContractRoomInfo(Map info) throws DAOException;


    /**
     * 查询合同房屋总数
     *
     * @param info 合同房屋信息
     * @return 合同房屋数量
     */
    int queryContractRoomsCount(Map info);

    List<Map> queryContractByRoomIds(Map info);
}
