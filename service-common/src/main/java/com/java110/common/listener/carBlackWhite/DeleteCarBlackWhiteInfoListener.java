package com.java110.common.listener.carBlackWhite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarBlackWhiteServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.car.CarBlackWhitePo;
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
 * 删除黑白名单信息 侦听
 * <p>
 * 处理节点
 * 1、businessCarBlackWhite:{} 黑白名单基本信息节点
 * 2、businessCarBlackWhiteAttr:[{}] 黑白名单属性信息节点
 * 3、businessCarBlackWhitePhoto:[{}] 黑白名单照片信息节点
 * 4、businessCarBlackWhiteCerdentials:[{}] 黑白名单证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCarBlackWhiteInfoListener")
@Transactional
public class DeleteCarBlackWhiteInfoListener extends AbstractCarBlackWhiteBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCarBlackWhiteInfoListener.class);
    @Autowired
    ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_CAR_BLACK_WHITE;
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


        //处理 businessCarBlackWhite 节点
        if (data.containsKey(CarBlackWhitePo.class.getSimpleName())) {
            Object _obj = data.get(CarBlackWhitePo.class.getSimpleName());
            JSONArray businessCarBlackWhites = null;
            if (_obj instanceof JSONObject) {
                businessCarBlackWhites = new JSONArray();
                businessCarBlackWhites.add(_obj);
            } else {
                businessCarBlackWhites = (JSONArray) _obj;
            }
            //JSONObject businessCarBlackWhite = data.getJSONObject("businessCarBlackWhite");
            for (int _carBlackWhiteIndex = 0; _carBlackWhiteIndex < businessCarBlackWhites.size(); _carBlackWhiteIndex++) {
                JSONObject businessCarBlackWhite = businessCarBlackWhites.getJSONObject(_carBlackWhiteIndex);
                doBusinessCarBlackWhite(business, businessCarBlackWhite);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("bwId", businessCarBlackWhite.getString("bwId"));
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

        //黑白名单信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //黑白名单信息
        List<Map> businessCarBlackWhiteInfos = carBlackWhiteServiceDaoImpl.getBusinessCarBlackWhiteInfo(info);
        if (businessCarBlackWhiteInfos != null && businessCarBlackWhiteInfos.size() > 0) {
            for (int _carBlackWhiteIndex = 0; _carBlackWhiteIndex < businessCarBlackWhiteInfos.size(); _carBlackWhiteIndex++) {
                Map businessCarBlackWhiteInfo = businessCarBlackWhiteInfos.get(_carBlackWhiteIndex);
                flushBusinessCarBlackWhiteInfo(businessCarBlackWhiteInfo, StatusConstant.STATUS_CD_INVALID);
                carBlackWhiteServiceDaoImpl.updateCarBlackWhiteInfoInstance(businessCarBlackWhiteInfo);
                dataFlowContext.addParamOut("bwId", businessCarBlackWhiteInfo.get("bw_id"));
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
        //黑白名单信息
        List<Map> carBlackWhiteInfo = carBlackWhiteServiceDaoImpl.getCarBlackWhiteInfo(info);
        if (carBlackWhiteInfo != null && carBlackWhiteInfo.size() > 0) {

            //黑白名单信息
            List<Map> businessCarBlackWhiteInfos = carBlackWhiteServiceDaoImpl.getBusinessCarBlackWhiteInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCarBlackWhiteInfos == null || businessCarBlackWhiteInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（carBlackWhite），程序内部异常,请检查！ " + delInfo);
            }
            for (int _carBlackWhiteIndex = 0; _carBlackWhiteIndex < businessCarBlackWhiteInfos.size(); _carBlackWhiteIndex++) {
                Map businessCarBlackWhiteInfo = businessCarBlackWhiteInfos.get(_carBlackWhiteIndex);
                flushBusinessCarBlackWhiteInfo(businessCarBlackWhiteInfo, StatusConstant.STATUS_CD_VALID);
                carBlackWhiteServiceDaoImpl.updateCarBlackWhiteInfoInstance(businessCarBlackWhiteInfo);
            }
        }
    }


    /**
     * 处理 businessCarBlackWhite 节点
     *
     * @param business              总的数据节点
     * @param businessCarBlackWhite 黑白名单节点
     */
    private void doBusinessCarBlackWhite(Business business, JSONObject businessCarBlackWhite) {

        Assert.jsonObjectHaveKey(businessCarBlackWhite, "bwId", "businessCarBlackWhite 节点下没有包含 bwId 节点");

        if (businessCarBlackWhite.getString("bwId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "bwId 错误，不能自动生成（必须已经存在的bwId）" + businessCarBlackWhite);
        }
        //自动插入DEL
        autoSaveDelBusinessCarBlackWhite(business, businessCarBlackWhite);
    }

    public ICarBlackWhiteServiceDao getCarBlackWhiteServiceDaoImpl() {
        return carBlackWhiteServiceDaoImpl;
    }

    public void setCarBlackWhiteServiceDaoImpl(ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl) {
        this.carBlackWhiteServiceDaoImpl = carBlackWhiteServiceDaoImpl;
    }
}
