package com.java110.rule.smo;

/**
 * 规则服务接口类
 * Created by wuxw on 2017/7/23.
 */
public interface IRuleServiceSMO {

    /**
     * 校验方法
     * @param validateInfo
     * @return
     * @throws Exception
     */
    public String validate(String validateInfo) throws Exception;
}
