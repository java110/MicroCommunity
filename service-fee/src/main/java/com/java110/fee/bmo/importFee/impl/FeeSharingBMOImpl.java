package com.java110.fee.bmo.importFee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeFormula.FeeFormulaDto;
import com.java110.fee.bmo.importFee.IFeeSharingBMO;
import com.java110.intf.IImportFeeDetailInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("feeSharingBMOImpl")
public class FeeSharingBMOImpl implements IFeeSharingBMO {

    private static final String IMPORT_FEE_NAME = "导入费用";

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reqJson
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> share(JSONObject reqJson) {


        String scope = reqJson.getString("scope");
        RoomDto roomDto = new RoomDto();
        roomDto.setState(RoomDto.STATE_SELL); // 已售房屋
        List<RoomDto> roomDtos = null;
        if ("1001".equals(scope)) {//小区
            roomDto.setCommunityId(reqJson.getString("objId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else if ("2002".equals(scope)) {//楼栋
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setFloorId(reqJson.getString("objId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else {//单元
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setUnitId(reqJson.getString("objId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        }

        if (roomDtos == null || roomDtos.size() < 1) {
            throw new IllegalArgumentException("未找到相应房屋公摊费用");
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        feeConfigDto.setFeeName(IMPORT_FEE_NAME);
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }

        FeeFormulaDto feeFormulaDto = new FeeFormulaDto();
        feeFormulaDto.setCommunityId(reqJson.getString("communityId"));
        feeFormulaDto.setFormulaId(reqJson.getString("formulaId"));

        List<FeeFormulaDto> feeFormulaDtos = feeFormulaInnerServiceSMOImpl.queryFeeFormulas(feeFormulaDto);
        Assert.listOnlyOne(feeFormulaDtos, "公摊公式错误");

        //公摊公式
        String formulaValue = deakFormula(feeFormulaDtos.get(0));

        //公摊费用到房屋
        sharingFeeToRoom(formulaValue, feeFormulaDtos.get(0).getPrice(), roomDtos, reqJson, feeConfigDto);


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

    /**
     * 公摊费用到房屋
     *
     * @param formulaValue
     * @param roomDtos
     */
    private void sharingFeeToRoom(String formulaValue, double price, List<RoomDto> roomDtos,
                                  JSONObject reqJson, FeeConfigDto feeConfigDto) {


        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        String importFeeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId);
        for (RoomDto roomDto : roomDtos) {
            doSharingFeeToRoom(formulaValue, price, roomDto, reqJson, payFeePos, feeConfigDto, feeAttrPos, importFeeId, importFeeDetailPos);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        //保存日志
        ImportFeePo importFeePo = new ImportFeePo();
        importFeePo.setCommunityId(reqJson.getString("communityId"));
        importFeePo.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        importFeePo.setImportFeeId(importFeeId);
        String scope = reqJson.getString("scope");
        String scopeName = "";
        if ("1001".equals(scope)) {//小区
            scopeName = "整个小区";
        } else if ("2002".equals(scope)) {//楼栋
            scopeName = roomDtos.get(0).getFloorNum() + "栋";
        } else {//单元
            scopeName = roomDtos.get(0).getFloorNum() + "栋" + roomDtos.get(0).getUnitNum() + "单元";
        }
        importFeePo.setRemark("总使用量：" + reqJson.getString("totalDegrees")
                + ",公式：" + formulaValue + ",公摊范围：" + scopeName);
        importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);

        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);

    }

    /**
     * 每个房屋公式
     * T 代表总量，如电费总使用量
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * 举例：公共区域公摊电费 电量/楼栋面积*房屋面积*单价
     * 公式：T/F * R * 1.5 1.5为单价
     *
     * @param formulaValue
     * @param roomDto
     */
    private void doSharingFeeToRoom(String formulaValue, double price, RoomDto roomDto, JSONObject reqJson,
                                    List<PayFeePo> payFeePos, FeeConfigDto feeConfigDto,
                                    List<FeeAttrPo> feeAttrPos, String importFeeId,
                                    List<ImportFeeDetailPo> importFeeDetailPos) {
        String orgFormulaValue = formulaValue;
        formulaValue = formulaValue.replace("T", reqJson.getString("totalDegrees"))
                .replace("F", roomDto.getFloorArea())
                .replace("U", roomDto.getUnitArea())
                .replace("R", roomDto.getBuiltUpArea())
                .replace("X", roomDto.getFeeCoefficient());

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String value = "";
        double amount = 0.0;
        try {
            value = engine.eval(formulaValue).toString();
            BigDecimal valueObj = new BigDecimal(Double.parseDouble(value));
            BigDecimal priceObj = new BigDecimal(price);
            priceObj = valueObj.multiply(priceObj).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            amount = priceObj.doubleValue();
        } catch (Exception e) {
            throw new IllegalArgumentException("公式计算异常，公式为【" + orgFormulaValue + "】,计算 【" + formulaValue + "】异常");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setEndTime(reqJson.getString("startTime"));
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(reqJson.getString("communityId"));
        payFeePo.setConfigId(feeConfigDto.getConfigId());
        payFeePo.setPayerObjId(reqJson.getString("objId"));
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setUserId(reqJson.getString("userId"));
        payFeePo.setIncomeObjId(reqJson.getString("storeId"));
        payFeePo.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setAmount(amount + "");
        //payFeePo.setStartTime(importRoomFee.getStartTime());
        payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

        payFeePos.add(payFeePo);

        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
        feeAttrPo.setValue(reqJson.getString("feeName"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_SHARE_DEGREES);
        feeAttrPo.setValue(reqJson.getString("value"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        ImportFeeDetailPo importFeeDetailPo = new ImportFeeDetailPo();
        importFeeDetailPo.setAmount(value);
        importFeeDetailPo.setCommunityId(reqJson.getString("communityId"));
        importFeeDetailPo.setEndTime(reqJson.getString("endTime"));
        importFeeDetailPo.setFeeId(payFeePo.getFeeId());
        importFeeDetailPo.setFeeName(reqJson.getString("feeName"));
        importFeeDetailPo.setFloorNum(roomDto.getFloorNum());
        importFeeDetailPo.setUnitNum(roomDto.getUnitNum());
        importFeeDetailPo.setRoomNum(roomDto.getRoomNum());
        importFeeDetailPo.setRoomId(roomDto.getRoomId());
        importFeeDetailPo.setStartTime(reqJson.getString("startTime"));
        importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        importFeeDetailPo.setState("1000");
        importFeeDetailPo.setImportFeeId(importFeeId);
        importFeeDetailPo.setRemark("公摊用量：" + value);
        importFeeDetailPos.add(importFeeDetailPo);


    }

    private String deakFormula(FeeFormulaDto feeFormulaDto) {
        String value = feeFormulaDto.getFormulaValue();

        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        return value;
    }


    /**
     * 保存保存导入费用配置
     *
     * @param feeConfigDto
     */
    private void saveFeeConfig(FeeConfigDto feeConfigDto) {

        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(feeConfigDto, PayFeeConfigPo.class);
        payFeeConfigPo.setAdditionalAmount("0");
        payFeeConfigPo.setBillType(FeeConfigDto.BILL_TYPE_MONTH);
        payFeeConfigPo.setComputingFormula("4004");
        payFeeConfigPo.setEndTime(DateUtil.getLastTime());
        payFeeConfigPo.setFeeFlag("2006012");
        payFeeConfigPo.setIsDefault("T");
        payFeeConfigPo.setPaymentCd("2100");
        payFeeConfigPo.setFeeName(IMPORT_FEE_NAME);
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setPaymentCycle("1");
        payFeeConfigPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        int saveFlag = feeConfigInnerServiceSMOImpl.saveFeeConfig(payFeeConfigPo);

        if (saveFlag < 1) {
            throw new IllegalArgumentException("创建导入费用失败");
        }
    }

}
