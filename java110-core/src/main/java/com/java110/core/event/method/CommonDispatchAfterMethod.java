package com.java110.core.event.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.AppContext;
import org.springframework.stereotype.Component;

/**
 * 公共调度 服务完成后，需要调用方法 处理
 *
 * Created by wuxw on 2017/9/18.
 */
@Component
public class CommonDispatchAfterMethod {


    /**
     * 反射调用
     * @param context
     * @param datas
     * @param returnData
     */
    public void test(AppContext context, JSONArray datas, JSONObject returnData){


    }

}
