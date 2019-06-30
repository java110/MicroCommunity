package com.java110.web.components.community;


import com.java110.core.context.IPageData;
import com.java110.web.smo.community.ICommunityManageSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 小区组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("communityManage")
public class CommunityManageComponent {

    @Autowired
    private ICommunityManageSMO communityManageSMOImpl;

    /**
     * 查询小区列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return communityManageSMOImpl.listCommunitys(pd);
    }

    public ICommunityManageSMO getCommunityManageSMOImpl() {
        return communityManageSMOImpl;
    }

    public void setCommunityManageSMOImpl(ICommunityManageSMO communityManageSMOImpl) {
        this.communityManageSMOImpl = communityManageSMOImpl;
    }
}
