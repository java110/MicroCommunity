package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.groupBuy.GroupBuyBatchDto;
import com.java110.po.groupBuyBatch.GroupBuyBatchPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IGroupBuyBatchInnerServiceSMO
 * @Description 拼团批次接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/groupBuyBatchApi")
public interface IGroupBuyBatchInnerServiceSMO {


    @RequestMapping(value = "/saveGroupBuyBatch", method = RequestMethod.POST)
    public int saveGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo);

    @RequestMapping(value = "/updateGroupBuyBatch", method = RequestMethod.POST)
    public int updateGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo);

    @RequestMapping(value = "/deleteGroupBuyBatch", method = RequestMethod.POST)
    public int deleteGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param groupBuyBatchDto 数据对象分享
     * @return GroupBuyBatchDto 对象数据
     */
    @RequestMapping(value = "/queryGroupBuyBatchs", method = RequestMethod.POST)
    List<GroupBuyBatchDto> queryGroupBuyBatchs(@RequestBody GroupBuyBatchDto groupBuyBatchDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param groupBuyBatchDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryGroupBuyBatchsCount", method = RequestMethod.POST)
    int queryGroupBuyBatchsCount(@RequestBody GroupBuyBatchDto groupBuyBatchDto);
}
