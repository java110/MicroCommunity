package com.java110.api.components.initData;


import com.java110.core.context.IPageData;
import com.java110.api.smo.community.IListMyEnteredCommunitysSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("initData")
public class InitDataComponent {

    @Autowired
    private IListMyEnteredCommunitysSMO listMyEnteredCommunitysSMOImpl;

    /**
     * 查询已经入住的小区
     *
     * @param pd 页面封装对象
     * @return 小区信息 [{community:"123123",name:"测试1小区"},{community:"223123",name:"测试2小区"}]
     */
    public ResponseEntity<String> getCommunitys(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        responseEntity = listMyEnteredCommunitysSMOImpl.myCommunitys(pd);

        return responseEntity;
    }

    public IListMyEnteredCommunitysSMO getListMyEnteredCommunitysSMOImpl() {
        return listMyEnteredCommunitysSMOImpl;
    }

    public void setListMyEnteredCommunitysSMOImpl(IListMyEnteredCommunitysSMO listMyEnteredCommunitysSMOImpl) {
        this.listMyEnteredCommunitysSMOImpl = listMyEnteredCommunitysSMOImpl;
    }

}
