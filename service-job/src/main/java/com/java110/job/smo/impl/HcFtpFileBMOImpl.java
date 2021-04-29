package com.java110.job.smo.impl;

import com.java110.job.dao.IHcFtpFileDAO;
import com.java110.job.model.*;
import com.java110.job.smo.IHcFtpFileBMO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
@Service("hcFtpFileBMOImpl")
@Transactional
public class HcFtpFileBMOImpl implements IHcFtpFileBMO {

	@Resource(name = "hcFtpFileDAOImpl")
	private IHcFtpFileDAO hcFtpFileDAOImpl;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return hcFtpFileDAOImpl.saveTaskRunLog(loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		hcFtpFileDAOImpl.updateTaskRunLog(loginfo);
	}
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return hcFtpFileDAOImpl.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		hcFtpFileDAOImpl.saveDbFunction(function);
	}

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		hcFtpFileDAOImpl.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		hcFtpFileDAOImpl.insertFileData2Table(insertSQL);
	}
	
}