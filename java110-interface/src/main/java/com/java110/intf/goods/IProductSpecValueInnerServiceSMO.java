package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductSpecValueDto;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductSpecValueInnerServiceSMO
 * @Description 产品规格值接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productSpecValueApi")
public interface IProductSpecValueInnerServiceSMO {


    @RequestMapping(value = "/saveProductSpecValue", method = RequestMethod.POST)
    public int saveProductSpecValue(@RequestBody ProductSpecValuePo productSpecValuePo);

    @RequestMapping(value = "/updateProductSpecValue", method = RequestMethod.POST)
    public int updateProductSpecValue(@RequestBody  ProductSpecValuePo productSpecValuePo);

    @RequestMapping(value = "/deleteProductSpecValue", method = RequestMethod.POST)
    public int deleteProductSpecValue(@RequestBody  ProductSpecValuePo productSpecValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productSpecValueDto 数据对象分享
     * @return ProductSpecValueDto 对象数据
     */
    @RequestMapping(value = "/queryProductSpecValues", method = RequestMethod.POST)
    List<ProductSpecValueDto> queryProductSpecValues(@RequestBody ProductSpecValueDto productSpecValueDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productSpecValueDto 数据对象分享
     * @return ProductSpecValueDto 对象数据
     */
    @RequestMapping(value = "/queryProductStockAndSales", method = RequestMethod.POST)
    List<ProductSpecValueDto> queryProductStockAndSales(@RequestBody ProductSpecValueDto productSpecValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productSpecValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductSpecValuesCount", method = RequestMethod.POST)
    int queryProductSpecValuesCount(@RequestBody ProductSpecValueDto productSpecValueDto);
}
