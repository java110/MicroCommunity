package com.java110.community.listener.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.community.dao.IApplyRoomDiscountRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除验房记录信息 侦听
 * <p>
 * 处理节点
 * 1、businessApplyRoomDiscountRecord:{} 验房记录基本信息节点
 * 2、businessApplyRoomDiscountRecordAttr:[{}] 验房记录属性信息节点
 * 3、businessApplyRoomDiscountRecordPhoto:[{}] 验房记录照片信息节点
 * 4、businessApplyRoomDiscountRecordCerdentials:[{}] 验房记录证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteApplyRoomDiscountRecordInfoListener")
@Transactional
public class DeleteApplyRoomDiscountRecordInfoListener extends AbstractApplyRoomDiscountRecordBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteApplyRoomDiscountRecordInfoListener.class);
    @Autowired
    IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_APPLY_ROOM_DISCOUNT_RECORD;
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

        //处理 businessApplyRoomDiscountRecord 节点
        if (data.containsKey(ApplyRoomDiscountRecordPo.class.getSimpleName())) {
            Object _obj = data.get(ApplyRoomDiscountRecordPo.class.getSimpleName());
            JSONArray businessApplyRoomDiscountRecords = null;
            if (_obj instanceof JSONObject) {
                businessApplyRoomDiscountRecords = new JSONArray();
                businessApplyRoomDiscountRecords.add(_obj);
            } else {
                businessApplyRoomDiscountRecords = (JSONArray) _obj;
            }
            //JSONObject businessApplyRoomDiscountRecord = data.getJSONObject(ApplyRoomDiscountRecordPo.class.getSimpleName());
            for (int _applyRoomDiscountRecordIndex = 0; _applyRoomDiscountRecordIndex < businessApplyRoomDiscountRecords.size(); _applyRoomDiscountRecordIndex++) {
                JSONObject businessApplyRoomDiscountRecord = businessApplyRoomDiscountRecords.getJSONObject(_applyRoomDiscountRecordIndex);
                doBusinessApplyRoomDiscountRecord(business, businessApplyRoomDiscountRecord);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ardrId", businessApplyRoomDiscountRecord.getString("ardrId"));
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

        //验房记录信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //验房记录信息
        List<Map> businessApplyRoomDiscountRecordInfos = applyRoomDiscountRecordServiceDaoImpl.getBusinessApplyRoomDiscountRecordInfo(info);
        if (businessApplyRoomDiscountRecordInfos != null && businessApplyRoomDiscountRecordInfos.size() > 0) {
            for (int _applyRoomDiscountRecordIndex = 0; _applyRoomDiscountRecordIndex < businessApplyRoomDiscountRecordInfos.size(); _applyRoomDiscountRecordIndex++) {
                Map businessApplyRoomDiscountRecordInfo = businessApplyRoomDiscountRecordInfos.get(_applyRoomDiscountRecordIndex);
                flushBusinessApplyRoomDiscountRecordInfo(businessApplyRoomDiscountRecordInfo, StatusConstant.STATUS_CD_INVALID);
                applyRoomDiscountRecordServiceDaoImpl.updateApplyRoomDiscountRecordInfoInstance(businessApplyRoomDiscountRecordInfo);
                dataFlowContext.addParamOut("ardrId", businessApplyRoomDiscountRecordInfo.get("ardr_id"));
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
        //验房记录信息
        List<Map> applyRoomDiscountRecordInfo = applyRoomDiscountRecordServiceDaoImpl.getApplyRoomDiscountRecordInfo(info);
        if (applyRoomDiscountRecordInfo != null && applyRoomDiscountRecordInfo.size() > 0) {

            //验房记录信息
            List<Map> businessApplyRoomDiscountRecordInfos = applyRoomDiscountRecordServiceDaoImpl.getBusinessApplyRoomDiscountRecordInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessApplyRoomDiscountRecordInfos == null || businessApplyRoomDiscountRecordInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（applyRoomDiscountRecord），程序内部异常,请检查！ " + delInfo);
            }
            for (int _applyRoomDiscountRecordIndex = 0; _applyRoomDiscountRecordIndex < businessApplyRoomDiscountRecordInfos.size(); _applyRoomDiscountRecordIndex++) {
                Map businessApplyRoomDiscountRecordInfo = businessApplyRoomDiscountRecordInfos.get(_applyRoomDiscountRecordIndex);
                flushBusinessApplyRoomDiscountRecordInfo(businessApplyRoomDiscountRecordInfo, StatusConstant.STATUS_CD_VALID);
                applyRoomDiscountRecordServiceDaoImpl.updateApplyRoomDiscountRecordInfoInstance(businessApplyRoomDiscountRecordInfo);
            }
        }
    }


    /**
     * 处理 businessApplyRoomDiscountRecord 节点
     *
     * @param business                        总的数据节点
     * @param businessApplyRoomDiscountRecord 验房记录节点
     */
    private void doBusinessApplyRoomDiscountRecord(Business business, JSONObject businessApplyRoomDiscountRecord) {

        Assert.jsonObjectHaveKey(businessApplyRoomDiscountRecord, "ardrId", "businessApplyRoomDiscountRecord 节点下没有包含 ardrId 节点");

        if (businessApplyRoomDiscountRecord.getString("ardrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "ardrId 错误，不能自动生成（必须已经存在的ardrId）" + businessApplyRoomDiscountRecord);
        }
        //自动插入DEL
        autoSaveDelBusinessApplyRoomDiscountRecord(business, businessApplyRoomDiscountRecord);
    }

    @Override
    public IApplyRoomDiscountRecordServiceDao getApplyRoomDiscountRecordServiceDaoImpl() {
        return applyRoomDiscountRecordServiceDaoImpl;
    }

    public void setApplyRoomDiscountRecordServiceDaoImpl(IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl) {
        this.applyRoomDiscountRecordServiceDaoImpl = applyRoomDiscountRecordServiceDaoImpl;
    }
}
