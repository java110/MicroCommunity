package com.java110.fee.listener.payFeeDetailDiscount;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.fee.dao.IPayFeeDetailDiscountServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴费优惠 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractPayFeeDetailDiscountBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractPayFeeDetailDiscountBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPayFeeDetailDiscountServiceDao getPayFeeDetailDiscountServiceDaoImpl();

    /**
     * 刷新 businessPayFeeDetailDiscountInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPayFeeDetailDiscountInfo
     */
    protected void flushBusinessPayFeeDetailDiscountInfo(Map businessPayFeeDetailDiscountInfo, String statusCd) {
        businessPayFeeDetailDiscountInfo.put("newBId", businessPayFeeDetailDiscountInfo.get("b_id"));
        businessPayFeeDetailDiscountInfo.put("operate", businessPayFeeDetailDiscountInfo.get("operate"));
        businessPayFeeDetailDiscountInfo.put("createTime", businessPayFeeDetailDiscountInfo.get("create_time"));
        businessPayFeeDetailDiscountInfo.put("detailDiscountId", businessPayFeeDetailDiscountInfo.get("detail_discount_id"));
        businessPayFeeDetailDiscountInfo.put("discountPrice", businessPayFeeDetailDiscountInfo.get("discount_price"));
        businessPayFeeDetailDiscountInfo.put("detailId", businessPayFeeDetailDiscountInfo.get("detail_id"));
        businessPayFeeDetailDiscountInfo.put("remark", businessPayFeeDetailDiscountInfo.get("remark"));
        businessPayFeeDetailDiscountInfo.put("communityId", businessPayFeeDetailDiscountInfo.get("community_id"));
        businessPayFeeDetailDiscountInfo.put("discountId", businessPayFeeDetailDiscountInfo.get("discount_id"));
        businessPayFeeDetailDiscountInfo.put("feeId", businessPayFeeDetailDiscountInfo.get("fee_id"));
        businessPayFeeDetailDiscountInfo.remove("bId");
        businessPayFeeDetailDiscountInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPayFeeDetailDiscount 缴费优惠信息
     */
    protected void autoSaveDelBusinessPayFeeDetailDiscount(Business business, JSONObject businessPayFeeDetailDiscount) {
//自动插入DEL
        Map info = new HashMap();
        info.put("detailDiscountId", businessPayFeeDetailDiscount.getString("detailDiscountId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentPayFeeDetailDiscountInfos = getPayFeeDetailDiscountServiceDaoImpl().getPayFeeDetailDiscountInfo(info);
        if (currentPayFeeDetailDiscountInfos == null || currentPayFeeDetailDiscountInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPayFeeDetailDiscountInfo = currentPayFeeDetailDiscountInfos.get(0);

        currentPayFeeDetailDiscountInfo.put("bId", business.getbId());

        currentPayFeeDetailDiscountInfo.put("operate", currentPayFeeDetailDiscountInfo.get("operate"));
        currentPayFeeDetailDiscountInfo.put("createTime", currentPayFeeDetailDiscountInfo.get("create_time"));
        currentPayFeeDetailDiscountInfo.put("detailDiscountId", currentPayFeeDetailDiscountInfo.get("detail_discount_id"));
        currentPayFeeDetailDiscountInfo.put("discountPrice", currentPayFeeDetailDiscountInfo.get("discount_price"));
        currentPayFeeDetailDiscountInfo.put("detailId", currentPayFeeDetailDiscountInfo.get("detail_id"));
        currentPayFeeDetailDiscountInfo.put("remark", currentPayFeeDetailDiscountInfo.get("remark"));
        currentPayFeeDetailDiscountInfo.put("communityId", currentPayFeeDetailDiscountInfo.get("community_id"));
        currentPayFeeDetailDiscountInfo.put("discountId", currentPayFeeDetailDiscountInfo.get("discount_id"));
        currentPayFeeDetailDiscountInfo.put("feeId", currentPayFeeDetailDiscountInfo.get("fee_id"));


        currentPayFeeDetailDiscountInfo.put("operate", StatusConstant.OPERATE_DEL);
        getPayFeeDetailDiscountServiceDaoImpl().saveBusinessPayFeeDetailDiscountInfo(currentPayFeeDetailDiscountInfo);
        for (Object key : currentPayFeeDetailDiscountInfo.keySet()) {
            if (businessPayFeeDetailDiscount.get(key) == null) {
                businessPayFeeDetailDiscount.put(key.toString(), currentPayFeeDetailDiscountInfo.get(key));
            }
        }
    }


}
