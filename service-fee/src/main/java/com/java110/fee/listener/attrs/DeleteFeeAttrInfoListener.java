package com.java110.fee.listener.attrs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.fee.FeeAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IFeeAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除费用属性信息 侦听
 *
 * 处理节点
 * 1、businessFeeAttr:{} 费用属性基本信息节点
 * 2、businessFeeAttrAttr:[{}] 费用属性属性信息节点
 * 3、businessFeeAttrPhoto:[{}] 费用属性照片信息节点
 * 4、businessFeeAttrCerdentials:[{}] 费用属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteFeeAttrInfoListener")
@Transactional
public class DeleteFeeAttrInfoListener extends AbstractFeeAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteFeeAttrInfoListener.class);
    @Autowired
    IFeeAttrServiceDao feeAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO;
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

            //处理 businessFeeAttr 节点
            if(data.containsKey(FeeAttrPo.class.getSimpleName())){
                Object _obj = data.get(FeeAttrPo.class.getSimpleName());
                JSONArray businessFeeAttrs = null;
                if(_obj instanceof JSONObject){
                    businessFeeAttrs = new JSONArray();
                    businessFeeAttrs.add(_obj);
                }else {
                    businessFeeAttrs = (JSONArray)_obj;
                }
                //JSONObject businessFeeAttr = data.getJSONObject("businessFeeAttr");
                for (int _feeAttrIndex = 0; _feeAttrIndex < businessFeeAttrs.size();_feeAttrIndex++) {
                    JSONObject businessFeeAttr = businessFeeAttrs.getJSONObject(_feeAttrIndex);
                    doBusinessFeeAttr(business, businessFeeAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessFeeAttr.getString("attrId"));
                    }
                }
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

        //费用属性信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //费用属性信息
        List<Map> businessFeeAttrInfos = feeAttrServiceDaoImpl.getBusinessFeeAttrInfo(info);
        if( businessFeeAttrInfos != null && businessFeeAttrInfos.size() >0) {
            for (int _feeAttrIndex = 0; _feeAttrIndex < businessFeeAttrInfos.size();_feeAttrIndex++) {
                Map businessFeeAttrInfo = businessFeeAttrInfos.get(_feeAttrIndex);
                flushBusinessFeeAttrInfo(businessFeeAttrInfo,StatusConstant.STATUS_CD_INVALID);
                feeAttrServiceDaoImpl.updateFeeAttrInfoInstance(businessFeeAttrInfo);
                dataFlowContext.addParamOut("attrId",businessFeeAttrInfo.get("attr_id"));
            }
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
        //费用属性信息
        List<Map> feeAttrInfo = feeAttrServiceDaoImpl.getFeeAttrInfo(info);
        if(feeAttrInfo != null && feeAttrInfo.size() > 0){

            //费用属性信息
            List<Map> businessFeeAttrInfos = feeAttrServiceDaoImpl.getBusinessFeeAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessFeeAttrInfos == null ||  businessFeeAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（feeAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _feeAttrIndex = 0; _feeAttrIndex < businessFeeAttrInfos.size();_feeAttrIndex++) {
                Map businessFeeAttrInfo = businessFeeAttrInfos.get(_feeAttrIndex);
                flushBusinessFeeAttrInfo(businessFeeAttrInfo,StatusConstant.STATUS_CD_VALID);
                feeAttrServiceDaoImpl.updateFeeAttrInfoInstance(businessFeeAttrInfo);
            }
        }
    }



    /**
     * 处理 businessFeeAttr 节点
     * @param business 总的数据节点
     * @param businessFeeAttr 费用属性节点
     */
    private void doBusinessFeeAttr(Business business,JSONObject businessFeeAttr){

        Assert.jsonObjectHaveKey(businessFeeAttr,"attrId","businessFeeAttr 节点下没有包含 attrId 节点");

        if(businessFeeAttr.getString("attrId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessFeeAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessFeeAttr(business,businessFeeAttr);
    }

    public IFeeAttrServiceDao getFeeAttrServiceDaoImpl() {
        return feeAttrServiceDaoImpl;
    }

    public void setFeeAttrServiceDaoImpl(IFeeAttrServiceDao feeAttrServiceDaoImpl) {
        this.feeAttrServiceDaoImpl = feeAttrServiceDaoImpl;
    }
}
