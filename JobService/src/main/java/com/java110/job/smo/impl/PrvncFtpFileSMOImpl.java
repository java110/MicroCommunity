package com.java110.job.smo.impl;

import com.java110.job.model.*;
import com.java110.job.smo.IHcFtpFileBMO;
import com.java110.job.smo.IHcFtpFileSMO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;
@Service("prvncFtpFileSMOImpl")
@Transactional
public class PrvncFtpFileSMOImpl implements IHcFtpFileSMO {
    /** logger */  
    protected final Logger log= Logger.getLogger(getClass());
	@Autowired
	private IHcFtpFileBMO iPrvncFtpFileBMO;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return iPrvncFtpFileBMO.saveTaskRunLog(loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		iPrvncFtpFileBMO.updateTaskRunLog(loginfo);
	}
	@Override
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return iPrvncFtpFileBMO.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		iPrvncFtpFileBMO.saveDbFunction(function);
	}
	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		iPrvncFtpFileBMO.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		iPrvncFtpFileBMO.insertFileData2Table(insertSQL);
	}
}