package com.java110.api.components.company;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IFlowServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 小区组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("hasCompany")
public class HasCompanyComponent {

    @Autowired
    private IFlowServiceSMO flowServiceSMOImpl;

    /**
     * 查询小区列表
     * @param pd 页面数据封装ApiServiceSMOImpl
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> check(IPageData pd){
        if(flowServiceSMOImpl.hasStoreInfos(pd)){
            return new ResponseEntity<>("有商户信息", HttpStatus.OK);
        }
         return new ResponseEntity<>("初始化商户",HttpStatus.FORBIDDEN);
    }


}
