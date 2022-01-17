package com.java110.common.listener.machineAuth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.machineAuth.MachineAuthPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.common.dao.IMachineAuthServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除设备权限信息 侦听
 *
 * 处理节点
 * 1、businessMachineAuth:{} 设备权限基本信息节点
 * 2、businessMachineAuthAttr:[{}] 设备权限属性信息节点
 * 3、businessMachineAuthPhoto:[{}] 设备权限照片信息节点
 * 4、businessMachineAuthCerdentials:[{}] 设备权限证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteMachineAuthInfoListener")
@Transactional
public class DeleteMachineAuthInfoListener extends AbstractMachineAuthBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteMachineAuthInfoListener.class);
    @Autowired
    IMachineAuthServiceDao machineAuthServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_AUTH;
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

            //处理 businessMachineAuth 节点
            if(data.containsKey(MachineAuthPo.class.getSimpleName())){
                Object _obj = data.get(MachineAuthPo.class.getSimpleName());
                JSONArray businessMachineAuths = null;
                if(_obj instanceof JSONObject){
                    businessMachineAuths = new JSONArray();
                    businessMachineAuths.add(_obj);
                }else {
                    businessMachineAuths = (JSONArray)_obj;
                }
                //JSONObject businessMachineAuth = data.getJSONObject(MachineAuthPo.class.getSimpleName());
                for (int _machineAuthIndex = 0; _machineAuthIndex < businessMachineAuths.size();_machineAuthIndex++) {
                    JSONObject businessMachineAuth = businessMachineAuths.getJSONObject(_machineAuthIndex);
                    doBusinessMachineAuth(business, businessMachineAuth);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("authId", businessMachineAuth.getString("authId"));
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

        //设备权限信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //设备权限信息
        List<Map> businessMachineAuthInfos = machineAuthServiceDaoImpl.getBusinessMachineAuthInfo(info);
        if( businessMachineAuthInfos != null && businessMachineAuthInfos.size() >0) {
            for (int _machineAuthIndex = 0; _machineAuthIndex < businessMachineAuthInfos.size();_machineAuthIndex++) {
                Map businessMachineAuthInfo = businessMachineAuthInfos.get(_machineAuthIndex);
                flushBusinessMachineAuthInfo(businessMachineAuthInfo,StatusConstant.STATUS_CD_INVALID);
                machineAuthServiceDaoImpl.updateMachineAuthInfoInstance(businessMachineAuthInfo);
                dataFlowContext.addParamOut("authId",businessMachineAuthInfo.get("auth_id"));
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
        //设备权限信息
        List<Map> machineAuthInfo = machineAuthServiceDaoImpl.getMachineAuthInfo(info);
        if(machineAuthInfo != null && machineAuthInfo.size() > 0){

            //设备权限信息
            List<Map> businessMachineAuthInfos = machineAuthServiceDaoImpl.getBusinessMachineAuthInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessMachineAuthInfos == null ||  businessMachineAuthInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（machineAuth），程序内部异常,请检查！ "+delInfo);
            }
            for (int _machineAuthIndex = 0; _machineAuthIndex < businessMachineAuthInfos.size();_machineAuthIndex++) {
                Map businessMachineAuthInfo = businessMachineAuthInfos.get(_machineAuthIndex);
                flushBusinessMachineAuthInfo(businessMachineAuthInfo,StatusConstant.STATUS_CD_VALID);
                machineAuthServiceDaoImpl.updateMachineAuthInfoInstance(businessMachineAuthInfo);
            }
        }
    }



    /**
     * 处理 businessMachineAuth 节点
     * @param business 总的数据节点
     * @param businessMachineAuth 设备权限节点
     */
    private void doBusinessMachineAuth(Business business,JSONObject businessMachineAuth){

        Assert.jsonObjectHaveKey(businessMachineAuth,"authId","businessMachineAuth 节点下没有包含 authId 节点");

        if(businessMachineAuth.getString("authId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"authId 错误，不能自动生成（必须已经存在的authId）"+businessMachineAuth);
        }
        //自动插入DEL
        autoSaveDelBusinessMachineAuth(business,businessMachineAuth);
    }
    @Override
    public IMachineAuthServiceDao getMachineAuthServiceDaoImpl() {
        return machineAuthServiceDaoImpl;
    }

    public void setMachineAuthServiceDaoImpl(IMachineAuthServiceDao machineAuthServiceDaoImpl) {
        this.machineAuthServiceDaoImpl = machineAuthServiceDaoImpl;
    }
}
