package com.java110.web.components;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.web.smo.INavServiceSMO;
import com.java110.web.smo.msg.IListMsgSMO;
import com.java110.web.smo.msg.IReadMsgSMO;
import com.java110.web.smo.notice.IListNoticesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 导航栏
 * Created by wuxw on 2019/3/19.
 */

@Component("nav")
public class NavComponent {

    @Autowired
    private INavServiceSMO navServiceSMOImpl;

    @Autowired
    private IListNoticesSMO listNoticesSMOImpl;

    @Autowired
    private IListMsgSMO listMsgSMOImpl;

    @Autowired
    private IReadMsgSMO readMsgSMOImpl;


    /**
     * 查询通知信息
     *
     * @param pd 页面封装数据
     * @return 通知信息
     */
    public ResponseEntity<String> getNavData(IPageData pd) {

        return listMsgSMOImpl.listMsg(pd);
    }

    /**
     * 阅读消息
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> readMsg(IPageData pd) {
        return readMsgSMOImpl.readMsg(pd);
    }


    /**
     * 退出登录
     *
     * @param pd 页面封装对象
     * @return 页面对象ResponseEntity
     */
    public ResponseEntity<String> logout(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = navServiceSMOImpl.doExit(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            return responseEntity;
        }
    }

    /**
     * 获取用户信息
     *
     * @param pd 页面封装对象
     * @return 页面对象ResponseEntity
     */
    public ResponseEntity<String> getUserInfo(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = navServiceSMOImpl.getUserInfo(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            return responseEntity;
        }
    }

    /**
     * 查询已经入住的小区
     *
     * @param pd 页面封装对象
     * @return 小区信息 [{community:"123123",name:"测试1小区"},{community:"223123",name:"测试2小区"}]
     */
    public ResponseEntity<String> getCommunitys(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        responseEntity = navServiceSMOImpl.listMyCommunity(pd);

        return responseEntity;
    }


    public INavServiceSMO getNavServiceSMOImpl() {
        return navServiceSMOImpl;
    }

    public void setNavServiceSMOImpl(INavServiceSMO navServiceSMOImpl) {
        this.navServiceSMOImpl = navServiceSMOImpl;
    }

    public IListNoticesSMO getListNoticesSMOImpl() {
        return listNoticesSMOImpl;
    }

    public void setListNoticesSMOImpl(IListNoticesSMO listNoticesSMOImpl) {
        this.listNoticesSMOImpl = listNoticesSMOImpl;
    }

    public IListMsgSMO getListMsgSMOImpl() {
        return listMsgSMOImpl;
    }

    public void setListMsgSMOImpl(IListMsgSMO listMsgSMOImpl) {
        this.listMsgSMOImpl = listMsgSMOImpl;
    }

    public IReadMsgSMO getReadMsgSMOImpl() {
        return readMsgSMOImpl;
    }

    public void setReadMsgSMOImpl(IReadMsgSMO readMsgSMOImpl) {
        this.readMsgSMOImpl = readMsgSMOImpl;
    }
}
