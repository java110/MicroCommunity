package com.java110.job.smo.impl;

import com.java110.job.dao.IPrvncFtpFileDAO;
import com.java110.job.model.*;
import com.java110.job.smo.IPrvncFtpFileBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service("prvncFtpFileBMOImpl")
@Transactional
public class PrvncFtpFileBMOImpl implements IPrvncFtpFileBMO {
	@Autowired
	private IPrvncFtpFileDAO iprvncFtpFileDAO;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return iprvncFtpFileDAO.saveTaskRunLog(loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		iprvncFtpFileDAO.updateTaskRunLog(loginfo);
	}
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return iprvncFtpFileDAO.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		iprvncFtpFileDAO.saveDbFunction(function);
	}

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		iprvncFtpFileDAO.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		iprvncFtpFileDAO.insertFileData2Table(insertSQL);
	}
	
}