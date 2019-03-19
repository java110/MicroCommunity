package com.java110.web.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 导航栏
 * Created by wuxw on 2019/3/19.
 */

@Component("nav")
public class NavComponent {


    public ResponseEntity<String> getNavData(String param){

        String result = "{'welcomeText':'欢迎访问HC小区管理系统，学文！','noticeSize':10," +
                "'moreNoticeUrl':'/moreNotice','notices':[" +
                "{'msg':'新系统开发测试','date':'2019-03-19'}," +
                "{'msg':'权限检查测试','date':'2019-03-21'}," +
                "{'msg':'系统欲开发测试','date':'2019-03-20'}" +
                "]}";

        JSONObject nav = JSONObject.parseObject(result);

        return new ResponseEntity<String>(nav.toJSONString(), HttpStatus.OK);
    }
}
