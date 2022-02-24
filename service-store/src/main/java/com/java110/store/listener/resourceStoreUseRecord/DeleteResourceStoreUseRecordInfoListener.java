package com.java110.store.listener.resourceStoreUseRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resourceStoreUseRecord.ResourceStoreUseRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.IResourceStoreUseRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除物品使用记录信息 侦听
 * <p>
 * 处理节点
 * 1、businessResourceStoreUseRecord:{} 物品使用记录基本信息节点
 * 2、businessResourceStoreUseRecordAttr:[{}] 物品使用记录属性信息节点
 * 3、businessResourceStoreUseRecordPhoto:[{}] 物品使用记录照片信息节点
 * 4、businessResourceStoreUseRecordCerdentials:[{}] 物品使用记录证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteResourceStoreUseRecordInfoListener")
@Transactional
public class DeleteResourceStoreUseRecordInfoListener extends AbstractResourceStoreUseRecordBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteResourceStoreUseRecordInfoListener.class);
    @Autowired
    IResourceStoreUseRecordServiceDao resourceResourceStoreUseRecordUseRecordServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE_USE_RECORD;
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

        //处理 businessResourceStoreUseRecord 节点
        if (data.containsKey(ResourceStoreUseRecordPo.class.getSimpleName())) {
            Object _obj = data.get(ResourceStoreUseRecordPo.class.getSimpleName());
            JSONArray businessResourceStoreUseRecords = null;
            if (_obj instanceof JSONObject) {
                businessResourceStoreUseRecords = new JSONArray();
                businessResourceStoreUseRecords.add(_obj);
            } else {
                businessResourceStoreUseRecords = (JSONArray) _obj;
            }
            //JSONObject businessResourceStoreUseRecord = data.getJSONObject(ResourceStoreUseRecordPo.class.getSimpleName());
            for (int _resourceResourceStoreUseRecordUseRecordIndex = 0; _resourceResourceStoreUseRecordUseRecordIndex < businessResourceStoreUseRecords.size(); _resourceResourceStoreUseRecordUseRecordIndex++) {
                JSONObject businessResourceStoreUseRecord = businessResourceStoreUseRecords.getJSONObject(_resourceResourceStoreUseRecordUseRecordIndex);
                doBusinessResourceStoreUseRecord(business, businessResourceStoreUseRecord);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rsurId", businessResourceStoreUseRecord.getString("rsurId"));
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

        //物品使用记录信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //物品使用记录信息
        List<Map> businessResourceStoreUseRecordInfos = resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getBusinessResourceStoreUseRecordInfo(info);
        if (businessResourceStoreUseRecordInfos != null && businessResourceStoreUseRecordInfos.size() > 0) {
            for (int _resourceResourceStoreUseRecordUseRecordIndex = 0; _resourceResourceStoreUseRecordUseRecordIndex < businessResourceStoreUseRecordInfos.size(); _resourceResourceStoreUseRecordUseRecordIndex++) {
                Map businessResourceStoreUseRecordInfo = businessResourceStoreUseRecordInfos.get(_resourceResourceStoreUseRecordUseRecordIndex);
                flushBusinessResourceStoreUseRecordInfo(businessResourceStoreUseRecordInfo, StatusConstant.STATUS_CD_INVALID);
                resourceResourceStoreUseRecordUseRecordServiceDaoImpl.updateResourceStoreUseRecordInfoInstance(businessResourceStoreUseRecordInfo);
                dataFlowContext.addParamOut("rsurId", businessResourceStoreUseRecordInfo.get("rsur_id"));
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
        //物品使用记录信息
        List<Map> resourceResourceStoreUseRecordUseRecordInfo = resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getResourceStoreUseRecordInfo(info);
        if (resourceResourceStoreUseRecordUseRecordInfo != null && resourceResourceStoreUseRecordUseRecordInfo.size() > 0) {

            //物品使用记录信息
            List<Map> businessResourceStoreUseRecordInfos = resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getBusinessResourceStoreUseRecordInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessResourceStoreUseRecordInfos == null || businessResourceStoreUseRecordInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（resourceResourceStoreUseRecordUseRecord），程序内部异常,请检查！ " + delInfo);
            }
            for (int _resourceResourceStoreUseRecordUseRecordIndex = 0; _resourceResourceStoreUseRecordUseRecordIndex < businessResourceStoreUseRecordInfos.size(); _resourceResourceStoreUseRecordUseRecordIndex++) {
                Map businessResourceStoreUseRecordInfo = businessResourceStoreUseRecordInfos.get(_resourceResourceStoreUseRecordUseRecordIndex);
                flushBusinessResourceStoreUseRecordInfo(businessResourceStoreUseRecordInfo, StatusConstant.STATUS_CD_VALID);
                resourceResourceStoreUseRecordUseRecordServiceDaoImpl.updateResourceStoreUseRecordInfoInstance(businessResourceStoreUseRecordInfo);
            }
        }
    }


    /**
     * 处理 businessResourceStoreUseRecord 节点
     *
     * @param business                       总的数据节点
     * @param businessResourceStoreUseRecord 物品使用记录节点
     */
    private void doBusinessResourceStoreUseRecord(Business business, JSONObject businessResourceStoreUseRecord) {

        Assert.jsonObjectHaveKey(businessResourceStoreUseRecord, "rsurId", "businessResourceStoreUseRecord 节点下没有包含 rsurId 节点");

        if (businessResourceStoreUseRecord.getString("rsurId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "rsurId 错误，不能自动生成（必须已经存在的rsurId）" + businessResourceStoreUseRecord);
        }
        //自动插入DEL
        autoSaveDelBusinessResourceStoreUseRecord(business, businessResourceStoreUseRecord);
    }

    @Override
    public IResourceStoreUseRecordServiceDao getResourceStoreUseRecordServiceDaoImpl() {
        return resourceResourceStoreUseRecordUseRecordServiceDaoImpl;
    }

    public void setResourceStoreUseRecordServiceDaoImpl(IResourceStoreUseRecordServiceDao resourceResourceStoreUseRecordUseRecordServiceDaoImpl) {
        this.resourceResourceStoreUseRecordUseRecordServiceDaoImpl = resourceResourceStoreUseRecordUseRecordServiceDaoImpl;
    }
}
