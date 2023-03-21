package com.java110.job.adapt.payment.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 退费审核同步收据
 *
 * @author fqz
 * @date 2023-03-09 16:28
 */
@Component(value = "updateReturnPayFeeAdapt")
public class UpdateReturnPayFeeAdapt extends DatabusAdaptImpl {

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(UpdateReturnPayFeeAdapt.class);

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        if (data != null) {
            logger.debug("请求日志:{}", data);
        }
        JSONArray businessPayFeeDetails = null;
        if (data == null) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setbId(business.getbId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未查询到缴费记录");
            businessPayFeeDetails = JSONArray.parseArray(JSONArray.toJSONString(feeDetailDtos, SerializerFeature.WriteDateUseDateFormat));
        } else if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFeeDetails = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFeeDetails = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(data);
            }
        }
        if (businessPayFeeDetails == null) {
            return;
        }
        for (int bPayFeeDetailIndex = 0; bPayFeeDetailIndex < businessPayFeeDetails.size(); bPayFeeDetailIndex++) {
            JSONObject businessPayFeeDetail = businessPayFeeDetails.getJSONObject(bPayFeeDetailIndex);
            //新增收据和收据明细
            doFeeReceipt(business, businessPayFeeDetail);
        }
    }

    //新增收据和收据明细
    private void doFeeReceipt(Business business, JSONObject paramInJson) {
        try {
            //查询缴费明细
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailPo.class);
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "未查询到费用信息");
            feeDto = feeDtos.get(0);
            //查询业主信息
            OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
            //添加收据
            FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
            feeReceiptPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
            feeReceiptPo.setCommunityId(paramInJson.getString("communityId"));
            feeReceiptPo.setObjType(paramInJson.getString("payerObjType")); //收据对象 3333 房屋 6666 车位车辆
            feeReceiptPo.setObjId(paramInJson.getString("payerObjId")); //对象ID
            feeReceiptPo.setObjName(paramInJson.getString("payerObjName")); //对象名称
            feeReceiptPo.setAmount(paramInJson.getString("receivedAmount")); //退费总金额
            feeReceiptPo.setRemark("退费收据");
            feeReceiptPo.setPayObjId(ownerDto.getOwnerId()); //付费人id
            feeReceiptPo.setPayObjName(ownerDto.getName()); //付费人姓名
            int i = feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);
            if (i < 1) {
                throw new CmdException("添加收据失败");
            }
            //添加收据详情
            FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
            feeReceiptDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            feeReceiptDetailPo.setReceiptId(feeReceiptPo.getReceiptId()); //收据id
            feeReceiptDetailPo.setFeeId(paramInJson.getString("feeId")); //费用id
            feeReceiptDetailPo.setFeeName(feeDtos.get(0).getFeeName());
            String roomArea = "";
            if (!StringUtil.isEmpty(feeDtos.get(0).getPayerObjType()) && feeDtos.get(0).getPayerObjType().equals("3333")) { //房屋
                RoomDto roomDto = new RoomDto();
                roomDto.setRoomId(feeDtos.get(0).getPayerObjId());
                //查询房屋
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                Assert.listOnlyOne(roomDtos, "查询房屋错误！");
                roomArea = roomDtos.get(0).getRoomArea();
            }
            feeReceiptDetailPo.setArea(roomArea); //面积/用量
            feeReceiptDetailPo.setStartTime(paramInJson.getString("startTime"));
            feeReceiptDetailPo.setEndTime(paramInJson.getString("endTime"));
            feeReceiptDetailPo.setAmount(feeReceiptPo.getAmount());
            feeReceiptDetailPo.setCycle(paramInJson.getString("cycles"));
            feeReceiptDetailPo.setCommunityId(paramInJson.getString("communityId"));
            int flag = feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
            if (flag < 1) {
                throw new CmdException("添加收据详情失败");
            }
        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }
}
