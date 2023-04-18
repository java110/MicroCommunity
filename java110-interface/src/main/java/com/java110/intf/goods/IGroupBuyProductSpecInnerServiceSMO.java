package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.groupBuy.GroupBuyProductSpecDto;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IGroupBuyProductSpecInnerServiceSMO
 * @Description 拼团产品规格接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/groupBuyProductSpecApi")
public interface IGroupBuyProductSpecInnerServiceSMO {


    @RequestMapping(value = "/saveGroupBuyProductSpec", method = RequestMethod.POST)
    public int saveGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo);

    @RequestMapping(value = "/updateGroupBuyProductSpec", method = RequestMethod.POST)
    public int updateGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo);

    @RequestMapping(value = "/deleteGroupBuyProductSpec", method = RequestMethod.POST)
    public int deleteGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param groupBuyProductSpecDto 数据对象分享
     * @return GroupBuyProductSpecDto 对象数据
     */
    @RequestMapping(value = "/queryGroupBuyProductSpecs", method = RequestMethod.POST)
    List<GroupBuyProductSpecDto> queryGroupBuyProductSpecs(@RequestBody GroupBuyProductSpecDto groupBuyProductSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param groupBuyProductSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryGroupBuyProductSpecsCount", method = RequestMethod.POST)
    int queryGroupBuyProductSpecsCount(@RequestBody GroupBuyProductSpecDto groupBuyProductSpecDto);

    @RequestMapping(value = "/queryProductStockAndSales", method = RequestMethod.POST)
    List<GroupBuyProductSpecDto> queryProductStockAndSales(@RequestBody GroupBuyProductSpecDto groupBuyProductSpecDto);
}
