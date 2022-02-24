package com.java110.fee.bmo.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.fee.bmo.IApiBaseBMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;

import java.text.ParseException;
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
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void updateFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyFee(JSONObject paramInJson, DataFlowContext dataFlowContext);


    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyOweFee(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext, FeeReceiptDetailPo feeReceiptDetailPo, FeeReceiptPo feeReceiptPo);

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addOweFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext,
                                      List<FeeReceiptDetailPo> feeReceiptDetailPos,
                                      List<FeeReceiptPo> feeReceiptPos);

    /**
     * 添加费用明细信息
     *
     * @param payFeeDetailPo  费用明细
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addSimpleFeeDetail(PayFeeDetailPo payFeeDetailPo, DataFlowContext dataFlowContext);

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeePreDetail(JSONObject paramInJson);

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyPreFee(JSONObject paramInJson);

    public JSONObject modifyTempCarInout(JSONObject reqJson, DataFlowContext context);

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeTempDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyTempFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param context 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFee(OwnerCarDto ownerCarDto, JSONObject paramInJson, ICmdDataFlowContext context);


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public FeeAttrPo addFeeAttr(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String specCd, String value);


    /**
     * 添加费用
     *
     * @param payFeePo        接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addSimpleFee(PayFeePo payFeePo, DataFlowContext dataFlowContext);

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addRoomFee(RoomDto roomDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) ;   /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addContractFee(ContractDto contractDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext);

    /**
     * 添加费用项信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
