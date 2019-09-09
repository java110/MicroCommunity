package com.java110.job.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.job.dao.IHcFtpFileDAO;
import com.java110.job.model.FtpTaskLog;
import com.java110.job.model.FtpTaskLogDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
@Service("hcFtpFileDAOImpl")
@Transactional
public class HcFtpFileDAOImpl extends BaseServiceDao implements IHcFtpFileDAO {

	public long saveTaskRunLog(FtpTaskLog loginfo){
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("IHcFtpFileDAO.saveTaskRunLog",loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo){
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("IHcFtpFileDAO.updateTaskRunLog",loginfo);
	}
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail){
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("IHcFtpFileDAO.saveTaskRunDetailLog",logdetail);
	}



	@Override
	public List execConfigSql(String dbsql){
		return  sqlSessionTemplate.selectList("IHcFtpFileDAO.execConfigSql", dbsql);
	}
	/**
	 * 查询文件下载在文件系统的配置任务列表
	 * @param info
	 * @return
	 */
	public Map queryFtpItems(Map info){
		Map resultInfo = new HashMap();
		//1.0查询总数
		Integer allCNT = (Integer) sqlSessionTemplate.selectOne("IHcFtpFileDAO.queryFtpItemsCount", info);
		resultInfo.put("ITEMSCOUNT", allCNT);
		List datas = sqlSessionTemplate.selectList("IHcFtpFileDAO.queryFtpItems",info);
		resultInfo.put("DATA", datas);
		return resultInfo;
	}
	/**
	 * 创建taskId
	 * @return
	 */
	public long newCreateTaskId(){
		Long tastId = (Long) sqlSessionTemplate.selectOne("IHcFtpFileDAO.newCreateTaskId");
		return tastId;
	}
	/**
	 * 保存文件下载配置
	 * @param info
	 * @return
	 */
	public int addFtpItem(Map info){
//		return  Integer.parseInt(sqlSessionTemplate.insert("IHcFtpFileDAO.addFtpItem", info).toString());
		sqlSessionTemplate.insert("IHcFtpFileDAO.addFtpItem", info);
		return 1;
	}
	/**
	 * 根据TaskId 查询ftp配置信息
	 * @param info
	 * @return
	 */
	public Map queryFtpItemByTaskId(Map info){
		Object data = sqlSessionTemplate.selectOne("IHcFtpFileDAO.queryFtpItemByTaskId", info);
		Map ftpItem = null ;
		if(data != null){
			ftpItem = (Map)data;
			//查ftpitem属性
			List<Map> ftpItemAttrs = sqlSessionTemplate.selectList("IHcFtpFileDAO.queryFtpItemAttrsByTaskId",info);
			ftpItem.put("FTP_ITEM_ATTRS", ftpItemAttrs);
		}

		return ftpItem;
	}

	/**
	 * 根据任务名称搜素
	 * @param info
	 * @return
	 */
	public List<Map> searchFtpItemByTaskName(Map info){
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.searchFtpItemByTaskName",info);
	}
	/**
	 * 修改ftp配置信息
	 * @param info
	 * @return
	 */
	public int updateFtpItemByTaskId(Map info){
		return sqlSessionTemplate.update("IHcFtpFileDAO.updateFtpItemByTaskId", info);
	}
	/**
	 * 删除ftp配置信息
	 * @param info
	 * @return
	 */
	public int deleteFtpItemByTaskId(Map info){
		return sqlSessionTemplate.update("IHcFtpFileDAO.deleteFtpItemByTaskId", info);
	}

	/**
	 * 根据taskids 获取将要操作的ftp配置信息
	 * @param info
	 * @return
	 */
	public List<Map> queryFtpItemsByTaskIds(Map info){
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.queryFtpItemsByTaskIds",info);
	}

	/**
	 * 查询FTPItem的属性信息
	 * @param info
	 * @return
	 */
	public List<Map> queryFtpItemAttrsByTaskId(Map info){
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.queryFtpItemAttrsByTaskId",info);
	}

	/**
	 * 删除属性
	 * @param info
	 * @return
	 */
	public int deleteFtpItemAttrsbyTaskId(Map info){
		return sqlSessionTemplate.delete("IHcFtpFileDAO.deleteFtpItemAttrsbyTaskId", info);
	}

	@Override
	public List<Map> queryTableColInfo(String tablename, String colnames) {
		// TODO Auto-generated method stub
		Map para=new HashMap();
		if(tablename.indexOf(".")>0){
			String username=tablename.substring(0,tablename.indexOf("."));
			tablename=tablename.substring(tablename.indexOf(".")+1);
			para.put("username", username.toUpperCase());
		}
		para.put("tablename", tablename.toUpperCase());
		para.put("colnames", "'"+colnames.toUpperCase().replaceAll(",", "','")+"'");
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.queryTableColInfo",para);
	}

	/**
	 * 保存FTPItem的属性信息
	 * @return
	 */
	public int addFtpItemAttrs(List<Map> infos){
		for(Map info :infos){
			sqlSessionTemplate.insert("IHcFtpFileDAO.addFtpItemAttrs",info);
		}

		return 1;
	}

	/**
	 * 查询没有下载过的文件名
	 * @param info
	 * @return
	 */
	public List<Map> queryFileNamesWithOutFtpLog(Map info){
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.queryFileNamesWithOutFtpLog",info);
	}
	/**
	 * 查询ItemSpec
	 * @param info
	 * @return
	 */
	public List<Map> queryItemSpec(Map info){
		return sqlSessionTemplate.selectList("IHcFtpFileDAO.queryItemSpec",info);
	}

	/**
	 * 修改ftp配置信息(状态)
	 * @param info
	 * @return
	 */
	public int updateFtpItemRunState(Map info){
		return sqlSessionTemplate.update("IHcFtpFileDAO.updateFtpItemRunState", info);
	}

	/**
	 * 保存下载文件名称
	 * @param info
	 * @return
	 */
	public int addDownloadFileName(Map info){
//		return  Integer.parseInt(getSqlMapClientTemplate().insert("IHcFtpFileDAO.addDownloadFileName", info).toString());

		return  sqlSessionTemplate.insert("IHcFtpFileDAO.addDownloadFileName", info);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		Map para=new HashMap();
		para.put("functionname", function);
		sqlSessionTemplate.selectOne("IHcFtpFileDAO.calltaskfunction",para);
	}

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		sqlSessionTemplate.selectOne("IHcFtpFileDAO.calltaskfunctionwithparam",info);
	}

	public void insertFileData2Table(String insertSQL){
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("IHcFtpFileDAO.insertFileData2Table",insertSQL);
	}
}