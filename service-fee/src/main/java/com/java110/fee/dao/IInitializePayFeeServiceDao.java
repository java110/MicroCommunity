package com.java110.fee.dao;


import java.util.Map;

/**
 * 缴费审核组件内部之间使用，没有给外围系统提供服务能力
 * 缴费审核服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IInitializePayFeeServiceDao {


    /**
     * 修改关系
     *
     * @param communityId
     * @return
     */
    int deletePayFee(Map communityId);

}
