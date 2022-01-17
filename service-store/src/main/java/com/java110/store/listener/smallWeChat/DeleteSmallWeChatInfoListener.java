package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.store.SmallWechatPo;
import com.java110.store.dao.ISmallWeChatServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除小程序管理信息 侦听
 * <p>
 * 处理节点
 * 1、businessSmallWeChat:{} 小程序管理基本信息节点
 * 2、businessSmallWeChatAttr:[{}] 小程序管理属性信息节点
 * 3、businessSmallWeChatPhoto:[{}] 小程序管理照片信息节点
 * 4、businessSmallWeChatCerdentials:[{}] 小程序管理证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteSmallWeChatInfoListener")
@Transactional
public class DeleteSmallWeChatInfoListener extends AbstractSmallWeChatBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteSmallWeChatInfoListener.class);
    @Autowired
    ISmallWeChatServiceDao smallWeChatServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WE_CHAT;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");
        if (data.containsKey(SmallWechatPo.class.getSimpleName())) {
            Object _obj = data.get(SmallWechatPo.class.getSimpleName());
            JSONArray businessSmallWeChats = null;
            if (_obj instanceof JSONObject) {
                businessSmallWeChats = new JSONArray();
                businessSmallWeChats.add(_obj);
            } else {
                businessSmallWeChats = (JSONArray) _obj;
            }
            for (int _smallWeChatIndex = 0; _smallWeChatIndex < businessSmallWeChats.size(); _smallWeChatIndex++) {
                JSONObject businessSmallWeChat = businessSmallWeChats.getJSONObject(_smallWeChatIndex);
                doBusinessSmallWeChat(business, businessSmallWeChat);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("weChatId", businessSmallWeChat.getString("weChatId"));
                }
            }
        }


    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);
        List<Map> businessSmallWeChatInfos = smallWeChatServiceDaoImpl.getBusinessSmallWeChatInfo(info);
        if (businessSmallWeChatInfos != null && businessSmallWeChatInfos.size() > 0) {
            for (int _smallWeChatIndex = 0; _smallWeChatIndex < businessSmallWeChatInfos.size(); _smallWeChatIndex++) {
                Map businessSmallWeChatInfo = businessSmallWeChatInfos.get(_smallWeChatIndex);
                flushBusinessSmallWeChatInfo(businessSmallWeChatInfo, StatusConstant.STATUS_CD_INVALID);
                smallWeChatServiceDaoImpl.updateSmallWeChatInfoInstance(businessSmallWeChatInfo);
                dataFlowContext.addParamOut("weChatId", businessSmallWeChatInfo.get("weChat_id"));
            }
        }
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        List<Map> smallWeChatInfo = smallWeChatServiceDaoImpl.getSmallWeChatInfo(info);
        if (smallWeChatInfo != null && smallWeChatInfo.size() > 0) {
            List<Map> businessSmallWeChatInfos = smallWeChatServiceDaoImpl.getBusinessSmallWeChatInfo(delInfo);
            if (businessSmallWeChatInfos == null || businessSmallWeChatInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（smallWeChat），程序内部异常,请检查！ " + delInfo);
            }
            for (int _smallWeChatIndex = 0; _smallWeChatIndex < businessSmallWeChatInfos.size(); _smallWeChatIndex++) {
                Map businessSmallWeChatInfo = businessSmallWeChatInfos.get(_smallWeChatIndex);
                flushBusinessSmallWeChatInfo(businessSmallWeChatInfo, StatusConstant.STATUS_CD_VALID);
                smallWeChatServiceDaoImpl.updateSmallWeChatInfoInstance(businessSmallWeChatInfo);
            }
        }
    }


    /**
     * 处理 businessSmallWeChat 节点
     *
     * @param business            总的数据节点
     * @param businessSmallWeChat 小程序管理节点
     */
    private void doBusinessSmallWeChat(Business business, JSONObject businessSmallWeChat) {
        Assert.jsonObjectHaveKey(businessSmallWeChat, "weChatId", "businessSmallWeChat 节点下没有包含 weChatId 节点");
        if (businessSmallWeChat.getString("weChatId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "weChatId 错误，不能自动生成（必须已经存在的weChatId）" + businessSmallWeChat);
        }
        autoSaveDelBusinessSmallWeChat(business, businessSmallWeChat);
    }

    public ISmallWeChatServiceDao getSmallWeChatServiceDaoImpl() {
        return smallWeChatServiceDaoImpl;
    }

    public void setSmallWeChatServiceDaoImpl(ISmallWeChatServiceDao smallWeChatServiceDaoImpl) {
        this.smallWeChatServiceDaoImpl = smallWeChatServiceDaoImpl;
    }
}
