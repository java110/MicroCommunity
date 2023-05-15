package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.importFee.ImportFeeDetailDto;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IImportFeeDetailInnerServiceSMO
 * @Description 费用导入明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/importFeeDetailApi")
public interface IImportFeeDetailInnerServiceSMO {


    @RequestMapping(value = "/saveImportFeeDetail", method = RequestMethod.POST)
    public int saveImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo);


    /**
     * 批量插入
     * @param importFeeDetailPo
     * @return
     */
    @RequestMapping(value = "/saveImportFeeDetails", method = RequestMethod.POST)
    public int saveImportFeeDetails(@RequestBody List<ImportFeeDetailPo> importFeeDetailPo);

    @RequestMapping(value = "/updateImportFeeDetail", method = RequestMethod.POST)
    public int updateImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo);

    @RequestMapping(value = "/deleteImportFeeDetail", method = RequestMethod.POST)
    public int deleteImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param importFeeDetailDto 数据对象分享
     * @return ImportFeeDetailDto 对象数据
     */
    @RequestMapping(value = "/queryImportFeeDetails", method = RequestMethod.POST)
    List<ImportFeeDetailDto> queryImportFeeDetails(@RequestBody ImportFeeDetailDto importFeeDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param importFeeDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryImportFeeDetailsCount", method = RequestMethod.POST)
    int queryImportFeeDetailsCount(@RequestBody ImportFeeDetailDto importFeeDetailDto);
}
