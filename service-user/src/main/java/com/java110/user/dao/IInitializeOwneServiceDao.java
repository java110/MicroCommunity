package com.java110.user.dao;


import java.util.Map;

/**
 * 业主房屋组件内部之间使用，没有给外围系统提供服务能力
 * 业主房屋服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IInitializeOwneServiceDao {

    /**
     * 修改关系
     *
     * @param communityId
     * @return
     */
    int deleteBuildingOwner(Map communityId);
}
