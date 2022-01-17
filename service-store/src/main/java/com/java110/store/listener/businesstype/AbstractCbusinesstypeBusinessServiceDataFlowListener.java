package com.java110.store.listener.businesstype;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.ICbusinesstypeServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cbusinesstype 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCbusinesstypeBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractCbusinesstypeBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICbusinesstypeServiceDao getCbusinesstypeServiceDaoImpl();

    /**
     * 刷新 businessCbusinesstypeInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCbusinesstypeInfo
     */
    protected void flushBusinessCbusinesstypeInfo(Map businessCbusinesstypeInfo, String statusCd) {
        businessCbusinesstypeInfo.put("newBId", businessCbusinesstypeInfo.get("b_id"));
        businessCbusinesstypeInfo.put("businessTypeCd", businessCbusinesstypeInfo.get("business_type_cd"));
        businessCbusinesstypeInfo.put("name", businessCbusinesstypeInfo.get("name"));
        businessCbusinesstypeInfo.put("description", businessCbusinesstypeInfo.get("description"));
        businessCbusinesstypeInfo.put("id", businessCbusinesstypeInfo.get("id"));
        businessCbusinesstypeInfo.remove("bId");
        businessCbusinesstypeInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCbusinesstype cbusinesstype信息
     */
    protected void autoSaveDelBusinessCbusinesstype(Business business, JSONObject businessCbusinesstype) {
//自动插入DEL
        Map info = new HashMap();
        info.put("id", businessCbusinesstype.getString("id"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCbusinesstypeInfos = getCbusinesstypeServiceDaoImpl().getCbusinesstypeInfo(info);
        if (currentCbusinesstypeInfos == null || currentCbusinesstypeInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCbusinesstypeInfo = currentCbusinesstypeInfos.get(0);

        currentCbusinesstypeInfo.put("bId", business.getbId());

        currentCbusinesstypeInfo.put("businessTypeCd", currentCbusinesstypeInfo.get("business_type_cd"));
        currentCbusinesstypeInfo.put("name", currentCbusinesstypeInfo.get("name"));
        currentCbusinesstypeInfo.put("description", currentCbusinesstypeInfo.get("description"));
        currentCbusinesstypeInfo.put("id", currentCbusinesstypeInfo.get("id"));


        currentCbusinesstypeInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCbusinesstypeServiceDaoImpl().saveBusinessCbusinesstypeInfo(currentCbusinesstypeInfo);

        for(Object key : currentCbusinesstypeInfo.keySet()) {
            if(businessCbusinesstype.get(key) == null) {
                businessCbusinesstype.put(key.toString(), currentCbusinesstypeInfo.get(key));
            }
        }

    }


}
