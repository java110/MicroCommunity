package com.java110.fee.bmo.importFee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeFormula.FeeFormulaDto;
import com.java110.fee.bmo.importFee.IFeeSharingBMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeFormulaInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

@Service("feeSharingBMOImpl")
public class FeeSharingBMOImpl implements IFeeSharingBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeFormulaInnerServiceSMO feeFormulaInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

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

        FeeFormulaDto feeFormulaDto = new FeeFormulaDto();
        feeFormulaDto.setCommunityId(reqJson.getString("communityId"));
        feeFormulaDto.setFormulaId(reqJson.getString("formulaId"));

        List<FeeFormulaDto> feeFormulaDtos = feeFormulaInnerServiceSMOImpl.queryFeeFormulas(feeFormulaDto);
        Assert.listOnlyOne(feeFormulaDtos, "公摊公式错误");

        //公摊公式
        String formulaValue = deakFormula(feeFormulaDtos.get(0));

        //公摊费用到房屋
        sharingFeeToRoom(formulaValue, roomDtos, reqJson);


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

    /**
     * 公摊费用到房屋
     *
     * @param formulaValue
     * @param roomDtos
     */
    private void sharingFeeToRoom(String formulaValue, List<RoomDto> roomDtos, JSONObject reqJson) {


        List<PayFeePo> payFeePos = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            doSharingFeeToRoom(formulaValue, roomDto, reqJson, payFeePos);
        }
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
    private void doSharingFeeToRoom(String formulaValue, RoomDto roomDto, JSONObject reqJson, List<PayFeePo> payFeePos) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "totalDegrees", "未包含使用量");
        Assert.hasKeyAndValue(reqJson, "scope", "未包含公摊范围");
        Assert.hasKeyAndValue(reqJson, "formulaId", "未包含公摊公式");
        Assert.hasKeyAndValue(reqJson, "feeName", "未包含费用名称");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "objId", "未包含公摊对象");
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "未包含费用类型");


        String orgFormulaValue = formulaValue;
        formulaValue = formulaValue.replace("T", reqJson.getString("totalDegrees"))
                .replace("F", roomDto.getFloorArea())
                .replace("U", roomDto.getUnitArea())
                .replace("R", roomDto.getBuiltUpArea())
                .replace("X", roomDto.getFeeCoefficient());

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String value = "";
        try {
            value = engine.eval(formulaValue).toString();
            value = engine.eval(value + ".toFixed(2)").toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("公式计算异常，公式为【" + orgFormulaValue + "】,计算 【" + formulaValue + "】异常");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setEndTime(reqJson.getString("endTime"));
        payFeePo.setUserId(reqJson.getString("userId"));
        payFeePo.setPayerObjId(roomDto.getRoomId());
        payFeePo.setCommunityId(reqJson.getString("communityId"));
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setIncomeObjId(reqJson.getString("storeId"));
        //payFeePo.setFeeFlag();


    }

    private String deakFormula(FeeFormulaDto feeFormulaDto) {
        String value = feeFormulaDto.getFormulaValue();

        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        return value;
    }

}
