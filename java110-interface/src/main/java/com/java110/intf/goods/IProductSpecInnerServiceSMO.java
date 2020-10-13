package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductSpecDto;
import com.java110.po.product.ProductSpecPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductSpecInnerServiceSMO
 * @Description 产品规格接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productSpecApi")
public interface IProductSpecInnerServiceSMO {


    @RequestMapping(value = "/saveProductSpec", method = RequestMethod.POST)
    public int saveProductSpec(@RequestBody ProductSpecPo productSpecPo);

    @RequestMapping(value = "/updateProductSpec", method = RequestMethod.POST)
    public int updateProductSpec(@RequestBody ProductSpecPo productSpecPo);

    @RequestMapping(value = "/deleteProductSpec", method = RequestMethod.POST)
    public int deleteProductSpec(@RequestBody ProductSpecPo productSpecPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param productSpecDto 数据对象分享
     * @return ProductSpecDto 对象数据
     */
    @RequestMapping(value = "/queryProductSpecs", method = RequestMethod.POST)
    List<ProductSpecDto> queryProductSpecs(@RequestBody ProductSpecDto productSpecDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productSpecDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductSpecsCount", method = RequestMethod.POST)
    int queryProductSpecsCount(@RequestBody ProductSpecDto productSpecDto);
}
