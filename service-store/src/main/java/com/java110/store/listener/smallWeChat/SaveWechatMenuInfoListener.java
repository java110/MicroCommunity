package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.wechatMenu.WechatMenuPo;
import com.java110.store.dao.IWechatMenuServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 公众号菜单信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveWechatMenuInfoListener")
@Transactional
public class SaveWechatMenuInfoListener extends AbstractWechatMenuBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveWechatMenuInfoListener.class);

    @Autowired
    private IWechatMenuServiceDao wechatMenuServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_WECHAT_MENU;
    }

    /**
     * 保存公众号菜单信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessWechatMenu 节点
        if (data.containsKey(WechatMenuPo.class.getSimpleName())) {
            Object bObj = data.get(WechatMenuPo.class.getSimpleName());
            JSONArray businessWechatMenus = null;
            if (bObj instanceof JSONObject) {
                businessWechatMenus = new JSONArray();
                businessWechatMenus.add(bObj);
            } else {
                businessWechatMenus = (JSONArray) bObj;
            }
            //JSONObject businessWechatMenu = data.getJSONObject(WechatMenuPo.class.getSimpleName());
            for (int bWechatMenuIndex = 0; bWechatMenuIndex < businessWechatMenus.size(); bWechatMenuIndex++) {
                JSONObject businessWechatMenu = businessWechatMenus.getJSONObject(bWechatMenuIndex);
                doBusinessWechatMenu(business, businessWechatMenu);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("wechatMenuId", businessWechatMenu.getString("wechatMenuId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
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

        //公众号菜单信息
        List<Map> businessWechatMenuInfo = wechatMenuServiceDaoImpl.getBusinessWechatMenuInfo(info);
        if (businessWechatMenuInfo != null && businessWechatMenuInfo.size() > 0) {
            reFreshShareColumn(info, businessWechatMenuInfo.get(0));
            wechatMenuServiceDaoImpl.saveWechatMenuInfoInstance(info);
            if (businessWechatMenuInfo.size() == 1) {
                dataFlowContext.addParamOut("wechatMenuId", businessWechatMenuInfo.get(0).get("wechat_menu_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //公众号菜单信息
        List<Map> wechatMenuInfo = wechatMenuServiceDaoImpl.getWechatMenuInfo(info);
        if (wechatMenuInfo != null && wechatMenuInfo.size() > 0) {
            reFreshShareColumn(paramIn, wechatMenuInfo.get(0));
            wechatMenuServiceDaoImpl.updateWechatMenuInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessWechatMenu 节点
     *
     * @param business           总的数据节点
     * @param businessWechatMenu 公众号菜单节点
     */
    private void doBusinessWechatMenu(Business business, JSONObject businessWechatMenu) {

        Assert.jsonObjectHaveKey(businessWechatMenu, "wechatMenuId", "businessWechatMenu 节点下没有包含 wechatMenuId 节点");

        if (businessWechatMenu.getString("wechatMenuId").startsWith("-")) {
            //刷新缓存
            //flushWechatMenuId(business.getDatas());

            businessWechatMenu.put("wechatMenuId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wechatMenuId));

        }

        businessWechatMenu.put("bId", business.getbId());
        businessWechatMenu.put("operate", StatusConstant.OPERATE_ADD);
        //保存公众号菜单信息
        wechatMenuServiceDaoImpl.saveBusinessWechatMenuInfo(businessWechatMenu);

    }

    @Override
    public IWechatMenuServiceDao getWechatMenuServiceDaoImpl() {
        return wechatMenuServiceDaoImpl;
    }

    public void setWechatMenuServiceDaoImpl(IWechatMenuServiceDao wechatMenuServiceDaoImpl) {
        this.wechatMenuServiceDaoImpl = wechatMenuServiceDaoImpl;
    }
}
