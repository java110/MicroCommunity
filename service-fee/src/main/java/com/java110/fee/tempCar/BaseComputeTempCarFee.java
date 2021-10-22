package com.java110.fee.tempCar;


import com.java110.core.factory.TempCarFeeFactory;
import com.java110.dto.fee.TempCarFeeResult;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.intf.fee.IComputeTempCarFee;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public abstract class BaseComputeTempCarFee implements IComputeTempCarFee {
    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;

    @Override
    public TempCarFeeResult computeTempCarFee(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto) throws Exception {
        return computeTempCarFee(carInoutDto, tempCarFeeConfigDto, null);
    }

    @Override
    public TempCarFeeResult computeTempCarFee(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto, List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos) throws Exception {
        if (tempCarFeeConfigAttrDtos == null || tempCarFeeConfigAttrDtos.size() < 1) {
            TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto = new TempCarFeeConfigAttrDto();
            tempCarFeeConfigAttrDto.setConfigId(tempCarFeeConfigDto.getConfigId());
            tempCarFeeConfigAttrDto.setCommunityId(tempCarFeeConfigDto.getCommunityId());

            tempCarFeeConfigAttrDtos = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrs(tempCarFeeConfigAttrDto);
        }
        TempCarFeeResult result = doCompute(carInoutDto, tempCarFeeConfigDto, tempCarFeeConfigAttrDtos);
        //获取停车时间
        long min = TempCarFeeFactory.getTempCarMin(carInoutDto);
        long hours = min / 60; //因为两者都是整数，你得到一个int
        long minutes = min % 60;
        result.setMin(minutes);
        result.setHours(hours);
        return result;
    }

    /**
     * 计算 费用
     *
     * @param carInoutDto
     * @param tempCarFeeConfigDto
     * @param tempCarFeeConfigAttrDtos
     * @return
     */
    public abstract TempCarFeeResult doCompute(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto, List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos);

    /**
     * 判断 用户是支付完成
     *
     * @param carInoutDto
     * @return
     */
    public static long getTempCarMin(CarInoutDto carInoutDto) {

        //支付时间是否超过15分钟
        Date payTime = null;
        try {
            //不是支付完成 状态
            if (CarInoutDto.STATE_PAY.equals(carInoutDto.getState())) {
                payTime = carInoutDto.getCreateTime();
            } else {
                payTime = DateUtil.getDateFromString(carInoutDto.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
            }
            Date nowTime = DateUtil.getCurrentDate();
            //支付完成超过15分钟
            return (nowTime.getTime() - payTime.getTime()) / (60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
