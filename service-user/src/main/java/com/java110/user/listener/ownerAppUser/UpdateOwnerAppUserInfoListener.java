package com.java110.user.listener.ownerAppUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.user.dao.IOwnerAppUserServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改绑定业主信息 侦听
 * <p>
 * 处理节点
 * 1、businessOwnerAppUser:{} 绑定业主基本信息节点
 * 2、businessOwnerAppUserAttr:[{}] 绑定业主属性信息节点
 * 3、businessOwnerAppUserPhoto:[{}] 绑定业主照片信息节点
 * 4、businessOwnerAppUserCerdentials:[{}] 绑定业主证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOwnerAppUserInfoListener")
@Transactional
public class UpdateOwnerAppUserInfoListener extends AbstractOwnerAppUserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOwnerAppUserInfoListener.class);
    @Autowired
    private IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_APP_USER;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessOwnerAppUser 节点
        if (data.containsKey(OwnerAppUserPo.class.getSimpleName())) {
            Object _obj = data.get(OwnerAppUserPo.class.getSimpleName());
            JSONArray businessOwnerAppUsers = null;
            if (_obj instanceof JSONObject) {
                businessOwnerAppUsers = new JSONArray();
                businessOwnerAppUsers.add(_obj);
            } else {
                businessOwnerAppUsers = (JSONArray) _obj;
            }
            //JSONObject businessOwnerAppUser = data.getJSONObject("businessOwnerAppUser");
            for (int _ownerAppUserIndex = 0; _ownerAppUserIndex < businessOwnerAppUsers.size(); _ownerAppUserIndex++) {
                JSONObject businessOwnerAppUser = businessOwnerAppUsers.getJSONObject(_ownerAppUserIndex);
                doBusinessOwnerAppUser(business, businessOwnerAppUser);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("appUserId", businessOwnerAppUser.getString("appUserId"));
                }
            }

        }
    }


    /**
     * business to instance 过程
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //绑定业主信息
        List<Map> businessOwnerAppUserInfos = ownerAppUserServiceDaoImpl.getBusinessOwnerAppUserInfo(info);
        if (businessOwnerAppUserInfos != null && businessOwnerAppUserInfos.size() > 0) {
            for (int _ownerAppUserIndex = 0; _ownerAppUserIndex < businessOwnerAppUserInfos.size(); _ownerAppUserIndex++) {
                Map businessOwnerAppUserInfo = businessOwnerAppUserInfos.get(_ownerAppUserIndex);
                flushBusinessOwnerAppUserInfo(businessOwnerAppUserInfo, StatusConstant.STATUS_CD_VALID);
                ownerAppUserServiceDaoImpl.updateOwnerAppUserInfoInstance(businessOwnerAppUserInfo);
                if (businessOwnerAppUserInfo.size() == 1) {
                    dataFlowContext.addParamOut("appUserId", businessOwnerAppUserInfo.get("app_user_id"));
                }
            }
        }

    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //绑定业主信息
        List<Map> ownerAppUserInfo = ownerAppUserServiceDaoImpl.getOwnerAppUserInfo(info);
        if (ownerAppUserInfo != null && ownerAppUserInfo.size() > 0) {

            //绑定业主信息
            List<Map> businessOwnerAppUserInfos = ownerAppUserServiceDaoImpl.getBusinessOwnerAppUserInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOwnerAppUserInfos == null || businessOwnerAppUserInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（ownerAppUser），程序内部异常,请检查！ " + delInfo);
            }
            for (int _ownerAppUserIndex = 0; _ownerAppUserIndex < businessOwnerAppUserInfos.size(); _ownerAppUserIndex++) {
                Map businessOwnerAppUserInfo = businessOwnerAppUserInfos.get(_ownerAppUserIndex);
                flushBusinessOwnerAppUserInfo(businessOwnerAppUserInfo, StatusConstant.STATUS_CD_VALID);
                ownerAppUserServiceDaoImpl.updateOwnerAppUserInfoInstance(businessOwnerAppUserInfo);
            }
        }

    }


    /**
     * 处理 businessOwnerAppUser 节点
     *
     * @param business             总的数据节点
     * @param businessOwnerAppUser 绑定业主节点
     */
    private void doBusinessOwnerAppUser(Business business, JSONObject businessOwnerAppUser) {

        Assert.jsonObjectHaveKey(businessOwnerAppUser, "appUserId", "businessOwnerAppUser 节点下没有包含 appUserId 节点");

        if (businessOwnerAppUser.getString("appUserId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "appUserId 错误，不能自动生成（必须已经存在的appUserId）" + businessOwnerAppUser);
        }
        //自动保存DEL
        autoSaveDelBusinessOwnerAppUser(business, businessOwnerAppUser);

        businessOwnerAppUser.put("bId", business.getbId());
        businessOwnerAppUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存绑定业主信息
        ownerAppUserServiceDaoImpl.saveBusinessOwnerAppUserInfo(businessOwnerAppUser);

    }


    public IOwnerAppUserServiceDao getOwnerAppUserServiceDaoImpl() {
        return ownerAppUserServiceDaoImpl;
    }

    public void setOwnerAppUserServiceDaoImpl(IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl) {
        this.ownerAppUserServiceDaoImpl = ownerAppUserServiceDaoImpl;
    }


}
