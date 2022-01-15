package com.java110.store.listener.purchaseApply;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.center.Business;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.dao.IPurchaseApplyServiceDao;
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
 * 删除采购申请信息 侦听
 * <p>
 * 处理节点
 * 1、businessPurchaseApply:{} 采购申请基本信息节点
 * 2、businessPurchaseApplyAttr:[{}] 采购申请属性信息节点
 * 3、businessPurchaseApplyPhoto:[{}] 采购申请照片信息节点
 * 4、businessPurchaseApplyCerdentials:[{}] 采购申请证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deletePurchaseApplyInfoListener")
@Transactional
public class DeletePurchaseApplyInfoListener extends AbstractPurchaseApplyBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeletePurchaseApplyInfoListener.class);
    @Autowired
    IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY;
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

        //处理 businessPurchaseApply 节点
        if (data.containsKey(PurchaseApplyPo.class.getSimpleName())) {
            Object _obj = data.get(PurchaseApplyPo.class.getSimpleName());
            JSONArray businessPurchaseApplys = null;
            if (_obj instanceof JSONObject) {
                businessPurchaseApplys = new JSONArray();
                businessPurchaseApplys.add(_obj);
            } else {
                businessPurchaseApplys = (JSONArray) _obj;
            }
            //JSONObject businessPurchaseApply = data.getJSONObject("businessPurchaseApply");
            for (int _purchaseApplyIndex = 0; _purchaseApplyIndex < businessPurchaseApplys.size(); _purchaseApplyIndex++) {
                JSONObject businessPurchaseApply = businessPurchaseApplys.getJSONObject(_purchaseApplyIndex);
                doBusinessPurchaseApply(business, businessPurchaseApply);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("applyOrderId", businessPurchaseApply.getString("applyOrderId"));
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

        //采购申请信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);


        List<Map> businessPurchaseApplyInfos = purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyInfo(info);
        if (businessPurchaseApplyInfos != null && businessPurchaseApplyInfos.size() > 0) {
            for (int _purchaseApplyIndex = 0; _purchaseApplyIndex < businessPurchaseApplyInfos.size(); _purchaseApplyIndex++) {
                Map businessPurchaseApplyInfo = businessPurchaseApplyInfos.get(_purchaseApplyIndex);
                flushBusinessPurchaseApplyInfo(businessPurchaseApplyInfo, StatusConstant.STATUS_CD_INVALID);
                purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance(businessPurchaseApplyInfo);
                //取消流程审批
                //查询任务
                PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
                purchaseDto.setBusinessKey(businessPurchaseApplyInfo.get("apply_order_id").toString());
                List<PurchaseApplyDto>  purchaseApplyDtoList=purchaseApplyInnerServiceSMOImpl.getActRuTaskId(purchaseDto);
                if(purchaseApplyDtoList!=null && purchaseApplyDtoList.size()>0){
                    PurchaseApplyDto purchaseDto1 = new PurchaseApplyDto();
                    purchaseDto1.setActRuTaskId(purchaseApplyDtoList.get(0).getActRuTaskId());
                    purchaseDto1.setAssigneeUser("999999");
                    purchaseApplyInnerServiceSMOImpl.updateActRuTaskById(purchaseDto1);
                }
                dataFlowContext.addParamOut("applyOrderId", businessPurchaseApplyInfo.get("apply_order_id"));
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
        //采购申请信息
        List<Map> purchaseApplyInfo = purchaseApplyServiceDaoImpl.getPurchaseApplyInfo(info);
        if (purchaseApplyInfo != null && purchaseApplyInfo.size() > 0) {

            //采购申请信息
            List<Map> businessPurchaseApplyInfos = purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessPurchaseApplyInfos == null || businessPurchaseApplyInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（purchaseApply），程序内部异常,请检查！ " + delInfo);
            }
            for (int _purchaseApplyIndex = 0; _purchaseApplyIndex < businessPurchaseApplyInfos.size(); _purchaseApplyIndex++) {
                Map businessPurchaseApplyInfo = businessPurchaseApplyInfos.get(_purchaseApplyIndex);
                flushBusinessPurchaseApplyInfo(businessPurchaseApplyInfo, StatusConstant.STATUS_CD_VALID);
                purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance(businessPurchaseApplyInfo);
            }
        }
    }


    /**
     * 处理 businessPurchaseApply 节点
     *
     * @param business              总的数据节点
     * @param businessPurchaseApply 采购申请节点
     */
    private void doBusinessPurchaseApply(Business business, JSONObject businessPurchaseApply) {

        Assert.jsonObjectHaveKey(businessPurchaseApply, "applyOrderId", "businessPurchaseApply 节点下没有包含 applyOrderId 节点");

        if (businessPurchaseApply.getString("applyOrderId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "applyOrderId 错误，不能自动生成（必须已经存在的applyOrderId）" + businessPurchaseApply);
        }
        //自动插入DEL
        autoSaveDelBusinessPurchaseApply(business, businessPurchaseApply);
    }

    public IPurchaseApplyServiceDao getPurchaseApplyServiceDaoImpl() {
        return purchaseApplyServiceDaoImpl;
    }

    public void setPurchaseApplyServiceDaoImpl(IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl) {
        this.purchaseApplyServiceDaoImpl = purchaseApplyServiceDaoImpl;
    }
}
