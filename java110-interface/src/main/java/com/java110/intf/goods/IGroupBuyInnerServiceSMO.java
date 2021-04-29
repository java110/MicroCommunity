package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.groupBuy.GroupBuyDto;
import com.java110.po.groupBuy.GroupBuyPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IGroupBuyInnerServiceSMO
 * @Description 拼团购买接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/groupBuyApi")
public interface IGroupBuyInnerServiceSMO {


    @RequestMapping(value = "/saveGroupBuy", method = RequestMethod.POST)
    public int saveGroupBuy(@RequestBody GroupBuyPo groupBuyPo);

    @RequestMapping(value = "/updateGroupBuy", method = RequestMethod.POST)
    public int updateGroupBuy(@RequestBody GroupBuyPo groupBuyPo);

    @RequestMapping(value = "/deleteGroupBuy", method = RequestMethod.POST)
    public int deleteGroupBuy(@RequestBody GroupBuyPo groupBuyPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param groupBuyDto 数据对象分享
     * @return GroupBuyDto 对象数据
     */
    @RequestMapping(value = "/queryGroupBuys", method = RequestMethod.POST)
    List<GroupBuyDto> queryGroupBuys(@RequestBody GroupBuyDto groupBuyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param groupBuyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryGroupBuysCount", method = RequestMethod.POST)
    int queryGroupBuysCount(@RequestBody GroupBuyDto groupBuyDto);
}
