package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.ICarInoutPaymentV1InnerServiceSMO;
import com.java110.intf.common.ICarInoutV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.CarInoutPo;
import com.java110.po.carInoutPayment.CarInoutPaymentPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：车辆支付通知
 * 服务编码：machineTranslate.machineUploadCarLog
 * 请求路劲：/app/machineTranslate.machineUploadCarLog
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machineTranslate.machineUploadCarPaymentCmd")
public class MachineUploadCarPaymentCmd extends Cmd {


    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private ICarInoutPaymentV1InnerServiceSMO carInoutPaymentV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    /**
     * data.put("carNum", carInoutDto.getCarNum());
     * data.put("machineCode", carInoutDto.getMachineCode());
     * data.put("communityId", communityDtos.get(0).getExtCommunityId());
     * data.put("payCharge", carInoutDto.getPayCharge());
     * data.put("realCharge", carInoutDto.getRealCharge());
     * data.put("payType", carInoutDto.getPayType());
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 请求报文数据
     * @param reqJson
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "payCharge", "必填，请填写应收费用");
        Assert.hasKeyAndValue(reqJson, "realCharge", "必填，请填写实收费用");
        Assert.hasKeyAndValue(reqJson, "payType", "必填，请填写支付类型");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //查询设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));
        machineDto.setMachineTypeCd(MachineDto.MACHINE_TYPE_CAR);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCommunityId(reqJson.getString("communityId"));
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setPaId(machineDto.getLocationObjId());
        carInoutDto.setStates(new String[]{
                CarInoutDto.STATE_IN,
                CarInoutDto.STATE_REPAY
        });
        List<CarInoutDto> carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(carInoutDto);

        if (carInoutDtos == null || carInoutDtos.size() < 1) {
            //可能车辆异常情况 没有 进场 补充进场记录
            throw new CmdException("没有待支付记录");
        }

        //写支付记录
        CarInoutPaymentPo carInoutPaymentPo = new CarInoutPaymentPo();
        carInoutPaymentPo.setPaymentId(GenerateCodeFactory.getGeneratorId("10"));
        carInoutPaymentPo.setCommunityId(reqJson.getString("communityId"));
        carInoutPaymentPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutPaymentPo.setPaId(carInoutDtos.get(0).getPaId());
        carInoutPaymentPo.setPayCharge(reqJson.getString("payCharge"));
        carInoutPaymentPo.setPayType(reqJson.getString("payType"));
        carInoutPaymentPo.setRealCharge(reqJson.getString("realCharge"));
        int flag = carInoutPaymentV1InnerServiceSMOImpl.saveCarInoutPayment(carInoutPaymentPo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }

        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setPaId(carInoutDtos.get(0).getPaId());
        carInoutPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutPo.setCommunityId(carInoutDtos.get(0).getCommunityId());
        carInoutPo.setState(CarInoutDto.STATE_PAY);
        flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);

        if (flag < 1) {
            throw new CmdException("更新出场时间失败");
        }

        //缴费
        hasFeeAndPayFee(carInoutPo, carInoutPaymentPo);
    }

    private boolean hasFeeAndPayFee(CarInoutPo carInoutPo, CarInoutPaymentPo carInoutPaymentPo) {

        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(carInoutPo.getCommunityId());
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_CAR_INOUT_ID);
        feeAttrDto.setValue(carInoutPo.getInoutId());
        feeAttrDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFeeByAttr(feeAttrDto);
        if (feeDtos == null || feeDtos.size() < 1) {
            return false;
        }
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setState(FeeDto.STATE_FINISH);
        payFeePo.setFeeId(feeDtos.get(0).getFeeId());
        payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
        int flag = feeInnerServiceSMOImpl.updateFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setPrimeRate("1.00");
        FeeDto feeDto = feeDtos.get(0);
        payFeeDetailPo.setStartTime(DateUtil.getFormatTimeString(feeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setCommunityId(carInoutPaymentPo.getCommunityId());
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(carInoutPaymentPo.getPayCharge());
        payFeeDetailPo.setReceivedAmount(carInoutPaymentPo.getRealCharge());
        payFeeDetailPo.setFeeId(feeDto.getFeeId());

        flag = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }
        return true;
    }
}
