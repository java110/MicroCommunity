package com.java110.community.listener.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.community.dao.IApplyRoomDiscountRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验房记录 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractApplyRoomDiscountRecordBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractApplyRoomDiscountRecordBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IApplyRoomDiscountRecordServiceDao getApplyRoomDiscountRecordServiceDaoImpl();

    /**
     * 刷新 businessApplyRoomDiscountRecordInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessApplyRoomDiscountRecordInfo
     */
    protected void flushBusinessApplyRoomDiscountRecordInfo(Map businessApplyRoomDiscountRecordInfo, String statusCd) {
        businessApplyRoomDiscountRecordInfo.put("newBId", businessApplyRoomDiscountRecordInfo.get("b_id"));
        businessApplyRoomDiscountRecordInfo.put("ardrId", businessApplyRoomDiscountRecordInfo.get("ardr_id"));
        businessApplyRoomDiscountRecordInfo.put("ardId", businessApplyRoomDiscountRecordInfo.get("ard_id"));
        businessApplyRoomDiscountRecordInfo.put("createUserId", businessApplyRoomDiscountRecordInfo.get("create_user_id"));
        businessApplyRoomDiscountRecordInfo.put("createUserName", businessApplyRoomDiscountRecordInfo.get("create_user_name"));
        businessApplyRoomDiscountRecordInfo.put("remark", businessApplyRoomDiscountRecordInfo.get("remark"));
        businessApplyRoomDiscountRecordInfo.put("communityId", businessApplyRoomDiscountRecordInfo.get("community_id"));
        businessApplyRoomDiscountRecordInfo.put("isTrue", businessApplyRoomDiscountRecordInfo.get("is_true"));
        businessApplyRoomDiscountRecordInfo.put("state", businessApplyRoomDiscountRecordInfo.get("state"));
        businessApplyRoomDiscountRecordInfo.remove("bId");
        businessApplyRoomDiscountRecordInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessApplyRoomDiscountRecord 验房记录信息
     */
    protected void autoSaveDelBusinessApplyRoomDiscountRecord(Business business, JSONObject businessApplyRoomDiscountRecord) {
//自动插入DEL
        Map info = new HashMap();
        info.put("ardrId", businessApplyRoomDiscountRecord.getString("ardrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentApplyRoomDiscountRecordInfos = getApplyRoomDiscountRecordServiceDaoImpl().getApplyRoomDiscountRecordInfo(info);
        if (currentApplyRoomDiscountRecordInfos == null || currentApplyRoomDiscountRecordInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentApplyRoomDiscountRecordInfo = currentApplyRoomDiscountRecordInfos.get(0);

        currentApplyRoomDiscountRecordInfo.put("bId", business.getbId());

        currentApplyRoomDiscountRecordInfo.put("ardrId", currentApplyRoomDiscountRecordInfo.get("ardr_id"));
        currentApplyRoomDiscountRecordInfo.put("ardId", currentApplyRoomDiscountRecordInfo.get("ard_id"));
        currentApplyRoomDiscountRecordInfo.put("createUserId", currentApplyRoomDiscountRecordInfo.get("create_user_id"));
        currentApplyRoomDiscountRecordInfo.put("createUserName", currentApplyRoomDiscountRecordInfo.get("create_user_name"));
        currentApplyRoomDiscountRecordInfo.put("remark", currentApplyRoomDiscountRecordInfo.get("remark"));
        currentApplyRoomDiscountRecordInfo.put("communityId", currentApplyRoomDiscountRecordInfo.get("community_id"));
        currentApplyRoomDiscountRecordInfo.put("isTrue", currentApplyRoomDiscountRecordInfo.get("is_true"));
        currentApplyRoomDiscountRecordInfo.put("state", currentApplyRoomDiscountRecordInfo.get("state"));
        currentApplyRoomDiscountRecordInfo.put("operate", StatusConstant.OPERATE_DEL);
        getApplyRoomDiscountRecordServiceDaoImpl().saveBusinessApplyRoomDiscountRecordInfo(currentApplyRoomDiscountRecordInfo);
        for (Object key : currentApplyRoomDiscountRecordInfo.keySet()) {
            if (businessApplyRoomDiscountRecord.get(key) == null) {
                businessApplyRoomDiscountRecord.put(key.toString(), currentApplyRoomDiscountRecordInfo.get(key));
            }
        }
    }


}
