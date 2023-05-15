package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeePrintSpecDto;
import com.java110.po.feePrintSpec.FeePrintSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeePrintSpecInnerServiceSMO
 * @Description 打印说明接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feePrintSpecApi")
public interface IFeePrintSpecInnerServiceSMO {


    @RequestMapping(value = "/saveFeePrintSpec", method = RequestMethod.POST)
    public int saveFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo);

    @RequestMapping(value = "/updateFeePrintSpec", method = RequestMethod.POST)
    public int updateFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo);

    @RequestMapping(value = "/deleteFeePrintSpec", method = RequestMethod.POST)
    public int deleteFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feePrintSpecDto 数据对象分享
     * @return FeePrintSpecDto 对象数据
     */
    @RequestMapping(value = "/queryFeePrintSpecs", method = RequestMethod.POST)
    List<FeePrintSpecDto> queryFeePrintSpecs(@RequestBody FeePrintSpecDto feePrintSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feePrintSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeePrintSpecsCount", method = RequestMethod.POST)
    int queryFeePrintSpecsCount(@RequestBody FeePrintSpecDto feePrintSpecDto);
}
