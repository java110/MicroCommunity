package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.product.ProductDto;
import com.java110.goods.bmo.product.IDeleteProductBMO;
import com.java110.goods.bmo.product.IGetProductBMO;
import com.java110.goods.bmo.product.ISaveProductBMO;
import com.java110.goods.bmo.product.IUpdateProductBMO;
import com.java110.po.product.ProductPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/product")
public class ProductApi {

    @Autowired
    private ISaveProductBMO saveProductBMOImpl;
    @Autowired
    private IUpdateProductBMO updateProductBMOImpl;
    @Autowired
    private IDeleteProductBMO deleteProductBMOImpl;

    @Autowired
    private IGetProductBMO getProductBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProduct
     * @path /app/product/saveProduct
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public ResponseEntity<String> saveProduct(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "prodName", "请求报文中未包含prodName");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "keyword", "请求报文中未包含keyword");
        Assert.hasKeyAndValue(reqJson, "categoryId", "请求报文中未包含categoryId");
        Assert.hasKeyAndValue(reqJson, "unitName", "请求报文中未包含unitName");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");
        Assert.hasKeyAndValue(reqJson, "isPostage", "请求报文中未包含isPostage");


        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        return saveProductBMOImpl.save(productPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/updateProduct
     * @path /app/product/updateProduct
     */
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public ResponseEntity<String> updateProduct(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "prodName", "请求报文中未包含prodName");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "keyword", "请求报文中未包含keyword");
        Assert.hasKeyAndValue(reqJson, "categoryId", "请求报文中未包含categoryId");
        Assert.hasKeyAndValue(reqJson, "unitName", "请求报文中未包含unitName");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");
        Assert.hasKeyAndValue(reqJson, "isPostage", "请求报文中未包含isPostage");
        Assert.hasKeyAndValue(reqJson, "productId", "productId不能为空");


        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        return updateProductBMOImpl.update(productPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/deleteProduct
     * @path /app/product/deleteProduct
     */
    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProduct(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "productId", "productId不能为空");


        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        return deleteProductBMOImpl.delete(productPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /product/queryProduct
     * @path /app/product/queryProduct
     */
    @RequestMapping(value = "/queryProduct", method = RequestMethod.GET)
    public ResponseEntity<String> queryProduct(@RequestParam(value = "store-id") String storeId,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "row") int row) {
        ProductDto productDto = new ProductDto();
        productDto.setPage(page);
        productDto.setRow(row);
        productDto.setStoreId(storeId);
        return getProductBMOImpl.get(productDto);
    }
}
