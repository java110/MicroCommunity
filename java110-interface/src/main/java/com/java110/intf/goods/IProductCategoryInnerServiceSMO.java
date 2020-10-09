package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductCategoryDto;
import com.java110.po.product.ProductCategoryPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductCategoryInnerServiceSMO
 * @Description 产品目录接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productCategoryApi")
public interface IProductCategoryInnerServiceSMO {


    @RequestMapping(value = "/saveProductCategory", method = RequestMethod.POST)
    public int saveProductCategory(@RequestBody ProductCategoryPo productCategoryPo);

    @RequestMapping(value = "/updateProductCategory", method = RequestMethod.POST)
    public int updateProductCategory(@RequestBody ProductCategoryPo productCategoryPo);

    @RequestMapping(value = "/deleteProductCategory", method = RequestMethod.POST)
    public int deleteProductCategory(@RequestBody ProductCategoryPo productCategoryPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productCategoryDto 数据对象分享
     * @return ProductCategoryDto 对象数据
     */
    @RequestMapping(value = "/queryProductCategorys", method = RequestMethod.POST)
    List<ProductCategoryDto> queryProductCategorys(@RequestBody ProductCategoryDto productCategoryDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productCategoryDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductCategorysCount", method = RequestMethod.POST)
    int queryProductCategorysCount(@RequestBody ProductCategoryDto productCategoryDto);
}
