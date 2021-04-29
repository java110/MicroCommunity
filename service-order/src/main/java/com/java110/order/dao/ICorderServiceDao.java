package com.java110.order.dao;


import com.java110.utils.exception.DAOException;
import com.java110.vo.api.corder.ApiCorderDataVo;

import java.util.List;
import java.util.Map;

/**
 * demo组件内部之间使用，没有给外围系统提供服务能力
 * demo服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICorderServiceDao {



    List<ApiCorderDataVo> getCorderInfo(Map info) throws DAOException;



    int queryCordersCount(Map info);

}
