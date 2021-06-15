package com.java110.report.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 欠费统计组件内部之间使用，没有给外围系统提供服务能力
 * 欠费统计服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IReportUserQuestionAnswerValueServiceDao {




    /**
     * 查询欠费统计信息（instance过程）
     * 根据bId 查询欠费统计信息
     * @param info bId 信息
     * @return 欠费统计信息
     * @throws DAOException DAO异常
     */
    List<Map> getUserQuestionAnswerValueInfo(Map info) throws DAOException;




    /**
     * 查询欠费统计总数
     *
     * @param info 欠费统计信息
     * @return 欠费统计数量
     */
    int queryUserQuestionAnswerValuesCount(Map info);
}
