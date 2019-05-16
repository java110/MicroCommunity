package com.java110.core.proxy;

/**
 * @ClassName ITestService
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/15 15:53
 * @Version 1.0
 * add by wuxw 2019/5/15
 **/
public interface ITestService {

    /**
     * 获取
     *
     * @param param 参数
     * @return 返回
     */
    String get(String param);

    void set(String param);
}
