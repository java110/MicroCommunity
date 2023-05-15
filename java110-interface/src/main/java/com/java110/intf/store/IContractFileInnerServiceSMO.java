package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.contract.ContractFileDto;
import com.java110.po.contractFile.ContractFilePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IContractFileInnerServiceSMO
 * @Description 合同附件接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractFileApi")
public interface IContractFileInnerServiceSMO {


    @RequestMapping(value = "/saveContractFile", method = RequestMethod.POST)
    public int saveContractFile(@RequestBody ContractFilePo contractFilePo);

    @RequestMapping(value = "/updateContractFile", method = RequestMethod.POST)
    public int updateContractFile(@RequestBody ContractFilePo contractFilePo);

    @RequestMapping(value = "/deleteContractFile", method = RequestMethod.POST)
    public int deleteContractFile(@RequestBody ContractFilePo contractFilePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param contractFileDto 数据对象分享
     * @return ContractFileDto 对象数据
     */
    @RequestMapping(value = "/queryContractFiles", method = RequestMethod.POST)
    List<ContractFileDto> queryContractFiles(@RequestBody ContractFileDto contractFileDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param contractFileDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryContractFilesCount", method = RequestMethod.POST)
    int queryContractFilesCount(@RequestBody ContractFileDto contractFileDto);
}
