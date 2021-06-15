package com.java110.service.develop;

import com.alibaba.fastjson.JSONObject;
import com.java110.service.context.DataQuery;

/**
 * 开发接口类
 * @ClassName IDevelop
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/8 14:22
 * @Version 1.0
 * add by wuxw 2019/9/8
 **/
public interface IDevelop {

    /**
     * 执行脚本方法
     * @param dataQuery 查询上下文对象
     * @return json
     */
    public JSONObject execute(DataQuery dataQuery);

}
