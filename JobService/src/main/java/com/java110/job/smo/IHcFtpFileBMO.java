package com.java110.job.smo;

import com.java110.job.model.*;
import java.util.Map;

public interface IHcFtpFileBMO {

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
     * 更新执行任务的详细日志，包含任务的传输信息,异常信息，当前正在处理的数据信息，线程处理的信息
     * @param 
     * @return Map
     */
	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function);
	
	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info);

	/**
	 * 保存FTP下载的一行数据到表中
	 * @param
	 * @return Map
	 */
	public void insertFileData2Table(String insertSQL);
}