package com.java110.user.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 答卷选项组件内部之间使用，没有给外围系统提供服务能力
 * 答卷选项服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IQuestionAnswerTitleValueServiceDao {


    /**
     * 保存 答卷选项信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveQuestionAnswerTitleValueInfo(Map info) throws DAOException;




    /**
     * 查询答卷选项信息（instance过程）
     * 根据bId 查询答卷选项信息
     * @param info bId 信息
     * @return 答卷选项信息
     * @throws DAOException DAO异常
     */
    List<Map> getQuestionAnswerTitleValueInfo(Map info) throws DAOException;



    /**
     * 修改答卷选项信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateQuestionAnswerTitleValueInfo(Map info) throws DAOException;


    /**
     * 查询答卷选项总数
     *
     * @param info 答卷选项信息
     * @return 答卷选项数量
     */
    int queryQuestionAnswerTitleValuesCount(Map info);

    /**
     * 查询问卷结果
     * @param info
     * @return
     */
    List<Map> queryQuestionAnswerTitleValueResult(Map info);

    /**
     * 查询问卷结果选择总人数
     * @param info
     * @return
     */
    List<Map> queryQuestionAnswerTitleValueResultCount(Map info);
}
