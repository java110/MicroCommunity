package com.java110.api.components.community;

import com.java110.core.context.IPageData;
import com.java110.api.smo.community.IEditCommunitySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editCommunity")
public class EditCommunityComponent {

    @Autowired
    private IEditCommunitySMO editCommunitySMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editCommunitySMOImpl.updateCommunity(pd);
    }

    public IEditCommunitySMO getEditCommunitySMOImpl() {
        return editCommunitySMOImpl;
    }

    public void setEditCommunitySMOImpl(IEditCommunitySMO editCommunitySMOImpl) {
        this.editCommunitySMOImpl = editCommunitySMOImpl;
    }
}
