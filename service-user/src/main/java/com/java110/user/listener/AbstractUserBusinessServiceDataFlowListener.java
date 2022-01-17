package com.java110.user.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.user.dao.IUserServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wux
 * @create 2018-12-08 下午3:15
 * @desc 用户服务抽象类
 **/
public abstract class AbstractUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractUserBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IUserServiceDao getUserServiceDaoImpl();
    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessUser 商户信息
     */
    protected void autoSaveDelBusinessUser(Business business, JSONObject businessUser){
        //自动插入DEL
        Map info = new HashMap();
        info.put("userId",businessUser.getString("userId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map currentUserInfo = getUserServiceDaoImpl().queryUserInfo(info);
        if(currentUserInfo == null || currentUserInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        currentUserInfo.put("bId",business.getbId());
        currentUserInfo.put("userId",currentUserInfo.get("user_id"));
        currentUserInfo.put("name",currentUserInfo.get("name"));
        currentUserInfo.put("email",currentUserInfo.get("email"));
        currentUserInfo.put("address",currentUserInfo.get("address"));
        currentUserInfo.put("password",currentUserInfo.get("password"));
        currentUserInfo.put("locationCd",currentUserInfo.get("location_cd"));
        currentUserInfo.put("age",currentUserInfo.get("age"));
        currentUserInfo.put("sex",currentUserInfo.get("sex"));
        currentUserInfo.put("tel",currentUserInfo.get("tel"));
        currentUserInfo.put("levelCd",currentUserInfo.get("level_cd"));
        currentUserInfo.put("operate",StatusConstant.OPERATE_DEL);
        getUserServiceDaoImpl().saveBusinessUserInfo(currentUserInfo);
        for (Object key : currentUserInfo.keySet()) {
            if (businessUser.get(key) == null) {
                businessUser.put(key.toString(), currentUserInfo.get(key));
            }
        }
    }


    /**
     * 刷新 businessUserInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessUserInfo
     */
    protected void flushBusinessUserInfo(Map businessUserInfo,String statusCd){

        businessUserInfo.put("newBId",businessUserInfo.get("b_id"));
        businessUserInfo.put("userId",businessUserInfo.get("user_id"));
        businessUserInfo.put("name",businessUserInfo.get("name"));
        businessUserInfo.put("email",businessUserInfo.get("email"));
        businessUserInfo.put("address",businessUserInfo.get("address"));
        businessUserInfo.put("password",businessUserInfo.get("password"));
        businessUserInfo.put("locationCd",businessUserInfo.get("location_cd"));
        businessUserInfo.put("age",businessUserInfo.get("age"));
        businessUserInfo.put("sex",businessUserInfo.get("sex"));
        businessUserInfo.put("tel",businessUserInfo.get("tel"));
        businessUserInfo.put("levelCd",businessUserInfo.get("level_cd"));
        businessUserInfo.put("statusCd", statusCd);

    }



    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessUser 商户信息
     */
    protected void autoSaveAddBusinessUser(Business business, JSONObject businessUser){
        //自动插入DEL
        Map info = new HashMap();
        info.put("userId",businessUser.getString("userId"));
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        Map currentUserInfo = getUserServiceDaoImpl().queryUserInfo(info);
        if(currentUserInfo == null || currentUserInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        currentUserInfo.put("bId",business.getbId());
        currentUserInfo.put("userId",currentUserInfo.get("user_id"));
        currentUserInfo.put("name",currentUserInfo.get("name"));
        currentUserInfo.put("email",currentUserInfo.get("email"));
        currentUserInfo.put("address",currentUserInfo.get("address"));
        currentUserInfo.put("password",currentUserInfo.get("password"));
        currentUserInfo.put("locationCd",currentUserInfo.get("location_cd"));
        currentUserInfo.put("age",currentUserInfo.get("age"));
        currentUserInfo.put("sex",currentUserInfo.get("sex"));
        currentUserInfo.put("tel",currentUserInfo.get("tel"));
        currentUserInfo.put("levelCd",currentUserInfo.get("level_cd"));
        currentUserInfo.put("operate",StatusConstant.OPERATE_ADD);
        getUserServiceDaoImpl().saveBusinessUserInfo(currentUserInfo);
    }

}
