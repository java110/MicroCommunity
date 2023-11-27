package com.java110.fee.bmo.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.room.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.fee.bmo.IApiBaseBMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.FeeReceiptDetailPo;

import java.util.List;

/**
 * @ClassName IFeeBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:22
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IFeeBMO extends IApiBaseBMO {



    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    JSONObject addFeePreDetail(JSONObject paramInJson);

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    JSONObject modifyPreFee(JSONObject paramInJson);



    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param context 数据上下文
     * @return 订单服务能够接受的报文
     */
    JSONObject addFee(OwnerCarDto ownerCarDto, JSONObject paramInJson, ICmdDataFlowContext context);


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    FeeAttrPo addFeeAttr(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String specCd, String value);



    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject addRoomFee(RoomDto roomDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) ;   /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    JSONObject addContractFee(ContractDto contractDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext);

}
