package com.java110.oa.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * OA表单组件内部之间使用，没有给外围系统提供服务能力
 * OA表单服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOaWorkflowFormServiceDao {


    /**
     * 保存 OA表单信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOaWorkflowFormInfo(Map info) throws DAOException;




    /**
     * 查询OA表单信息（instance过程）
     * 根据bId 查询OA表单信息
     * @param info bId 信息
     * @return OA表单信息
     * @throws DAOException DAO异常
     */
    List<Map> getOaWorkflowFormInfo(Map info) throws DAOException;



    /**
     * 修改OA表单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOaWorkflowFormInfo(Map info) throws DAOException;


    /**
     * 查询OA表单总数
     *
     * @param info OA表单信息
     * @return OA表单数量
     */
    int queryOaWorkflowFormsCount(Map info);

    /**
     * 查询是否有表
     * @param info
     * @return
     */
    List<Map> hasTable(Map info);

    int createTable(Map info);

    int queryOaWorkflowFormDataCount(Map paramIn);

    List<Map> queryOaWorkflowFormDatas(Map paramIn);

    /**
     * 保存数据
     * @param paramIn
     * @return
     */
    int saveOaWorkflowFormDataInfo(Map paramIn);

    /**
     * 修改表单数据
     * @param beanCovertMap
     * @return
     */
    int updateOaWorkflowFormData(Map beanCovertMap);
    /**
     * 修改表单数据
     * @param beanCovertMap
     * @return
     */
    int updateOaWorkflowFormDataAll(Map beanCovertMap);
}
