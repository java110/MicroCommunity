package com.java110.api.components.org;

import com.java110.core.context.IPageData;
import com.java110.api.smo.org.IDeleteOrgCommunitySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加组织管理组件
 */
@Component("deleteOrgCommunity")
public class DeleteOrgCommunityComponent {


    @Autowired
    private IDeleteOrgCommunitySMO deleteOrgCommunitySMOImpl;

    /**
     * 添加组织管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteOrgCommunitySMOImpl.deleteOrgCommunity(pd);
    }

}
