package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.*;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.fee.bmo.IImportRoomFee;
import com.java110.fee.bmo.IPayOweFee;
import com.java110.fee.listener.fee.UpdateFeeInfoListener;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.constant.FeeStateConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 欠费缴费实现类
 */
@Service
public class ImportRoomFeeImpl implements IImportRoomFee {

    private static Logger logger = LoggerFactory.getLogger(UpdateFeeInfoListener.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    /**
     * 欠费缴费
     *
     * @param reqJson 缴费报文 {"communityId":"7020181217000001","fees":[{"feeId":"902020073149140091","feePrice":90},{"feeId":"902020072844020741","feePrice":1500}]}
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importFee(JSONObject reqJson) {

        //小区ID
        String communityId = reqJson.getString("communityId");


        JSONArray importRoomFees = reqJson.getJSONArray("importRoomFees");

        JSONObject feeObj = null;

        for (int feeIndex = 0; feeIndex < importRoomFees.size(); feeIndex++) {
            feeObj = importRoomFees.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(feeObj, "feeId", "未包含费用项ID");
            Assert.hasKeyAndValue(feeObj, "feePrice", "未包含缴费金额");

            feeObj.put("communityId", communityId);

        }
        return ResultVo.success();
    }



}
