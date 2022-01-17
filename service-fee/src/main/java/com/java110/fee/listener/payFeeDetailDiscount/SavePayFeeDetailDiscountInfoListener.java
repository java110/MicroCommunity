package com.java110.fee.listener.payFeeDetailDiscount;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.fee.dao.IPayFeeDetailDiscountServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 缴费优惠信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("savePayFeeDetailDiscountInfoListener")
@Transactional
public class SavePayFeeDetailDiscountInfoListener extends AbstractPayFeeDetailDiscountBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SavePayFeeDetailDiscountInfoListener.class);

    @Autowired
    private IPayFeeDetailDiscountServiceDao payFeeDetailDiscountServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO;
    }

    /**
     * 保存缴费优惠信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessPayFeeDetailDiscount 节点
        if(data.containsKey(PayFeeDetailDiscountPo.class.getSimpleName())){
            Object bObj = data.get(PayFeeDetailDiscountPo.class.getSimpleName());
            JSONArray businessPayFeeDetailDiscounts = null;
            if(bObj instanceof JSONObject){
                businessPayFeeDetailDiscounts = new JSONArray();
                businessPayFeeDetailDiscounts.add(bObj);
            }else {
                businessPayFeeDetailDiscounts = (JSONArray)bObj;
            }
            //JSONObject businessPayFeeDetailDiscount = data.getJSONObject(PayFeeDetailDiscountPo.class.getSimpleName());
            for (int bPayFeeDetailDiscountIndex = 0; bPayFeeDetailDiscountIndex < businessPayFeeDetailDiscounts.size();bPayFeeDetailDiscountIndex++) {
                JSONObject businessPayFeeDetailDiscount = businessPayFeeDetailDiscounts.getJSONObject(bPayFeeDetailDiscountIndex);
                doBusinessPayFeeDetailDiscount(business, businessPayFeeDetailDiscount);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailDiscountId", businessPayFeeDetailDiscount.getString("detailDiscountId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //缴费优惠信息
        List<Map> businessPayFeeDetailDiscountInfo = payFeeDetailDiscountServiceDaoImpl.getBusinessPayFeeDetailDiscountInfo(info);
        if( businessPayFeeDetailDiscountInfo != null && businessPayFeeDetailDiscountInfo.size() >0) {
            reFreshShareColumn(info, businessPayFeeDetailDiscountInfo.get(0));
            payFeeDetailDiscountServiceDaoImpl.savePayFeeDetailDiscountInfoInstance(info);
            if(businessPayFeeDetailDiscountInfo.size() == 1) {
                dataFlowContext.addParamOut("detailDiscountId", businessPayFeeDetailDiscountInfo.get(0).get("detail_discount_id"));
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

        if (info.containsKey("community_id")) {
            return;
        }

        if (!businessInfo.containsKey("communityId")) {
            return;
        }

        info.put("community_id", businessInfo.get("communityId"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //缴费优惠信息
        List<Map> payFeeDetailDiscountInfo = payFeeDetailDiscountServiceDaoImpl.getPayFeeDetailDiscountInfo(info);
        if(payFeeDetailDiscountInfo != null && payFeeDetailDiscountInfo.size() > 0){
            reFreshShareColumn(paramIn, payFeeDetailDiscountInfo.get(0));
            payFeeDetailDiscountServiceDaoImpl.updatePayFeeDetailDiscountInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessPayFeeDetailDiscount 节点
     * @param business 总的数据节点
     * @param businessPayFeeDetailDiscount 缴费优惠节点
     */
    private void doBusinessPayFeeDetailDiscount(Business business,JSONObject businessPayFeeDetailDiscount){

        Assert.jsonObjectHaveKey(businessPayFeeDetailDiscount,"detailDiscountId","businessPayFeeDetailDiscount 节点下没有包含 detailDiscountId 节点");

        if(businessPayFeeDetailDiscount.getString("detailDiscountId").startsWith("-")){
            //刷新缓存
            //flushPayFeeDetailDiscountId(business.getDatas());

            businessPayFeeDetailDiscount.put("detailDiscountId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailDiscountId));

        }

        businessPayFeeDetailDiscount.put("bId",business.getbId());
        businessPayFeeDetailDiscount.put("operate", StatusConstant.OPERATE_ADD);
        //保存缴费优惠信息
        payFeeDetailDiscountServiceDaoImpl.saveBusinessPayFeeDetailDiscountInfo(businessPayFeeDetailDiscount);

    }
    @Override
    public IPayFeeDetailDiscountServiceDao getPayFeeDetailDiscountServiceDaoImpl() {
        return payFeeDetailDiscountServiceDaoImpl;
    }

    public void setPayFeeDetailDiscountServiceDaoImpl(IPayFeeDetailDiscountServiceDao payFeeDetailDiscountServiceDaoImpl) {
        this.payFeeDetailDiscountServiceDaoImpl = payFeeDetailDiscountServiceDaoImpl;
    }
}
