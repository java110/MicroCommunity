package com.java110.listener.bmo;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/24.
 */
public interface IListenerBmo {


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
}
