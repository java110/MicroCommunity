package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeFormulaDto;
import com.java110.po.feeFormula.FeeFormulaPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeFormulaInnerServiceSMO
 * @Description 费用公式接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeFormulaApi")
public interface IFeeFormulaInnerServiceSMO {


    @RequestMapping(value = "/saveFeeFormula", method = RequestMethod.POST)
    public int saveFeeFormula(@RequestBody FeeFormulaPo feeFormulaPo);

    @RequestMapping(value = "/updateFeeFormula", method = RequestMethod.POST)
    public int updateFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo);

    @RequestMapping(value = "/deleteFeeFormula", method = RequestMethod.POST)
    public int deleteFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeFormulaDto 数据对象分享
     * @return FeeFormulaDto 对象数据
     */
    @RequestMapping(value = "/queryFeeFormulas", method = RequestMethod.POST)
    List<FeeFormulaDto> queryFeeFormulas(@RequestBody FeeFormulaDto feeFormulaDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeFormulaDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeFormulasCount", method = RequestMethod.POST)
    int queryFeeFormulasCount(@RequestBody FeeFormulaDto feeFormulaDto);
}
