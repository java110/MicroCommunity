package com.java110.user.cmd.carInout;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.carInout.ApiCarInoutDataVo;
import com.java110.vo.api.carInout.ApiCarInoutVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "carInout.listCarInouts")
public class ListCarInoutsCmd extends Cmd{

    private static Logger logger = LoggerFactory.getLogger(ListCarInoutsCmd.class);

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CarInoutDto carInoutDto = BeanConvertUtil.covertBean(reqJson, CarInoutDto.class);

        if (reqJson.containsKey("state") && reqJson.getString("state").contains(",")) {
            carInoutDto.setState("");
            String[] states = reqJson.getString("state").split(",");
            carInoutDto.setStates(states);
        }

        int count = carInoutInnerServiceSMOImpl.queryCarInoutsCount(carInoutDto);

        List<ApiCarInoutDataVo> carInouts = null;

        if (count > 0) {
            carInouts = BeanConvertUtil.covertBeanList(carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto), ApiCarInoutDataVo.class);

            refreshCarInout(carInouts, reqJson.getString("communityId"));
        } else {
            carInouts = new ArrayList<>();
        }

        ApiCarInoutVo apiCarInoutVo = new ApiCarInoutVo();

        apiCarInoutVo.setTotal(count);
        apiCarInoutVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarInoutVo.setCarInouts(carInouts);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCarInoutVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 刷新返回数据 加入 停车时间和 应收费用
     *
     * @param carInouts
     */
    private void refreshCarInout(List<ApiCarInoutDataVo> carInouts, String communityId) {

        Date nowTime = new Date();
        Date inTime = null;
        List<String> inoutIds = new ArrayList<>();
        for (ApiCarInoutDataVo apiCarInoutDataVo : carInouts) {
            if ("100600".equals(apiCarInoutDataVo.getState())) {
                inoutIds.add(apiCarInoutDataVo.getInoutId());
            }
        }

        if (inoutIds.size() > 0) {
            FeeDto feeDto = new FeeDto();
            feeDto.setCommunityId(communityId);
            feeDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
            feeDto.setPayerObjIds(inoutIds.toArray(new String[inoutIds.size()]));
            feeDto.setState("2008001");
            feeDto.setFeeFlag("2006012");
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            for (FeeDto tmpFeeDto : feeDtos) {
                for (ApiCarInoutDataVo apiCarInoutDataVo : carInouts) {
                    if (tmpFeeDto.getPayerObjId().equals(apiCarInoutDataVo.getInoutId())) {
                        apiCarInoutDataVo.setInTime(DateUtil.getFormatTimeString(tmpFeeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
                    }
                }
            }
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityId);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            return;
        }
        FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);
        for (ApiCarInoutDataVo apiCarInoutDataVo : carInouts) {
            if ("100500".equals(apiCarInoutDataVo.getState())) {
                continue;
            }
            try {
                inTime = DateUtil.getDateFromString(apiCarInoutDataVo.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
            } catch (Exception e) {
                logger.error("格式化入场时间出错", e);
                continue;
            }
            long diff = nowTime.getTime() - inTime.getTime();
            long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
            long nh = 1000 * 60 * 60;// 一小时的毫秒数
            long nm = 1000 * 60;// 一分钟的毫秒数
            double day = 0;
            double hour = 0;
            double min = 0;
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm;// 计算差多少分钟
            double money = 0.00;
            double newHour = hour;
            if (min > 0) { //一小时超过
                newHour += 1;
            }
            if (newHour <= 2) {
                money = Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount());
            } else {
                BigDecimal lastHour = new BigDecimal(newHour - 2);
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpFeeConfigDto.getSquarePrice()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount()));
                money = squarePrice.multiply(lastHour).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            }
            apiCarInoutDataVo.setMoney(money);
            apiCarInoutDataVo.setInHours(new Double(hour).intValue());
            apiCarInoutDataVo.setInMin(new Double(min).intValue());
        }

    }
}
