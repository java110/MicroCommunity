package com.java110.oa.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * OA流程图组件内部之间使用，没有给外围系统提供服务能力
 * OA流程图服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOaWorkflowXmlServiceDao {


    /**
     * 保存 OA流程图信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOaWorkflowXmlInfo(Map info) throws DAOException;




    /**
     * 查询OA流程图信息（instance过程）
     * 根据bId 查询OA流程图信息
     * @param info bId 信息
     * @return OA流程图信息
     * @throws DAOException DAO异常
     */
    List<Map> getOaWorkflowXmlInfo(Map info) throws DAOException;



    /**
     * 修改OA流程图信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOaWorkflowXmlInfo(Map info) throws DAOException;


    /**
     * 查询OA流程图总数
     *
     * @param info OA流程图信息
     * @return OA流程图数量
     */
    int queryOaWorkflowXmlsCount(Map info);

}
