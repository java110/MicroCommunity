package com.java110.fee.dao;

import java.util.List;
import java.util.Map;

/**
 * 抵扣明细接口类
 *
 * @author fqz
 * @date 2022-05-16
 */
public interface IFeeAccountDetailServiceDao {

    List<Map> getFeeAccountDetailsInfo(Map info);

    int queryFeeAccountDetailsCount(Map info);

    void saveFeeAccountDetail(Map info);

}
