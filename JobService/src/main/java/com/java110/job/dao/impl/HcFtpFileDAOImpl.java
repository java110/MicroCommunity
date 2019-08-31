package com.java110.job.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.job.dao.IHccFtpFileDAO;
import com.java110.job.model.FtpTaskLog;
import com.java110.job.model.FtpTaskLogDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
@Service("IPrvncFtpFileDAO")
@Transactional
public class HcFtpFileDAOImpl extends BaseServiceDao implements IHccFtpFileDAO {

	public long saveTaskRunLog(FtpTaskLog loginfo){
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("IPrvncFtpFileDAO.saveTaskRunLog",loginfo);
	}

	public void updateTaskRunLog(FtpTaskLog loginfo){
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("IPrvncFtpFileDAO.updateTaskRunLog",loginfo);
	}
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail){
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("IPrvncFtpFileDAO.saveTaskRunDetailLog",logdetail);
	}



	@Override
	public List execConfigSql(String dbsql){
		return  sqlSessionTemplate.selectList("IPrvncFtpFileDAO.execConfigSql", dbsql);
	}
	/**
	 * 查询文件下载在文件系统的配置任务列表
	 * @param info
	 * @return
	 */
	public Map queryFtpItems(Map info){
		Map resultInfo = new HashMap();
		//1.0查询总数
		Integer allCNT = (Integer) sqlSessionTemplate.selectOne("IPrvncFtpFileDAO.queryFtpItemsCount", info);
		resultInfo.put("ITEMSCOUNT", allCNT);
		List datas = sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryFtpItems",info);
		resultInfo.put("DATA", datas);
		return resultInfo;
	}
	/**
	 * 创建taskId
	 * @return
	 */
	public long newCreateTaskId(){
		Long tastId = (Long) sqlSessionTemplate.selectOne("IPrvncFtpFileDAO.newCreateTaskId");
		return tastId;
	}
	/**
	 * 保存文件下载配置
	 * @param info
	 * @return
	 */
	public int addFtpItem(Map info){
//		return  Integer.parseInt(sqlSessionTemplate.insert("IPrvncFtpFileDAO.addFtpItem", info).toString());
		sqlSessionTemplate.insert("IPrvncFtpFileDAO.addFtpItem", info);
		return 1;
	}
	/**
	 * 根据TaskId 查询ftp配置信息
	 * @param info
	 * @return
	 */
	public Map queryFtpItemByTaskId(Map info){
		Object data = sqlSessionTemplate.selectOne("IPrvncFtpFileDAO.queryFtpItemByTaskId", info);
		Map ftpItem = null ;
		if(data != null){
			ftpItem = (Map)data;
			//查ftpitem属性
			List<Map> ftpItemAttrs = sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryFtpItemAttrsByTaskId",info);
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
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.searchFtpItemByTaskName",info);
	}
	/**
	 * 修改ftp配置信息
	 * @param info
	 * @return
	 */
	public int updateFtpItemByTaskId(Map info){
		return sqlSessionTemplate.update("IPrvncFtpFileDAO.updateFtpItemByTaskId", info);
	}
	/**
	 * 删除ftp配置信息
	 * @param info
	 * @return
	 */
	public int deleteFtpItemByTaskId(Map info){
		return sqlSessionTemplate.update("IPrvncFtpFileDAO.deleteFtpItemByTaskId", info);
	}

	/**
	 * 根据taskids 获取将要操作的ftp配置信息
	 * @param info
	 * @return
	 */
	public List<Map> queryFtpItemsByTaskIds(Map info){
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryFtpItemsByTaskIds",info);
	}

	/**
	 * 查询FTPItem的属性信息
	 * @param info
	 * @return
	 */
	public List<Map> queryFtpItemAttrsByTaskId(Map info){
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryFtpItemAttrsByTaskId",info);
	}

	/**
	 * 删除属性
	 * @param info
	 * @return
	 */
	public int deleteFtpItemAttrsbyTaskId(Map info){
		return sqlSessionTemplate.delete("IPrvncFtpFileDAO.deleteFtpItemAttrsbyTaskId", info);
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
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryTableColInfo",para);
	}

	/**
	 * 保存FTPItem的属性信息
	 * @return
	 */
	public int addFtpItemAttrs(List<Map> infos){
		for(Map info :infos){
			sqlSessionTemplate.insert("IPrvncFtpFileDAO.addFtpItemAttrs",info);
		}

		return 1;
	}

	/**
	 * 查询没有下载过的文件名
	 * @param info
	 * @return
	 */
	public List<Map> queryFileNamesWithOutFtpLog(Map info){
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryFileNamesWithOutFtpLog",info);
	}
	/**
	 * 查询ItemSpec
	 * @param info
	 * @return
	 */
	public List<Map> queryItemSpec(Map info){
		return sqlSessionTemplate.selectList("IPrvncFtpFileDAO.queryItemSpec",info);
	}

	/**
	 * 修改ftp配置信息(状态)
	 * @param info
	 * @return
	 */
	public int updateFtpItemRunState(Map info){
		return sqlSessionTemplate.update("IPrvncFtpFileDAO.updateFtpItemRunState", info);
	}

	/**
	 * 保存下载文件名称
	 * @param info
	 * @return
	 */
	public int addDownloadFileName(Map info){
//		return  Integer.parseInt(getSqlMapClientTemplate().insert("IPrvncFtpFileDAO.addDownloadFileName", info).toString());

		return  sqlSessionTemplate.insert("IPrvncFtpFileDAO.addDownloadFileName", info);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	public void saveDbFunction(String function){
		Map para=new HashMap();
		para.put("functionname", function);
		sqlSessionTemplate.selectOne("IPrvncFtpFileDAO.calltaskfunction",para);
	}

	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	public void saveDbFunctionWithParam(Map info){
		sqlSessionTemplate.selectOne("IPrvncFtpFileDAO.calltaskfunctionwithparam",info);
	}

	public void insertFileData2Table(String insertSQL){
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("IPrvncFtpFileDAO.insertFileData2Table",insertSQL);
	}
}