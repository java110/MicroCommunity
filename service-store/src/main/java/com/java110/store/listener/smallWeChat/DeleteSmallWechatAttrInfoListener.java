package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.store.dao.ISmallWechatAttrServiceDao;
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
 * 删除微信属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessSmallWechatAttr:{} 微信属性基本信息节点
 * 2、businessSmallWechatAttrAttr:[{}] 微信属性属性信息节点
 * 3、businessSmallWechatAttrPhoto:[{}] 微信属性照片信息节点
 * 4、businessSmallWechatAttrCerdentials:[{}] 微信属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteSmallWechatAttrInfoListener")
@Transactional
public class DeleteSmallWechatAttrInfoListener extends AbstractSmallWechatAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteSmallWechatAttrInfoListener.class);
    @Autowired
    ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WECHAT_ATTR;
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

        //处理 businessSmallWechatAttr 节点
        if (data.containsKey(SmallWechatAttrPo.class.getSimpleName())) {
            Object _obj = data.get(SmallWechatAttrPo.class.getSimpleName());
            JSONArray businessSmallWechatAttrs = null;
            if (_obj instanceof JSONObject) {
                businessSmallWechatAttrs = new JSONArray();
                businessSmallWechatAttrs.add(_obj);
            } else {
                businessSmallWechatAttrs = (JSONArray) _obj;
            }
            //JSONObject businessSmallWechatAttr = data.getJSONObject(SmallWechatAttrPo.class.getSimpleName());
            for (int _smallWechatAttrIndex = 0; _smallWechatAttrIndex < businessSmallWechatAttrs.size(); _smallWechatAttrIndex++) {
                JSONObject businessSmallWechatAttr = businessSmallWechatAttrs.getJSONObject(_smallWechatAttrIndex);
                doBusinessSmallWechatAttr(business, businessSmallWechatAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessSmallWechatAttr.getString("attrId"));
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
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //微信属性信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //微信属性信息
        List<Map> businessSmallWechatAttrInfos = smallWechatAttrServiceDaoImpl.getBusinessSmallWechatAttrInfo(info);
        if (businessSmallWechatAttrInfos != null && businessSmallWechatAttrInfos.size() > 0) {
            for (int _smallWechatAttrIndex = 0; _smallWechatAttrIndex < businessSmallWechatAttrInfos.size(); _smallWechatAttrIndex++) {
                Map businessSmallWechatAttrInfo = businessSmallWechatAttrInfos.get(_smallWechatAttrIndex);
                flushBusinessSmallWechatAttrInfo(businessSmallWechatAttrInfo, StatusConstant.STATUS_CD_INVALID);
                smallWechatAttrServiceDaoImpl.updateSmallWechatAttrInfoInstance(businessSmallWechatAttrInfo);
                dataFlowContext.addParamOut("attrId", businessSmallWechatAttrInfo.get("attr_id"));
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
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //微信属性信息
        List<Map> smallWechatAttrInfo = smallWechatAttrServiceDaoImpl.getSmallWechatAttrInfo(info);
        if (smallWechatAttrInfo != null && smallWechatAttrInfo.size() > 0) {

            //微信属性信息
            List<Map> businessSmallWechatAttrInfos = smallWechatAttrServiceDaoImpl.getBusinessSmallWechatAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessSmallWechatAttrInfos == null || businessSmallWechatAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（smallWechatAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _smallWechatAttrIndex = 0; _smallWechatAttrIndex < businessSmallWechatAttrInfos.size(); _smallWechatAttrIndex++) {
                Map businessSmallWechatAttrInfo = businessSmallWechatAttrInfos.get(_smallWechatAttrIndex);
                flushBusinessSmallWechatAttrInfo(businessSmallWechatAttrInfo, StatusConstant.STATUS_CD_VALID);
                smallWechatAttrServiceDaoImpl.updateSmallWechatAttrInfoInstance(businessSmallWechatAttrInfo);
            }
        }
    }


    /**
     * 处理 businessSmallWechatAttr 节点
     *
     * @param business                总的数据节点
     * @param businessSmallWechatAttr 微信属性节点
     */
    private void doBusinessSmallWechatAttr(Business business, JSONObject businessSmallWechatAttr) {

        Assert.jsonObjectHaveKey(businessSmallWechatAttr, "attrId", "businessSmallWechatAttr 节点下没有包含 attrId 节点");

        if (businessSmallWechatAttr.getString("attrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + businessSmallWechatAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessSmallWechatAttr(business, businessSmallWechatAttr);
    }

    @Override
    public ISmallWechatAttrServiceDao getSmallWechatAttrServiceDaoImpl() {
        return smallWechatAttrServiceDaoImpl;
    }

    public void setSmallWechatAttrServiceDaoImpl(ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl) {
        this.smallWechatAttrServiceDaoImpl = smallWechatAttrServiceDaoImpl;
    }
}
