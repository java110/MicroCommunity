package com.java110.store.listener.businesstype;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.store.dao.ICbusinesstypeServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除cbusinesstype信息 侦听
 * <p>
 * 处理节点
 * 1、businessCbusinesstype:{} cbusinesstype基本信息节点
 * 2、businessCbusinesstypeAttr:[{}] cbusinesstype属性信息节点
 * 3、businessCbusinesstypePhoto:[{}] cbusinesstype照片信息节点
 * 4、businessCbusinesstypeCerdentials:[{}] cbusinesstype证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCbusinesstypeInfoListener")
@Transactional
public class DeleteCbusinesstypeInfoListener extends AbstractCbusinesstypeBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCbusinesstypeInfoListener.class);
    @Autowired
    ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_BUSINESSTYPE_INFO;
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

        //处理 businessCbusinesstype 节点
        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_DELETE_BUSINESSTYPE_INFO)) {
            Object _obj = data.get(BusinessTypeConstant.BUSINESS_TYPE_DELETE_BUSINESSTYPE_INFO);
            JSONArray businessCbusinesstypes = null;
            if (_obj instanceof JSONObject) {
                businessCbusinesstypes = new JSONArray();
                businessCbusinesstypes.add(_obj);
            } else {
                businessCbusinesstypes = (JSONArray) _obj;
            }
            //JSONObject businessCbusinesstype = data.getJSONObject("businessCbusinesstype");
            for (int _cbusinesstypeIndex = 0; _cbusinesstypeIndex < businessCbusinesstypes.size(); _cbusinesstypeIndex++) {
                JSONObject businessCbusinesstype = businessCbusinesstypes.getJSONObject(_cbusinesstypeIndex);
                doBusinessCbusinesstype(business, businessCbusinesstype);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("id", businessCbusinesstype.getString("id"));
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

        //cbusinesstype信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //cbusinesstype信息
        List<Map> businessCbusinesstypeInfos = cbusinesstypeServiceDaoImpl.getBusinessCbusinesstypeInfo(info);
        if (businessCbusinesstypeInfos != null && businessCbusinesstypeInfos.size() > 0) {
            for (int _cbusinesstypeIndex = 0; _cbusinesstypeIndex < businessCbusinesstypeInfos.size(); _cbusinesstypeIndex++) {
                Map businessCbusinesstypeInfo = businessCbusinesstypeInfos.get(_cbusinesstypeIndex);
                flushBusinessCbusinesstypeInfo(businessCbusinesstypeInfo, StatusConstant.STATUS_CD_INVALID);
                cbusinesstypeServiceDaoImpl.updateCbusinesstypeInfoInstance(businessCbusinesstypeInfo);
                dataFlowContext.addParamOut("id", businessCbusinesstypeInfo.get("id"));
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
        //cbusinesstype信息
        List<Map> cbusinesstypeInfo = cbusinesstypeServiceDaoImpl.getCbusinesstypeInfo(info);
        if (cbusinesstypeInfo != null && cbusinesstypeInfo.size() > 0) {

            //cbusinesstype信息
            List<Map> businessCbusinesstypeInfos = cbusinesstypeServiceDaoImpl.getBusinessCbusinesstypeInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCbusinesstypeInfos == null || businessCbusinesstypeInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（cbusinesstype），程序内部异常,请检查！ " + delInfo);
            }
            for (int _cbusinesstypeIndex = 0; _cbusinesstypeIndex < businessCbusinesstypeInfos.size(); _cbusinesstypeIndex++) {
                Map businessCbusinesstypeInfo = businessCbusinesstypeInfos.get(_cbusinesstypeIndex);
                flushBusinessCbusinesstypeInfo(businessCbusinesstypeInfo, StatusConstant.STATUS_CD_VALID);
                cbusinesstypeServiceDaoImpl.updateCbusinesstypeInfoInstance(businessCbusinesstypeInfo);
            }
        }
    }


    /**
     * 处理 businessCbusinesstype 节点
     *
     * @param business              总的数据节点
     * @param businessCbusinesstype cbusinesstype节点
     */
    private void doBusinessCbusinesstype(Business business, JSONObject businessCbusinesstype) {

        Assert.jsonObjectHaveKey(businessCbusinesstype, "id", "businessCbusinesstype 节点下没有包含 id 节点");

        if (businessCbusinesstype.getString("id").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "id 错误，不能自动生成（必须已经存在的id）" + businessCbusinesstype);
        }
        //自动插入DEL
        autoSaveDelBusinessCbusinesstype(business, businessCbusinesstype);
    }

    public ICbusinesstypeServiceDao getCbusinesstypeServiceDaoImpl() {
        return cbusinesstypeServiceDaoImpl;
    }

    public void setCbusinesstypeServiceDaoImpl(ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl) {
        this.cbusinesstypeServiceDaoImpl = cbusinesstypeServiceDaoImpl;
    }
}
