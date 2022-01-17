package com.java110.fee.listener.tempCarFeeConfigAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.fee.dao.ITempCarFeeConfigAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改临时车收费标准属性信息 侦听
 *
 * 处理节点
 * 1、businessTempCarFeeConfigAttr:{} 临时车收费标准属性基本信息节点
 * 2、businessTempCarFeeConfigAttrAttr:[{}] 临时车收费标准属性属性信息节点
 * 3、businessTempCarFeeConfigAttrPhoto:[{}] 临时车收费标准属性照片信息节点
 * 4、businessTempCarFeeConfigAttrCerdentials:[{}] 临时车收费标准属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateTempCarFeeConfigAttrInfoListener")
@Transactional
public class UpdateTempCarFeeConfigAttrInfoListener extends AbstractTempCarFeeConfigAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateTempCarFeeConfigAttrInfoListener.class);
    @Autowired
    private ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_ATTR_INFO;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");


            //处理 businessTempCarFeeConfigAttr 节点
            if(data.containsKey(TempCarFeeConfigAttrPo.class.getSimpleName())){
                Object _obj = data.get(TempCarFeeConfigAttrPo.class.getSimpleName());
                JSONArray businessTempCarFeeConfigAttrs = null;
                if(_obj instanceof JSONObject){
                    businessTempCarFeeConfigAttrs = new JSONArray();
                    businessTempCarFeeConfigAttrs.add(_obj);
                }else {
                    businessTempCarFeeConfigAttrs = (JSONArray)_obj;
                }
                //JSONObject businessTempCarFeeConfigAttr = data.getJSONObject(TempCarFeeConfigAttrPo.class.getSimpleName());
                for (int _tempCarFeeConfigAttrIndex = 0; _tempCarFeeConfigAttrIndex < businessTempCarFeeConfigAttrs.size();_tempCarFeeConfigAttrIndex++) {
                    JSONObject businessTempCarFeeConfigAttr = businessTempCarFeeConfigAttrs.getJSONObject(_tempCarFeeConfigAttrIndex);
                    doBusinessTempCarFeeConfigAttr(business, businessTempCarFeeConfigAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessTempCarFeeConfigAttr.getString("attrId"));
                    }
                }
            }
    }


    /**
     * business to instance 过程
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //临时车收费标准属性信息
        List<Map> businessTempCarFeeConfigAttrInfos = tempCarFeeConfigAttrServiceDaoImpl.getBusinessTempCarFeeConfigAttrInfo(info);
        if( businessTempCarFeeConfigAttrInfos != null && businessTempCarFeeConfigAttrInfos.size() >0) {
            for (int _tempCarFeeConfigAttrIndex = 0; _tempCarFeeConfigAttrIndex < businessTempCarFeeConfigAttrInfos.size();_tempCarFeeConfigAttrIndex++) {
                Map businessTempCarFeeConfigAttrInfo = businessTempCarFeeConfigAttrInfos.get(_tempCarFeeConfigAttrIndex);
                flushBusinessTempCarFeeConfigAttrInfo(businessTempCarFeeConfigAttrInfo,StatusConstant.STATUS_CD_VALID);
                tempCarFeeConfigAttrServiceDaoImpl.updateTempCarFeeConfigAttrInfoInstance(businessTempCarFeeConfigAttrInfo);
                if(businessTempCarFeeConfigAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("attrId", businessTempCarFeeConfigAttrInfo.get("attr_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //临时车收费标准属性信息
        List<Map> tempCarFeeConfigAttrInfo = tempCarFeeConfigAttrServiceDaoImpl.getTempCarFeeConfigAttrInfo(info);
        if(tempCarFeeConfigAttrInfo != null && tempCarFeeConfigAttrInfo.size() > 0){

            //临时车收费标准属性信息
            List<Map> businessTempCarFeeConfigAttrInfos = tempCarFeeConfigAttrServiceDaoImpl.getBusinessTempCarFeeConfigAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessTempCarFeeConfigAttrInfos == null || businessTempCarFeeConfigAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（tempCarFeeConfigAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _tempCarFeeConfigAttrIndex = 0; _tempCarFeeConfigAttrIndex < businessTempCarFeeConfigAttrInfos.size();_tempCarFeeConfigAttrIndex++) {
                Map businessTempCarFeeConfigAttrInfo = businessTempCarFeeConfigAttrInfos.get(_tempCarFeeConfigAttrIndex);
                flushBusinessTempCarFeeConfigAttrInfo(businessTempCarFeeConfigAttrInfo,StatusConstant.STATUS_CD_VALID);
                tempCarFeeConfigAttrServiceDaoImpl.updateTempCarFeeConfigAttrInfoInstance(businessTempCarFeeConfigAttrInfo);
            }
        }

    }



    /**
     * 处理 businessTempCarFeeConfigAttr 节点
     * @param business 总的数据节点
     * @param businessTempCarFeeConfigAttr 临时车收费标准属性节点
     */
    private void doBusinessTempCarFeeConfigAttr(Business business,JSONObject businessTempCarFeeConfigAttr){

        Assert.jsonObjectHaveKey(businessTempCarFeeConfigAttr,"attrId","businessTempCarFeeConfigAttr 节点下没有包含 attrId 节点");

        if(businessTempCarFeeConfigAttr.getString("attrId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessTempCarFeeConfigAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessTempCarFeeConfigAttr(business,businessTempCarFeeConfigAttr);

        businessTempCarFeeConfigAttr.put("bId",business.getbId());
        businessTempCarFeeConfigAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存临时车收费标准属性信息
        tempCarFeeConfigAttrServiceDaoImpl.saveBusinessTempCarFeeConfigAttrInfo(businessTempCarFeeConfigAttr);

    }



    @Override
    public ITempCarFeeConfigAttrServiceDao getTempCarFeeConfigAttrServiceDaoImpl() {
        return tempCarFeeConfigAttrServiceDaoImpl;
    }

    public void setTempCarFeeConfigAttrServiceDaoImpl(ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl) {
        this.tempCarFeeConfigAttrServiceDaoImpl = tempCarFeeConfigAttrServiceDaoImpl;
    }



}
