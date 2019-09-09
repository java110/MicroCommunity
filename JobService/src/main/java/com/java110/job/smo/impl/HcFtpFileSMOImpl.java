package com.java110.job.smo.impl;

import com.java110.job.model.*;
import com.java110.job.smo.IHcFtpFileBMO;
import com.java110.job.smo.IHcFtpFileSMO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.Map;
@Service("hcFtpFileSMOImpl")
@Transactional
public class HcFtpFileSMOImpl implements IHcFtpFileSMO {
    /** logger */  
    protected final Logger log= Logger.getLogger(getClass());

	@Resource(name = "hcFtpFileBMOImpl")
	private IHcFtpFileBMO hcFtpFileBMOImpl;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return hcFtpFileBMOImpl.saveTaskRunLog(loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		hcFtpFileBMOImpl.updateTaskRunLog(loginfo);
	}
	@Override
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return hcFtpFileBMOImpl.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		hcFtpFileBMOImpl.saveDbFunction(function);
	}
	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		hcFtpFileBMOImpl.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		hcFtpFileBMOImpl.insertFileData2Table(insertSQL);
	}
}