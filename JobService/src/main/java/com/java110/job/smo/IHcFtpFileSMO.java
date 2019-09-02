package com.java110.job.smo;

import com.java110.job.model.*;

import java.util.Map;

/** 
 * FTP任务管理SMO，在此类管理所有事务
 *  
 * @author 师延俊
 * @version
 */  
public interface IHcFtpFileSMO {

	/**
     * 保存执行任务的日志，任务的执行状态
     * @param 
     * @return Map
	 * 00004
     */
	public long saveTaskRunLog(FtpTaskLog loginfo);
	/**
     * 更新执行任务的日志
     * @param 
     * @return Map
	 * 00033
     */
	public void updateTaskRunLog(FtpTaskLog loginfo);
	/**
     * 保存执行任务的详细日志，包含任务的传输信息，如果下载线程信息，线程是否执行完成，下载的起始，需要下载的数据大小
     * @param
     * @return Map  0006
     */
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail);
	/**
	 * 执行存过，处理任务执行前后的事情
	 * 00011
	 */
	public void saveDbFunction(String function);
	
	
	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 * 00002
	 */
	public void saveDbFunctionWithParam(Map info);

	/**
	 * 保存FTP下载的一行数据到表中
	 * @param
	 * @return Map
	 */
	public void insertFileData2Table(String insertSQL);
}