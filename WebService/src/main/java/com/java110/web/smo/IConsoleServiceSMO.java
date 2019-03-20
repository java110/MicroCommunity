package com.java110.web.smo;

import com.java110.common.exception.SMOException;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;

import java.util.List;
import java.util.Map;

/**
 * 控制类业务接口
 * Created by wuxw on 2018/4/28.
 */
public interface IConsoleServiceSMO {



    /**
     * 用户登录
     * @param pd
     * @return
     * @throws SMOException
     */
    public void login(IPageData pd) throws SMOException;

}
