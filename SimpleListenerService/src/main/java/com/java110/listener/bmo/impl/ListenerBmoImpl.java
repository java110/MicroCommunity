package com.java110.listener.bmo.impl;

import com.java110.listener.bmo.IListenerBmo;
import com.java110.listener.dao.IListenerServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/24.
 */
@Service("listenerBmoImpl")
public class ListenerBmoImpl implements IListenerBmo {

    @Autowired
    IListenerServiceDao listenerServiceDaoImpl;


    @Override
    public Map queryFtpItems(Map info) throws Exception {
        return listenerServiceDaoImpl.queryFtpItems(info);
    }

    @Override
    public int addFtpItem(Map info) throws Exception {
        return listenerServiceDaoImpl.addFtpItem(info);
    }

    @Override
    public Map queryFtpItemByTaskId(Map info) throws Exception {
        return listenerServiceDaoImpl.queryFtpItemByTaskId(info);
    }

    @Override
    public List<Map> searchFtpItemByTaskName(Map info) throws Exception {
        return listenerServiceDaoImpl.searchFtpItemByTaskName(info);
    }

    @Override
    public int updateFtpItemByTaskId(Map info) throws Exception {
        return listenerServiceDaoImpl.updateFtpItemByTaskId(info);
    }

    @Override
    public int deleteFtpItemByTaskId(Map info) throws Exception {
        return listenerServiceDaoImpl.updateFtpItemByTaskId(info);
    }

    @Override
    public List<Map> queryFtpItemsByTaskIds(Map info) throws Exception {
        return listenerServiceDaoImpl.queryFtpItemsByTaskIds(info);
    }

    @Override
    public List<Map> queryFtpItemAttrsByTaskId(Map info) throws Exception {
        return listenerServiceDaoImpl.queryFtpItemAttrsByTaskId(info);
    }

    @Override
    public long newCreateTaskId() {
        return listenerServiceDaoImpl.newCreateTaskId();
    }

    @Override
    public int addFtpItemAttrs(List<Map> infos) throws Exception {
        return listenerServiceDaoImpl.addFtpItemAttrs(infos);
    }

    @Override
    public List<Map> queryFileNamesWithOutFtpLog(Map info) throws Exception {
        return listenerServiceDaoImpl.queryFileNamesWithOutFtpLog(info);
    }

    @Override
    public int addDownloadFileName(Map info) throws Exception {
        return listenerServiceDaoImpl.addDownloadFileName(info);
    }

    @Override
    public int updateFtpItemRunState(Map info) throws Exception {
        return listenerServiceDaoImpl.updateFtpItemRunState(info);
    }

    @Override
    public List<Map> queryItemSpec(Map info) throws Exception {
        return listenerServiceDaoImpl.queryItemSpec(info);
    }

    @Override
    public int deleteFtpItemAttrsbyTaskId(Map info) throws Exception {
        return listenerServiceDaoImpl.deleteFtpItemAttrsbyTaskId(info);
    }


    public IListenerServiceDao getListenerServiceDaoImpl() {
        return listenerServiceDaoImpl;
    }

    public void setListenerServiceDaoImpl(IListenerServiceDao listenerServiceDaoImpl) {
        this.listenerServiceDaoImpl = listenerServiceDaoImpl;
    }
}
