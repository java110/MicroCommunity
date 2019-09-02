package com.java110.job.smo.impl;

import com.java110.job.dao.IHcFtpFileDAO;
import com.java110.job.model.*;
import com.java110.job.smo.IHcFtpFileBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service("prvncFtpFileBMOImpl")
@Transactional
public class HcPrvncFtpFileBMOImpl implements IHcFtpFileBMO {
	@Autowired
	private IHcFtpFileDAO iHcFtpFileDAO;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return iHcFtpFileDAO.saveTaskRunLog(loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		iHcFtpFileDAO.updateTaskRunLog(loginfo);
	}
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return iHcFtpFileDAO.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		iHcFtpFileDAO.saveDbFunction(function);
	}

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		iHcFtpFileDAO.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		iHcFtpFileDAO.insertFileData2Table(insertSQL);
	}
	
}