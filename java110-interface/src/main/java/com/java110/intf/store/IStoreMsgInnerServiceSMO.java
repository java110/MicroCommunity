package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeMsg.StoreMsgDto;
import com.java110.po.storeMsg.StoreMsgPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreMsgInnerServiceSMO
 * @Description 商户消息接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeMsgApi")
public interface IStoreMsgInnerServiceSMO {


    @RequestMapping(value = "/saveStoreMsg", method = RequestMethod.POST)
    public int saveStoreMsg(@RequestBody StoreMsgPo storeMsgPo);

    @RequestMapping(value = "/updateStoreMsg", method = RequestMethod.POST)
    public int updateStoreMsg(@RequestBody  StoreMsgPo storeMsgPo);

    @RequestMapping(value = "/deleteStoreMsg", method = RequestMethod.POST)
    public int deleteStoreMsg(@RequestBody  StoreMsgPo storeMsgPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeMsgDto 数据对象分享
     * @return StoreMsgDto 对象数据
     */
    @RequestMapping(value = "/queryStoreMsgs", method = RequestMethod.POST)
    List<StoreMsgDto> queryStoreMsgs(@RequestBody StoreMsgDto storeMsgDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeMsgDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreMsgsCount", method = RequestMethod.POST)
    int queryStoreMsgsCount(@RequestBody StoreMsgDto storeMsgDto);
}
