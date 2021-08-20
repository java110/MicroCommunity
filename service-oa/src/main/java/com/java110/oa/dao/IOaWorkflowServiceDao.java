package com.java110.oa.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * OA工作流组件内部之间使用，没有给外围系统提供服务能力
 * OA工作流服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOaWorkflowServiceDao {


    /**
     * 保存 OA工作流信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOaWorkflowInfo(Map info) throws DAOException;




    /**
     * 查询OA工作流信息（instance过程）
     * 根据bId 查询OA工作流信息
     * @param info bId 信息
     * @return OA工作流信息
     * @throws DAOException DAO异常
     */
    List<Map> getOaWorkflowInfo(Map info) throws DAOException;



    /**
     * 修改OA工作流信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOaWorkflowInfo(Map info) throws DAOException;


    /**
     * 查询OA工作流总数
     *
     * @param info OA工作流信息
     * @return OA工作流数量
     */
    int queryOaWorkflowsCount(Map info);

}
