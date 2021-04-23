package com.java110.job.dao;

import com.java110.job.model.*;

import java.util.List;
import java.util.Map;

public interface IHcFtpFileDAO {


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
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail);

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function);

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info);

	/**
	 * 执行配置的sql
	 * @param dbsql
	 * @return 000777
	 */
	public List execConfigSql(String dbsql);

	/**
	 * 查询文件下载在文件系统的配置任务列表
	 * @param info
	 * @return   001
	 */
	public Map queryFtpItems(Map info);
	
	
	/**
	 * 保存文件下载配置
	 * @param info
	 * @return  0022
	 */
	public int addFtpItem(Map info);
	
	
	/**
	 * 根据TaskId 查询ftp配置信息
	 * @param info
	 * @return 0004
	 */
	public Map queryFtpItemByTaskId(Map info);
	
	/**
	 * 根据任务名称搜素
	 * @param info
	 * @return 000666
	 */
	public List<Map> searchFtpItemByTaskName(Map info);
	
	
	/**
	 * 修改ftp配置信息
	 * @param info
	 * @return  003
	 */
	public int updateFtpItemByTaskId(Map info);
	
	/**
	 * 删除ftp配置信息
	 * @param info
	 * @return 0006
	 */
	public int deleteFtpItemByTaskId(Map info);
	
	
	/**
	 * 根据taskids 获取将要操作的ftp配置信息 
	 * @param info
	 * @return   002
	 */
	public List<Map> queryFtpItemsByTaskIds(Map info);
	
	/**
	 * 查询FTPItem的属性信息
	 * @param info
	 * @return 0007
	 */
	public List<Map> queryFtpItemAttrsByTaskId(Map info);
	
	/**
	 * 创建taskId
	 * @return   000011
	 */
	public long newCreateTaskId();
	
	/**
	 * 保存FTPItem的属性信息
	 * @return  00333
	 */
	public int addFtpItemAttrs(List<Map> infos);
	
	/**
	 * 查询没有下载过的文件名
	 * @param info
	 * @return  0005
	 */
	public List<Map> queryFileNamesWithOutFtpLog(Map info);


	/**
	 * 查询ItemSpec
	 * @param info
	 * @return 0009
	 */
	public List<Map> queryItemSpec(Map info);
	
	/**
	 * 删除属性
	 * @param info
	 * @return 00044
	 */
	public int deleteFtpItemAttrsbyTaskId(Map info);


	public List queryTableColInfo(String tablename,String colnames);

	/**
	 * 修改ftp配置信息(状态)
	 * @param info
	 * @return
	 */
	public int updateFtpItemRunState(Map info);

	/**
	 * 保存下载文件名称
	 * @param info
	 * @return
	 */
	public int addDownloadFileName(Map info);

	/**
	 * 保存FTP下载的一行数据到表中
	 * @param
	 * @return Map
	 */
	public void insertFileData2Table(String insertSQL);


}