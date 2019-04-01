package com.java110.web.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.web.smo.INavServiceSMO;
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

    public ResponseEntity<String> getNavData(IPageData pd){

        String result = "{'noticeSize':10," +
                "'moreNoticeUrl':'/moreNotice','notices':[" +
                "{'msg':'新系统开发测试','date':'2019-03-19'}," +
                "{'msg':'权限检查测试','date':'2019-03-21'}," +
                "{'msg':'系统欲开发测试','date':'2019-03-20'}" +
                "]}";

        JSONObject nav = JSONObject.parseObject(result);

        return new ResponseEntity<String>(nav.toJSONString(), HttpStatus.OK);
    }


    /**
     * 退出登录
     * @param pd
     * @return
     */
    public ResponseEntity<String> logout(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity =  navServiceSMOImpl.doExit(pd);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }

    /**
     * 获取用户信息
     * @param pd
     * @return
     */
    public ResponseEntity<String> getUserInfo(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity =  navServiceSMOImpl.getUserInfo(pd);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }


    public INavServiceSMO getNavServiceSMOImpl() {
        return navServiceSMOImpl;
    }

    public void setNavServiceSMOImpl(INavServiceSMO navServiceSMOImpl) {
        this.navServiceSMOImpl = navServiceSMOImpl;
    }
}
