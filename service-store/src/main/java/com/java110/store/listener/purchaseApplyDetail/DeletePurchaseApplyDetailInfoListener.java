package com.java110.store.listener.purchaseApplyDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.dao.IPurchaseApplyDetailServiceDao;
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
 * 删除订单明细信息 侦听
 * <p>
 * 处理节点
 * 1、businessPurchaseApplyDetail:{} 订单明细基本信息节点
 * 2、businessPurchaseApplyDetailAttr:[{}] 订单明细属性信息节点
 * 3、businessPurchaseApplyDetailPhoto:[{}] 订单明细照片信息节点
 * 4、businessPurchaseApplyDetailCerdentials:[{}] 订单明细证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deletePurchaseApplyDetailInfoListener")
@Transactional
public class DeletePurchaseApplyDetailInfoListener extends AbstractPurchaseApplyDetailBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeletePurchaseApplyDetailInfoListener.class);
    @Autowired
    IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL;
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


        //处理 businessPurchaseApplyDetail 节点
        if (data.containsKey(PurchaseApplyDetailPo.class.getSimpleName())) {
            Object _obj = data.get(PurchaseApplyDetailPo.class.getSimpleName());
            JSONArray businessPurchaseApplyDetails = null;
            if (_obj instanceof JSONObject) {
                businessPurchaseApplyDetails = new JSONArray();
                businessPurchaseApplyDetails.add(_obj);
            } else {
                businessPurchaseApplyDetails = (JSONArray) _obj;
            }
            //JSONObject businessPurchaseApplyDetail = data.getJSONObject("businessPurchaseApplyDetail");
            for (int _purchaseApplyDetailIndex = 0; _purchaseApplyDetailIndex < businessPurchaseApplyDetails.size(); _purchaseApplyDetailIndex++) {
                JSONObject businessPurchaseApplyDetail = businessPurchaseApplyDetails.getJSONObject(_purchaseApplyDetailIndex);
                doBusinessPurchaseApplyDetail(business, businessPurchaseApplyDetail);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("id", businessPurchaseApplyDetail.getString("id"));
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

        //订单明细信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //订单明细信息
        List<Map> businessPurchaseApplyDetailInfos = purchaseApplyDetailServiceDaoImpl.getBusinessPurchaseApplyDetailInfo(info);
        if (businessPurchaseApplyDetailInfos != null && businessPurchaseApplyDetailInfos.size() > 0) {
            for (int _purchaseApplyDetailIndex = 0; _purchaseApplyDetailIndex < businessPurchaseApplyDetailInfos.size(); _purchaseApplyDetailIndex++) {
                Map businessPurchaseApplyDetailInfo = businessPurchaseApplyDetailInfos.get(_purchaseApplyDetailIndex);
                flushBusinessPurchaseApplyDetailInfo(businessPurchaseApplyDetailInfo, StatusConstant.STATUS_CD_INVALID);
                purchaseApplyDetailServiceDaoImpl.updatePurchaseApplyDetailInfoInstance(businessPurchaseApplyDetailInfo);
                dataFlowContext.addParamOut("id", businessPurchaseApplyDetailInfo.get("id"));
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
        //订单明细信息
        List<Map> purchaseApplyDetailInfo = purchaseApplyDetailServiceDaoImpl.getPurchaseApplyDetailInfo(info);
        if (purchaseApplyDetailInfo != null && purchaseApplyDetailInfo.size() > 0) {

            //订单明细信息
            List<Map> businessPurchaseApplyDetailInfos = purchaseApplyDetailServiceDaoImpl.getBusinessPurchaseApplyDetailInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessPurchaseApplyDetailInfos == null || businessPurchaseApplyDetailInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（purchaseApplyDetail），程序内部异常,请检查！ " + delInfo);
            }
            for (int _purchaseApplyDetailIndex = 0; _purchaseApplyDetailIndex < businessPurchaseApplyDetailInfos.size(); _purchaseApplyDetailIndex++) {
                Map businessPurchaseApplyDetailInfo = businessPurchaseApplyDetailInfos.get(_purchaseApplyDetailIndex);
                flushBusinessPurchaseApplyDetailInfo(businessPurchaseApplyDetailInfo, StatusConstant.STATUS_CD_VALID);
                purchaseApplyDetailServiceDaoImpl.updatePurchaseApplyDetailInfoInstance(businessPurchaseApplyDetailInfo);
            }
        }
    }


    /**
     * 处理 businessPurchaseApplyDetail 节点
     *
     * @param business                    总的数据节点
     * @param businessPurchaseApplyDetail 订单明细节点
     */
    private void doBusinessPurchaseApplyDetail(Business business, JSONObject businessPurchaseApplyDetail) {
        Assert.jsonObjectHaveKey(businessPurchaseApplyDetail, "applyOrderId", "businessPurchaseApplyDetail 节点下没有包含 applyOrderId 节点");
        if (businessPurchaseApplyDetail.getString("applyOrderId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "applyOrderId 错误，不能自动生成（必须已经存在的applyOrderId）" + businessPurchaseApplyDetail);
        }
        //自动插入DEL
        autoSaveDelBusinessPurchaseApplyDetail(business, businessPurchaseApplyDetail);
    }


    @Override
    public IPurchaseApplyDetailServiceDao getPurchaseApplyDetailServiceDaoImpl() {
        return purchaseApplyDetailServiceDaoImpl;
    }

    public void setPurchaseApplyDetailServiceDaoImpl(IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl) {
        this.purchaseApplyDetailServiceDaoImpl = purchaseApplyDetailServiceDaoImpl;
    }
}
