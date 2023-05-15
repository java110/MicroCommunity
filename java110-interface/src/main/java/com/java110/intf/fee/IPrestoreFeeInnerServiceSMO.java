package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.prestoreFee.PrestoreFeeDto;
import com.java110.po.prestoreFee.PrestoreFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPrestoreFeeInnerServiceSMO
 * @Description 预存费用接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/prestoreFeeApi")
public interface IPrestoreFeeInnerServiceSMO {


    @RequestMapping(value = "/savePrestoreFee", method = RequestMethod.POST)
    public int savePrestoreFee(@RequestBody  PrestoreFeePo prestoreFeePo);

    @RequestMapping(value = "/updatePrestoreFee", method = RequestMethod.POST)
    public int updatePrestoreFee(@RequestBody  PrestoreFeePo prestoreFeePo);

    @RequestMapping(value = "/deletePrestoreFee", method = RequestMethod.POST)
    public int deletePrestoreFee(@RequestBody PrestoreFeePo prestoreFeePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param prestoreFeeDto 数据对象分享
     * @return PrestoreFeeDto 对象数据
     */
    @RequestMapping(value = "/queryPrestoreFees", method = RequestMethod.POST)
    List<PrestoreFeeDto> queryPrestoreFees(@RequestBody PrestoreFeeDto prestoreFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param prestoreFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPrestoreFeesCount", method = RequestMethod.POST)
    int queryPrestoreFeesCount(@RequestBody PrestoreFeeDto prestoreFeeDto);
}
