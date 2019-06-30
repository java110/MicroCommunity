package com.java110.web.components.community;

import com.java110.core.context.IPageData;
import com.java110.web.smo.community.IAddCommunitySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加小区组件
 */
@Component("addCommunity")
public class AddCommunityComponent {

    @Autowired
    private IAddCommunitySMO addCommunitySMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addCommunitySMOImpl.saveCommunity(pd);
    }

    public IAddCommunitySMO getAddCommunitySMOImpl() {
        return addCommunitySMOImpl;
    }

    public void setAddCommunitySMOImpl(IAddCommunitySMO addCommunitySMOImpl) {
        this.addCommunitySMOImpl = addCommunitySMOImpl;
    }
}
