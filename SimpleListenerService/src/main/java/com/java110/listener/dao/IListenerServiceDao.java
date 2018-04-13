package com.java110.listener.dao;

import com.java110.entity.listener.FtpTaskLog;
import com.java110.entity.listener.FtpTaskLogDetail;
import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleEntrance;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/23.
 */
public interface IListenerServiceDao {


    /**
     * 查询文件下载在文件系统的配置任务列表
     * @param info
     * @return
     */
    public Map queryFtpItems(Map info) throws Exception;


    /**
     * 保存文件下载配置
     * @param info
     * @return
     */
    public int addFtpItem(Map info) throws Exception;


    /**
     * 根据TaskId 查询ftp配置信息
     * @param info
     * @return
     */
    public Map queryFtpItemByTaskId(Map info) throws Exception;

    /**
     * 根据任务名称搜素
     * @param info
     * @return
     */
    public List<Map> searchFtpItemByTaskName(Map info) throws Exception;


    /**
     * 修改ftp配置信息
     * @param info
     * @return
     */
    public int updateFtpItemByTaskId(Map info) throws Exception;

    /**
     * 删除ftp配置信息
     * @param info
     * @return
     */
    public int deleteFtpItemByTaskId(Map info) throws Exception;


    /**
     * 根据taskids 获取将要操作的ftp配置信息
     * @param info
     * @return
     */
    public List<Map> queryFtpItemsByTaskIds(Map info) throws Exception;

    /**
     * 查询FTPItem的属性信息
     * @param info
     * @return
     */
    public List<Map> queryFtpItemAttrsByTaskId(Map info) throws Exception;

    /**
     * 创建taskId
     * @return
     */
    public long newCreateTaskId();

    /**
     * 保存FTPItem的属性信息
     * @return
     */
    public int addFtpItemAttrs(List<Map> infos) throws Exception;

    /**
     * 查询没有下载过的文件名
     * @param info
     * @return
     */
    public List<Map> queryFileNamesWithOutFtpLog(Map info) throws Exception;

    /**
     * 保存下载文件名称
     * @param info
     * @return
     */
    public int addDownloadFileName(Map info) throws Exception;

    /**
     * 修改ftp配置信息(状态)
     * @param info
     * @return
     */
    public int updateFtpItemRunState(Map info) throws Exception;

    /**
     * 查询ItemSpec
     * @param info
     * @return
     */
    public List<Map> queryItemSpec(Map info) throws Exception;

    /**
     * 删除属性
     * @param info
     * @return
     */
    public int deleteFtpItemAttrsbyTaskId(Map info) throws Exception;


    /**
     * 执行存过，处理任务执行前后的事情
     */
    public void saveDbFunction(String function);


    /**
     * 执行存过(带参数)，处理任务执行前后的事情
     */
    public void saveDbFunctionWithParam(Map info);



    /**
     * 保存执行任务的日志，任务的执行状态
     * @param
     * @return Map
     */
    public long saveTaskRunLog(FtpTaskLog loginfo);
    /**
     * 更新执行任务的日志
     * @param
     * @return Map
     */
    public void updateTaskRunLog(FtpTaskLog loginfo);
    /**
     * 保存执行任务的详细日志，包含任务的传输信息，如果下载线程信息，线程是否执行完成，下载的起始，需要下载的数据大小
     * @param
     * @return Map
     */
    public long saveTaskRunDetailLog(FtpTaskLogDetail logdetail);
    /**
     * 更新执行任务的详细日志，包含任务的传输信息,异常信息，当前正在处理的数据信息，线程处理的信息
     * @param
     * @return Map
     */
    public void updateTaskRunDetailLog(FtpTaskLogDetail logdetail);
    /**
     * 保存FTP下载的一行数据到表中
     * @param
     * @return Map
     */
    public void insertFileData2Table(String insertSQL);

    /**
     * 执行配置的sql
     * @param dbsql
     * @return
     */
    public List execConfigSql(String dbsql);


    /**
     * 取一个表的指定字段的数据类型
     *
     */
    public List queryTableColInfo(String tablename,String colnames);
    public List queryTableColInfo(String username,String tablename,String colnames);
}
