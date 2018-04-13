package com.java110.listener.dao.impl;

import com.java110.common.constant.RuleDomain;
import com.java110.common.util.SerializeUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.listener.FtpTaskLog;
import com.java110.entity.listener.FtpTaskLogDetail;
import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleCondCfg;
import com.java110.entity.rule.RuleEntrance;
import com.java110.listener.dao.IListenerServiceDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by wuxw on 2017/7/23.
 */

@Service("listenerServiceDaoImpl")
@Transactional
public class ListenerServiceDaoImpl extends BaseServiceDao implements IListenerServiceDao {

    /**
     * 查询文件下载在文件系统的配置任务列表
     * @param info
     * @return
     */
    public Map queryFtpItems(Map info){
        Map resultInfo = new HashMap();
        //1.0查询总数
        Integer allCNT = (Integer) sqlSessionTemplate.selectOne("ListenerServiceDaoImpl.queryFtpItemsCount", info);
        resultInfo.put("ITEMSCOUNT", allCNT);
        List datas = sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryFtpItems",info);
        resultInfo.put("DATA", datas);
        return resultInfo;
    }
    /**
     * 创建taskId
     * @return
     */
    public long newCreateTaskId(){
        long tastId = (Long) sqlSessionTemplate.selectOne("ListenerServiceDaoImpl.newCreateTaskId");
        return tastId;
    }
    /**
     * 保存文件下载配置
     * @param info
     * @return
     */
    public int addFtpItem(Map info){
//		return  Integer.parseInt(sqlSessionTemplate.insert("ListenerServiceDaoImpl.addFtpItem", info).toString());
        sqlSessionTemplate.insert("ListenerServiceDaoImpl.addFtpItem", info);
        return 1;
    }
    /**
     * 根据TaskId 查询ftp配置信息
     * @param info
     * @return
     */
    public Map queryFtpItemByTaskId(Map info){
        Object data = sqlSessionTemplate.selectOne("ListenerServiceDaoImpl.queryFtpItemByTaskId", info);
        Map ftpItem = null ;
        if(data != null){
            ftpItem = (Map)data;
            //查ftpitem属性
            List<Map> ftpItemAttrs = sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryFtpItemAttrsByTaskId",info);
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
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.searchFtpItemByTaskName",info);
    }
    /**
     * 修改ftp配置信息
     * @param info
     * @return
     */
    public int updateFtpItemByTaskId(Map info){
        return sqlSessionTemplate.update("ListenerServiceDaoImpl.updateFtpItemByTaskId", info);
    }

    /**
     * 修改ftp配置信息(状态)
     * @param info
     * @return
     */
    public int updateFtpItemRunState(Map info){
        return sqlSessionTemplate.update("ListenerServiceDaoImpl.updateFtpItemRunState", info);
    }

    /**
     * 删除ftp配置信息
     * @param info
     * @return
     */
    public int deleteFtpItemByTaskId(Map info){
        return sqlSessionTemplate.update("ListenerServiceDaoImpl.deleteFtpItemByTaskId", info);
    }

    /**
     * 根据taskids 获取将要操作的ftp配置信息 
     * @param info
     * @return
     */
    public List<Map> queryFtpItemsByTaskIds(Map info){
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryFtpItemsByTaskIds",info);
    }

    /**
     * 查询FTPItem的属性信息
     * @param info
     * @return
     */
    public List<Map> queryFtpItemAttrsByTaskId(Map info){
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryFtpItemAttrsByTaskId",info);
    }

    /**
     * 删除属性
     * @param info
     * @return
     */
    public int deleteFtpItemAttrsbyTaskId(Map info){
        return sqlSessionTemplate.delete("ListenerServiceDaoImpl.deleteFtpItemAttrsbyTaskId", info);
    }

    /**
     * 执行存过，处理任务执行前后的事情
     */
    public void saveDbFunction(String function){
        Map para=new HashMap();
        para.put("functionname", function);
        sqlSessionTemplate.selectOne("ListenerServiceDaoImpl.calltaskfunction",para);
    }

    /**
     * 执行存过(带参数)，处理任务执行前后的事情
     */
    public void saveDbFunctionWithParam(Map info){
        sqlSessionTemplate.selectOne("ListenerServiceDaoImpl.calltaskfunctionwithparam",info);
    }

    /**
     * 保存FTPItem的属性信息
     * @return
     */
    public int addFtpItemAttrs(List<Map> infos){
        for(Map info :infos){
            sqlSessionTemplate.insert("ListenerServiceDaoImpl.addFtpItemAttrs",info);
        }

        return 1;
    }

    /**
     * 查询没有下载过的文件名
     * @param info
     * @return
     */
    public List<Map> queryFileNamesWithOutFtpLog(Map info){
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryFileNamesWithOutFtpLog",info);
    }

    /**
     * 保存下载文件名称
     * @param info
     * @return
     */
    public int addDownloadFileName(Map info){
//		return  Integer.parseInt(sqlSessionTemplate.insert("ListenerServiceDaoImpl.addDownloadFileName", info).toString());
        sqlSessionTemplate.insert("ListenerServiceDaoImpl.addDownloadFileName", info);
        return  1;
    }
    /**
     * 查询ItemSpec
     * @param info
     * @return
     */
    public List<Map> queryItemSpec(Map info){
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryItemSpec",info);
    }


    public long saveTaskRunLog(FtpTaskLog loginfo){
        // TODO Auto-generated method stub
        return sqlSessionTemplate.insert("ListenerServiceDaoImpl.saveTaskRunLog",loginfo);
    }

    public void updateTaskRunLog(FtpTaskLog loginfo){
        // TODO Auto-generated method stub
        sqlSessionTemplate.update("ListenerServiceDaoImpl.updateTaskRunLog",loginfo);
    }
    public long saveTaskRunDetailLog(FtpTaskLogDetail logdetail){
        // TODO Auto-generated method stub
        return sqlSessionTemplate.insert("ListenerServiceDaoImpl.saveTaskRunDetailLog",logdetail);
    }

    public void updateTaskRunDetailLog(FtpTaskLogDetail logdetail){
        // TODO Auto-generated method stub
        sqlSessionTemplate.update("ListenerServiceDaoImpl.updateTaskRunDetailLog",logdetail);
    }
    public void insertFileData2Table(String insertSQL){
        // TODO Auto-generated method stub
        sqlSessionTemplate.update("ListenerServiceDaoImpl.insertFileData2Table",insertSQL);
    }

    @Override
    public List execConfigSql(String dbsql){
        return  sqlSessionTemplate.selectList("ListenerServiceDaoImpl.execConfigSql", dbsql);
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
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryTableColInfo",para);
    }
    public List<Map> queryTableColInfo(String username,String tablename,String colnames) {
        // TODO Auto-generated method stub
        Map para=new HashMap();
        para.put("tablename", tablename);
        para.put("username", username);
        para.put("colnames", "'"+colnames.toUpperCase().replaceAll("\n", "").replaceAll(" ", "").replaceAll(" ", "").replaceAll(",", "','")+"'");
        return sqlSessionTemplate.selectList("ListenerServiceDaoImpl.queryTableColInfo",para);
    }

}
