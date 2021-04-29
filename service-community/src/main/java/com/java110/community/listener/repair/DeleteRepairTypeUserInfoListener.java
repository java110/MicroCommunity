package com.java110.community.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRepairTypeUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.repair.RepairTypeUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除报修设置信息 侦听
 * <p>
 * 处理节点
 * 1、businessRepairTypeUser:{} 报修设置基本信息节点
 * 2、businessRepairTypeUserAttr:[{}] 报修设置属性信息节点
 * 3、businessRepairTypeUserPhoto:[{}] 报修设置照片信息节点
 * 4、businessRepairTypeUserCerdentials:[{}] 报修设置证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteRepairTypeUserInfoListener")
@Transactional
public class DeleteRepairTypeUserInfoListener extends AbstractRepairTypeUserBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteRepairTypeUserInfoListener.class);
    @Autowired
    IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_TYPE_USER;
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

        //处理 businessRepairTypeUser 节点
        if (data.containsKey(RepairTypeUserPo.class.getSimpleName())) {
            Object _obj = data.get(RepairTypeUserPo.class.getSimpleName());
            JSONArray businessRepairTypeUsers = null;
            if (_obj instanceof JSONObject) {
                businessRepairTypeUsers = new JSONArray();
                businessRepairTypeUsers.add(_obj);
            } else {
                businessRepairTypeUsers = (JSONArray) _obj;
            }
            //JSONObject businessRepairTypeUser = data.getJSONObject(RepairTypeUserPo.class.getSimpleName());
            for (int _repairTypeUserIndex = 0; _repairTypeUserIndex < businessRepairTypeUsers.size(); _repairTypeUserIndex++) {
                JSONObject businessRepairTypeUser = businessRepairTypeUsers.getJSONObject(_repairTypeUserIndex);
                doBusinessRepairTypeUser(business, businessRepairTypeUser);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("typeUserId", businessRepairTypeUser.getString("typeUserId"));
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

        //报修设置信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //报修设置信息
        List<Map> businessRepairTypeUserInfos = repairTypeUserServiceDaoImpl.getBusinessRepairTypeUserInfo(info);
        if (businessRepairTypeUserInfos != null && businessRepairTypeUserInfos.size() > 0) {
            for (int _repairTypeUserIndex = 0; _repairTypeUserIndex < businessRepairTypeUserInfos.size(); _repairTypeUserIndex++) {
                Map businessRepairTypeUserInfo = businessRepairTypeUserInfos.get(_repairTypeUserIndex);
                flushBusinessRepairTypeUserInfo(businessRepairTypeUserInfo, StatusConstant.STATUS_CD_INVALID);
                repairTypeUserServiceDaoImpl.updateRepairTypeUserInfoInstance(businessRepairTypeUserInfo);
                dataFlowContext.addParamOut("typeUserId", businessRepairTypeUserInfo.get("type_user_id"));
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
        //报修设置信息
        List<Map> repairTypeUserInfo = repairTypeUserServiceDaoImpl.getRepairTypeUserInfo(info);
        if (repairTypeUserInfo != null && repairTypeUserInfo.size() > 0) {

            //报修设置信息
            List<Map> businessRepairTypeUserInfos = repairTypeUserServiceDaoImpl.getBusinessRepairTypeUserInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessRepairTypeUserInfos == null || businessRepairTypeUserInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（repairTypeUser），程序内部异常,请检查！ " + delInfo);
            }
            for (int _repairTypeUserIndex = 0; _repairTypeUserIndex < businessRepairTypeUserInfos.size(); _repairTypeUserIndex++) {
                Map businessRepairTypeUserInfo = businessRepairTypeUserInfos.get(_repairTypeUserIndex);
                flushBusinessRepairTypeUserInfo(businessRepairTypeUserInfo, StatusConstant.STATUS_CD_VALID);
                repairTypeUserServiceDaoImpl.updateRepairTypeUserInfoInstance(businessRepairTypeUserInfo);
            }
        }
    }


    /**
     * 处理 businessRepairTypeUser 节点
     *
     * @param business               总的数据节点
     * @param businessRepairTypeUser 报修设置节点
     */
    private void doBusinessRepairTypeUser(Business business, JSONObject businessRepairTypeUser) {

        Assert.jsonObjectHaveKey(businessRepairTypeUser, "typeUserId", "businessRepairTypeUser 节点下没有包含 typeUserId 节点");

        if (businessRepairTypeUser.getString("typeUserId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "typeUserId 错误，不能自动生成（必须已经存在的typeUserId）" + businessRepairTypeUser);
        }
        //自动插入DEL
        autoSaveDelBusinessRepairTypeUser(business, businessRepairTypeUser);
    }

    @Override
    public IRepairTypeUserServiceDao getRepairTypeUserServiceDaoImpl() {
        return repairTypeUserServiceDaoImpl;
    }

    public void setRepairTypeUserServiceDaoImpl(IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl) {
        this.repairTypeUserServiceDaoImpl = repairTypeUserServiceDaoImpl;
    }
}
