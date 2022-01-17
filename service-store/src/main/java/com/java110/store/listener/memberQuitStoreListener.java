package com.java110.store.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IStoreServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户成员退出 侦听
 *
 * 处理节点
 * 1、businessMemberStore:{} 商户基本信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("memberQuitStoreListener")
@Transactional
public class memberQuitStoreListener extends AbstractStoreBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(memberQuitStoreListener.class);
    @Autowired
    IStoreServiceDao storeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_STORE;
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

        //处理 businessStore 节点 按理这里不应该处理，程序上支持，以防真有这种业务
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_STORE)){
            JSONObject memberStore = data.getJSONObject(BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_STORE);
            doBusinessMemberStore(business,memberStore);
            dataFlowContext.addParamOut("memberStoreId",memberStore.getString("memberStoreId"));
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
        Map businessMemberStore = storeServiceDaoImpl.getBusinessMemberStore(info);
        if( businessMemberStore != null && !businessMemberStore.isEmpty()) {
            flushBusinessStoreInfo(businessMemberStore,StatusConstant.STATUS_CD_INVALID);
            storeServiceDaoImpl.updateMemberStoreInstance(businessMemberStore);
            dataFlowContext.addParamOut("memberStoreId",businessMemberStore.get("member_store_id"));
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
        Map memberStore = storeServiceDaoImpl.getMemberStore(info);
        if(memberStore != null && !memberStore.isEmpty()){

            //商户信息
            Map businessMemberStore = storeServiceDaoImpl.getBusinessMemberStore(delInfo);
            //除非程序出错了，这里不会为空
            if(businessMemberStore == null || businessMemberStore.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（member store），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessStoreInfo(businessMemberStore,StatusConstant.STATUS_CD_VALID);
            storeServiceDaoImpl.updateMemberStoreInstance(businessMemberStore);
            dataFlowContext.addParamOut("memberStoreId",memberStore.get("member_store_id"));
        }
    }



    /**
     * 处理 businessStore 节点
     * @param business 总的数据节点
     * @param businessStore 商户节点
     */
    private void doBusinessMemberStore(Business business,JSONObject businessStore){

        Assert.jsonObjectHaveKey(businessStore,"memberStoreId","doBusinessMemberStore 节点下没有包含 memberStoreId 节点");

        if(businessStore.getString("memberStoreId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"memberStoreId 错误，不能自动生成（必须已经存在的memberStoreId）"+businessStore);
        }
        //自动插入DEL
        autoSaveDelBusinessMemberStore(business,businessStore);
    }




    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
