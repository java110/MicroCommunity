package com.java110.boot.components.initData;


import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("initData")
public class InitDataComponent {


    /**
     * 查询已经入住的小区
     *
     * @param pd 页面封装对象
     * @return 小区信息 [{community:"123123",name:"测试1小区"},{community:"223123",name:"测试2小区"}]
     */
    public ResponseEntity<String> getCommunitys(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        // responseEntity = listMyEnteredCommunitysSMOImpl.myCommunitys(pd);

        return responseEntity;
    }

}
