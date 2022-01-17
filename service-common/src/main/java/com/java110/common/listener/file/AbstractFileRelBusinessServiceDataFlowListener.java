package com.java110.common.listener.file;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IFileRelServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件存放 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractFileRelBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractFileRelBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IFileRelServiceDao getFileRelServiceDaoImpl();

    /**
     * 刷新 businessFileRelInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessFileRelInfo
     */
    protected void flushBusinessFileRelInfo(Map businessFileRelInfo, String statusCd) {
        businessFileRelInfo.put("newBId", businessFileRelInfo.get("b_id"));
        businessFileRelInfo.put("relTypeCd", businessFileRelInfo.get("rel_type_cd"));
        businessFileRelInfo.put("saveWay", businessFileRelInfo.get("save_way"));
        businessFileRelInfo.put("operate", businessFileRelInfo.get("operate"));
        businessFileRelInfo.put("fileRelId", businessFileRelInfo.get("file_rel_id"));
        businessFileRelInfo.put("fileRealName", businessFileRelInfo.get("file_real_name"));
        businessFileRelInfo.put("objId", businessFileRelInfo.get("obj_id"));
        businessFileRelInfo.put("fileSaveName", businessFileRelInfo.get("file_save_name"));
        businessFileRelInfo.remove("bId");
        businessFileRelInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessFileRel 文件存放信息
     */
    protected void autoSaveDelBusinessFileRel(Business business, JSONObject businessFileRel) {
//自动插入DEL
        Map info = new HashMap();
        info.put("fileRelId", businessFileRel.getString("fileRelId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentFileRelInfos = getFileRelServiceDaoImpl().getFileRelInfo(info);
        if (currentFileRelInfos == null || currentFileRelInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentFileRelInfo = currentFileRelInfos.get(0);

        currentFileRelInfo.put("bId", business.getbId());

        currentFileRelInfo.put("relTypeCd", currentFileRelInfo.get("rel_type_cd"));
        currentFileRelInfo.put("saveWay", currentFileRelInfo.get("save_way"));
        currentFileRelInfo.put("operate", currentFileRelInfo.get("operate"));
        currentFileRelInfo.put("fileRelId", currentFileRelInfo.get("file_rel_id"));
        currentFileRelInfo.put("fileRealName", currentFileRelInfo.get("file_real_name"));
        currentFileRelInfo.put("objId", currentFileRelInfo.get("obj_id"));
        currentFileRelInfo.put("fileSaveName", currentFileRelInfo.get("file_save_name"));


        currentFileRelInfo.put("operate", StatusConstant.OPERATE_DEL);
        getFileRelServiceDaoImpl().saveBusinessFileRelInfo(currentFileRelInfo);
        for(Object key : currentFileRelInfo.keySet()) {
            if(businessFileRel.get(key) == null) {
                businessFileRel.put(key.toString(), currentFileRelInfo.get(key));
            }
        }
    }


}
