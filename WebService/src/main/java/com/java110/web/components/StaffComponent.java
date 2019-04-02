package com.java110.web.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 员工展示组件
 * Created by Administrator on 2019/4/2.
 */
@Component("staff")
public class StaffComponent {

    public ResponseEntity<String> loadData(IPageData pd){

        String result = "{'total':1,'page':1,'row':10,'data':[" +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安1','sex':'男','tel':'17797173944','statusCd':'0','createTime':'2019-03-19'}," +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安2','sex':'男','tel':'17797173945','statusCd':'0','createTime':'2019-03-19'}," +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安3','sex':'男','tel':'17797173946','statusCd':'0','createTime':'2019-03-19'}" +
                "]}";

        JSONObject resultObjs = JSONObject.parseObject(result);

        return new ResponseEntity<String>(resultObjs.toJSONString(), HttpStatus.OK);
    }
}
