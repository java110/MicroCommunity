package com.java110.common.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * IOT同步错误日志记录组件内部之间使用，没有给外围系统提供服务能力
 * IOT同步错误日志记录服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IMachineTranslateErrorServiceDao {


    /**
     * 保存 IOT同步错误日志记录信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMachineTranslateErrorInfo(Map info) throws DAOException;




    /**
     * 查询IOT同步错误日志记录信息（instance过程）
     * 根据bId 查询IOT同步错误日志记录信息
     * @param info bId 信息
     * @return IOT同步错误日志记录信息
     * @throws DAOException DAO异常
     */
    List<Map> getMachineTranslateErrorInfo(Map info) throws DAOException;



    /**
     * 修改IOT同步错误日志记录信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateMachineTranslateErrorInfo(Map info) throws DAOException;


    /**
     * 查询IOT同步错误日志记录总数
     *
     * @param info IOT同步错误日志记录信息
     * @return IOT同步错误日志记录数量
     */
    int queryMachineTranslateErrorsCount(Map info);

}
