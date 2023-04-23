package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeFormulaDto;
import com.java110.fee.bmo.feeFormula.IDeleteFeeFormulaBMO;
import com.java110.fee.bmo.feeFormula.IGetFeeFormulaBMO;
import com.java110.fee.bmo.feeFormula.ISaveFeeFormulaBMO;
import com.java110.fee.bmo.feeFormula.IUpdateFeeFormulaBMO;
import com.java110.po.feeFormula.FeeFormulaPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/feeFormula")
public class FeeFormulaApi {

    @Autowired
    private ISaveFeeFormulaBMO saveFeeFormulaBMOImpl;
    @Autowired
    private IUpdateFeeFormulaBMO updateFeeFormulaBMOImpl;
    @Autowired
    private IDeleteFeeFormulaBMO deleteFeeFormulaBMOImpl;

    @Autowired
    private IGetFeeFormulaBMO getFeeFormulaBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeFormula/saveFeeFormula
     * @path /app/feeFormula/saveFeeFormula
     */
    @RequestMapping(value = "/saveFeeFormula", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeFormula(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "formulaValue", "请求报文中未包含formulaValue");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        FeeFormulaPo feeFormulaPo = BeanConvertUtil.covertBean(reqJson, FeeFormulaPo.class);
        feeFormulaPo.setFormulaType(FeeFormulaDto.FORMULA_TYPE_PUBLIC);
        return saveFeeFormulaBMOImpl.save(feeFormulaPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeFormula/updateFeeFormula
     * @path /app/feeFormula/updateFeeFormula
     */
    @RequestMapping(value = "/updateFeeFormula", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeFormula(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "formulaValue", "请求报文中未包含formulaValue");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "formulaId", "formulaId不能为空");


        FeeFormulaPo feeFormulaPo = BeanConvertUtil.covertBean(reqJson, FeeFormulaPo.class);
        return updateFeeFormulaBMOImpl.update(feeFormulaPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeFormula/deleteFeeFormula
     * @path /app/feeFormula/deleteFeeFormula
     */
    @RequestMapping(value = "/deleteFeeFormula", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeFormula(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "formulaId", "formulaId不能为空");


        FeeFormulaPo feeFormulaPo = BeanConvertUtil.covertBean(reqJson, FeeFormulaPo.class);
        return deleteFeeFormulaBMOImpl.delete(feeFormulaPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeFormula/queryFeeFormula
     * @path /app/feeFormula/queryFeeFormula
     */
    @RequestMapping(value = "/queryFeeFormula", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeFormula(@RequestParam(value = "communityId") String communityId,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row) {
        FeeFormulaDto feeFormulaDto = new FeeFormulaDto();
        feeFormulaDto.setPage(page);
        feeFormulaDto.setRow(row);
        feeFormulaDto.setCommunityId(communityId);
        return getFeeFormulaBMOImpl.get(feeFormulaDto);
    }
}
