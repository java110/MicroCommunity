package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductAttrDto;
import com.java110.po.productAttr.ProductAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductAttrInnerServiceSMO
 * @Description 产品属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productAttrApi")
public interface IProductAttrInnerServiceSMO {


    @RequestMapping(value = "/saveProductAttr", method = RequestMethod.POST)
    public int saveProductAttr(@RequestBody ProductAttrPo productAttrPo);

    @RequestMapping(value = "/updateProductAttr", method = RequestMethod.POST)
    public int updateProductAttr(@RequestBody  ProductAttrPo productAttrPo);

    @RequestMapping(value = "/deleteProductAttr", method = RequestMethod.POST)
    public int deleteProductAttr(@RequestBody  ProductAttrPo productAttrPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productAttrDto 数据对象分享
     * @return ProductAttrDto 对象数据
     */
    @RequestMapping(value = "/queryProductAttrs", method = RequestMethod.POST)
    List<ProductAttrDto> queryProductAttrs(@RequestBody ProductAttrDto productAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductAttrsCount", method = RequestMethod.POST)
    int queryProductAttrsCount(@RequestBody ProductAttrDto productAttrDto);
}
