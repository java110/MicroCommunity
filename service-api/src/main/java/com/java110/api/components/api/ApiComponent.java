package com.java110.api.components.api;


import com.java110.core.context.IPageData;
import com.java110.api.smo.common.ICommonGetSMO;
import com.java110.api.smo.common.ICommonPostSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 活动组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("api")
public class ApiComponent {

    @Autowired
    private ICommonGetSMO commonGetSMOImpl;

    @Autowired
    private ICommonPostSMO commonPostSMOImpl;

    /**
     * 查询活动列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> callApi(IPageData pd) {

        // get方式调用
        if(HttpMethod.GET == pd.getMethod()){
            return commonGetSMOImpl.doService(pd);
        }
        //post 方式调用
        return commonPostSMOImpl.doService(pd);
    }


}
