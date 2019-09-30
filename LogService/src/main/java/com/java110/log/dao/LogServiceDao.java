package com.java110.log.dao;

import com.java110.utils.exception.DAOException;

import java.util.Map;

/**
 * 购物车，订单项信息管理
 *
 * 增加，查询
 * 没有查询，理论上购物车信息，不允许删除和修改
 *
 * Created by wuxw on 2016/12/27.
 */
public interface LogServiceDao {

    /**
     * 保存日志
     * 相关表 l_transaction_log
     * @param logMessageParams 日志参数信息
     */
    public void saveTransactionLog(Map logMessageParams) throws DAOException;

    /**
     * 保存日志（交互报文）
     * 相关表 l_transaction_log_message
     * @param logMessageParams 日志参数信息
     */
    public void saveTransactionLogMessage(Map logMessageParams) throws DAOException;
}