package com.java110.console.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.SMOException;
import com.java110.entity.service.PageData;

import java.util.List;
import java.util.Map;

/**
 * 控制类业务接口
 * Created by wuxw on 2018/4/28.
 */
public interface IConsoleServiceSMO {

    /**
     * 根据 管理员ID 查询菜单
     * @param manageId
     * @return
     */
    public List<Map> getMenuItemsByManageId(String manageId) throws SMOException,IllegalArgumentException;

    /**
     * 用户登录
     * @param pd
     * @return
     * @throws SMOException
     */
    public void login(PageData pd) throws SMOException;


    /**
     * 查询模板信息
     * @param pd
     * @throws SMOException
     */
    public void getTemplateCol(PageData pd) throws SMOException;
}
