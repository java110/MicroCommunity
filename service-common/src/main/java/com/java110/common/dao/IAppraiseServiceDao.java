package com.java110.common.dao;

import java.util.Map;

public interface IAppraiseServiceDao {

    /**
     * 保存评价
     * @param info
     * @return
     */
    int saveAppraise(Map info);
}
