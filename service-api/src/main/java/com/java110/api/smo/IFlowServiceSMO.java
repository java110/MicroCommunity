package com.java110.api.smo;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;

/**
 * 控制类业务接口
 * Created by wuxw on 2018/4/28.
 */
public interface IFlowServiceSMO {



    /**
     * 用户登录
     * @param pd
     * @return
     * @throws SMOException
     */
    public void login(IPageData pd) throws SMOException;

    /**
     * 是否有商户信息
     * @param pd 前台页面封装对象
     * @return
     * @throws SMOException
     */
    public boolean hasStoreInfos(IPageData pd) throws SMOException;

}
