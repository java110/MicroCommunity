package com.java110.fee.cmd.tempCarFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：feePrintPage.deleteFeePrintPage
 * 请求路劲：/app/feePrintPage.DeleteFeePrintPage
 * add by 吴学文 at 2021-09-16 22:26:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "tempCarFee.getTempCarFeeOrder")
public class GetTempCarFeeOrderCmd extends Cmd {
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "carNum", "carNum不能为空");
        if(StringUtil.isEmpty(reqJson.getString("paId"))){
            Assert.hasKeyAndValue(reqJson, "boxId", "boxId不能为空");

            ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
            parkingBoxAreaDto.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_TRUE);
            parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
            List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);
            if(parkingBoxAreaDtos == null || parkingBoxAreaDtos.size()< 1){
                throw new CmdException("未找到停车场");
            }
            reqJson.put("paId",parkingBoxAreaDtos.get(0).getPaId());
        }
        Assert.hasKeyAndValue(reqJson, "paId", "paId不能为空");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
        tempCarPayOrderDto.setPaId(reqJson.getString("paId"));
        tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
        if(reqJson.containsKey("pccIds") && !StringUtil.isEmpty(reqJson.getString("pccIds"))){
            tempCarPayOrderDto.setPccIds(reqJson.getString("pccIds").split(","));
        }
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.getTempCarFeeOrder(tempCarPayOrderDto);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
