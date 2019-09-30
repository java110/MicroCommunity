package com.java110.shop.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除商户信息 侦听
 *
 * 处理节点
 * 1、businessShop:{} 商户基本信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteShopDescListener")
@Transactional
public class DeleteShopDescListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteShopDescListener.class);
    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_SHOP_INFO;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessShop 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if(data.containsKey("businessShopDesc")){
            JSONObject businessShopDesc = data.getJSONObject("businessShopDesc");
            doBusinessShopDesc(business,businessShopDesc);
            dataFlowContext.addParamOut("shopDescId",businessShopDesc.getString("shopDescId"));
        }
    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //商户信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //商户信息
        Map businessShopDesc = shopServiceDaoImpl.getBusinessShopDesc(info);
        if( businessShopDesc != null && !businessShopDesc.isEmpty()) {
            flushBusinessShopDesc(businessShopDesc,StatusConstant.STATUS_CD_INVALID);
            shopServiceDaoImpl.updateShopDescInstance(businessShopDesc);
            dataFlowContext.addParamOut("shopDescId",businessShopDesc.get("shop_desc_id"));
        }
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //商户信息
        Map shopDesc = shopServiceDaoImpl.getShopDesc(info);
        if(shopDesc != null && !shopDesc.isEmpty()){

            //商户信息
            Map businessShopDesc = shopServiceDaoImpl.getBusinessShopDesc(delInfo);
            //除非程序出错了，这里不会为空
            if(businessShopDesc == null || businessShopDesc.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（shop_desc），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessShopDesc(businessShopDesc,StatusConstant.STATUS_CD_VALID);
            shopServiceDaoImpl.updateShopDescInstance(businessShopDesc);
            dataFlowContext.addParamOut("shopDescId",shopDesc.get("shop_desc_id"));
        }
    }


    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param businessShopDesc 商品优惠节点
     */
    private void doBusinessShopDesc(Business business,JSONObject businessShopDesc){

        Assert.jsonObjectHaveKey(businessShopDesc,"shopDescId","businessShopDesc 节点下没有包含 shopDescId 节点");

        if(businessShopDesc.getString("shopDescId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"shopDescId 错误，不能自动生成（必须已经存在的shopDescId）"+businessShopDesc);
        }
        //自动插入DEL
        autoSaveDelBusinessShopDesc(business,businessShopDesc);
    }

    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
