package com.java110.goods.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.product.ProductDto;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.product.ProductSpecDto;
import com.java110.dto.productAttr.ProductAttrDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.bmo.product.IDeleteProductBMO;
import com.java110.goods.bmo.product.IGetProductBMO;
import com.java110.goods.bmo.product.ISaveProductBMO;
import com.java110.goods.bmo.product.IUpdateProductBMO;
import com.java110.goods.bmo.productAttr.IDeleteProductAttrBMO;
import com.java110.goods.bmo.productAttr.IGetProductAttrBMO;
import com.java110.goods.bmo.productAttr.ISaveProductAttrBMO;
import com.java110.goods.bmo.productAttr.IUpdateProductAttrBMO;
import com.java110.goods.bmo.productSpec.IDeleteProductSpecBMO;
import com.java110.goods.bmo.productSpec.IGetProductSpecBMO;
import com.java110.goods.bmo.productSpec.ISaveProductSpecBMO;
import com.java110.goods.bmo.productSpec.IUpdateProductSpecBMO;
import com.java110.goods.bmo.productSpecDetail.IDeleteProductSpecDetailBMO;
import com.java110.goods.bmo.productSpecDetail.IGetProductSpecDetailBMO;
import com.java110.goods.bmo.productSpecDetail.ISaveProductSpecDetailBMO;
import com.java110.goods.bmo.productSpecDetail.IUpdateProductSpecDetailBMO;
import com.java110.goods.bmo.productSpecValue.IDeleteProductSpecValueBMO;
import com.java110.goods.bmo.productSpecValue.IGetProductSpecValueBMO;
import com.java110.goods.bmo.productSpecValue.ISaveProductSpecValueBMO;
import com.java110.goods.bmo.productSpecValue.IUpdateProductSpecValueBMO;
import com.java110.po.product.ProductPo;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.po.product.ProductSpecPo;
import com.java110.po.productAttr.ProductAttrPo;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    private ISaveProductSpecBMO saveProductSpecBMOImpl;
    @Autowired
    private IUpdateProductSpecBMO updateProductSpecBMOImpl;
    @Autowired
    private IDeleteProductSpecBMO deleteProductSpecBMOImpl;

    @Autowired
    private IGetProductSpecBMO getProductSpecBMOImpl;

    @Autowired
    private ISaveProductSpecDetailBMO saveProductSpecDetailBMOImpl;
    @Autowired
    private IUpdateProductSpecDetailBMO updateProductSpecDetailBMOImpl;
    @Autowired
    private IDeleteProductSpecDetailBMO deleteProductSpecDetailBMOImpl;

    @Autowired
    private IGetProductSpecDetailBMO getProductSpecDetailBMOImpl;

    @Autowired
    private ISaveProductSpecValueBMO saveProductSpecValueBMOImpl;
    @Autowired
    private IUpdateProductSpecValueBMO updateProductSpecValueBMOImpl;
    @Autowired
    private IDeleteProductSpecValueBMO deleteProductSpecValueBMOImpl;

    @Autowired
    private IGetProductSpecValueBMO getProductSpecValueBMOImpl;

    @Autowired
    private ISaveProductAttrBMO saveProductAttrBMOImpl;
    @Autowired
    private IUpdateProductAttrBMO updateProductAttrBMOImpl;
    @Autowired
    private IDeleteProductAttrBMO deleteProductAttrBMOImpl;

    @Autowired
    private IGetProductAttrBMO getProductAttrBMOImpl;


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProduct
     * @path /app/product/saveProduct
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public ResponseEntity<String> saveProduct(@RequestBody JSONObject reqJson,
                                              @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "prodName", "请求报文中未包含prodName");
        Assert.hasKeyAndValue(reqJson, "keyword", "请求报文中未包含keyword");
        Assert.hasKeyAndValue(reqJson, "categoryId", "请求报文中未包含categoryId");
        Assert.hasKeyAndValue(reqJson, "unitName", "请求报文中未包含unitName");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");
        Assert.hasKeyAndValue(reqJson, "isPostage", "请求报文中未包含isPostage");
        Assert.hasKeyAndValue(reqJson, "coverPhoto", "请求报文中未包含商品封面");

        Assert.hasKey(reqJson,"carouselFigurePhoto","请求报文中未包含轮播图");
        Assert.hasKey(reqJson,"productSpecs","请求报文中未包含规格信息");


        String coverPhoto = reqJson.getString("coverPhoto");
        JSONArray carouselFigurePhoto = reqJson.getJSONArray("carouselFigurePhoto");

        JSONArray productSpecs = reqJson.getJSONArray("productSpecs");

        if(carouselFigurePhoto.size() < 1){
            throw new IllegalArgumentException("请求报文中未包含轮播图");
        }
        if(productSpecs.size() < 1){
            throw new IllegalArgumentException("请求报文中未包含规格信息");
        }

        JSONObject spec = null;
        List<ProductSpecValuePo> productSpecValuePos = new ArrayList<>();
        for (int specIndex = 0; specIndex < productSpecs.size(); specIndex++) {
            spec = productSpecs.getJSONObject(specIndex);
            productSpecValuePos.add(BeanConvertUtil.covertBean(spec,ProductSpecValuePo.class));
        }

        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        productPo.setStoreId(storeId);
        return saveProductBMOImpl.save(productPo,coverPhoto,carouselFigurePhoto,productSpecValuePos);
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
    public ResponseEntity<String> updateProduct(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "prodName", "请求报文中未包含prodName");
        Assert.hasKeyAndValue(reqJson, "keyword", "请求报文中未包含keyword");
        Assert.hasKeyAndValue(reqJson, "categoryId", "请求报文中未包含categoryId");
        Assert.hasKeyAndValue(reqJson, "unitName", "请求报文中未包含unitName");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");
        Assert.hasKeyAndValue(reqJson, "isPostage", "请求报文中未包含isPostage");
        Assert.hasKeyAndValue(reqJson, "productId", "productId不能为空");


        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        productPo.setStoreId(storeId);
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
    public ResponseEntity<String> deleteProduct(@RequestBody JSONObject reqJson,
                                                @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "productId", "productId不能为空");


        ProductPo productPo = BeanConvertUtil.covertBean(reqJson, ProductPo.class);
        productPo.setStoreId(storeId);
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
    public ResponseEntity<String> queryProduct(@RequestHeader(value = "store-id") String storeId,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "row") int row) {
        ProductDto productDto = new ProductDto();
        productDto.setPage(page);
        productDto.setRow(row);
        productDto.setStoreId(storeId);
        return getProductBMOImpl.get(productDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProductSpec
     * @path /app/product/saveProductSpec
     */
    @RequestMapping(value = "/saveProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductSpec(@RequestBody JSONObject reqJson,
                                                  @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");

        if (!reqJson.containsKey("specDetails")) {
            throw new IllegalArgumentException("未包含规格信息");
        }

        JSONArray specDetails = reqJson.getJSONArray("specDetails");

        if (specDetails.size() < 1) {
            throw new IllegalArgumentException("未包含规格信息");
        }
        ProductSpecDetailPo productSpecDetailPo = null;
        JSONObject specDetail = null;
        List<ProductSpecDetailPo> productSpecDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < specDetails.size(); detailIndex++) {
            specDetail = specDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(specDetail, "detailName", "未包含规格");
            Assert.hasKeyAndValue(specDetail, "detailValue", "未包含规格值");
            productSpecDetailPo = BeanConvertUtil.covertBean(specDetail, ProductSpecDetailPo.class);
            productSpecDetailPos.add(productSpecDetailPo);
        }


        ProductSpecPo productSpecPo = BeanConvertUtil.covertBean(reqJson, ProductSpecPo.class);
        productSpecPo.setStoreId(storeId);
        return saveProductSpecBMOImpl.save(productSpecPo, productSpecDetailPos);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/updateProductSpec
     * @path /app/product/updateProductSpec
     */
    @RequestMapping(value = "/updateProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductSpec(@RequestBody JSONObject reqJson,
                                                    @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "specId", "specId不能为空");


        if (!reqJson.containsKey("specDetails")) {
            throw new IllegalArgumentException("未包含规格信息");
        }

        JSONArray specDetails = reqJson.getJSONArray("specDetails");

        if (specDetails.size() < 1) {
            throw new IllegalArgumentException("未包含规格信息");
        }
        ProductSpecDetailPo productSpecDetailPo = null;
        JSONObject specDetail = null;
        List<ProductSpecDetailPo> productSpecDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < specDetails.size(); detailIndex++) {
            specDetail = specDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(specDetail, "detailName", "未包含规格");
            Assert.hasKeyAndValue(specDetail, "detailValue", "未包含规格值");
            productSpecDetailPo = BeanConvertUtil.covertBean(specDetail, ProductSpecDetailPo.class);
            productSpecDetailPos.add(productSpecDetailPo);
        }

        ProductSpecPo productSpecPo = BeanConvertUtil.covertBean(reqJson, ProductSpecPo.class);
        productSpecPo.setStoreId(storeId);
        return updateProductSpecBMOImpl.update(productSpecPo, productSpecDetailPos);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/deleteProductSpec
     * @path /app/product/deleteProductSpec
     */
    @RequestMapping(value = "/deleteProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductSpec(@RequestBody JSONObject reqJson,
                                                    @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specId", "specId不能为空");


        ProductSpecPo productSpecPo = BeanConvertUtil.covertBean(reqJson, ProductSpecPo.class);
        productSpecPo.setStoreId(storeId);
        return deleteProductSpecBMOImpl.delete(productSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /product/queryProductSpec
     * @path /app/product/queryProductSpec
     */
    @RequestMapping(value = "/queryProductSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryProductSpec(@RequestHeader(value = "store-id") String storeId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        ProductSpecDto productSpecDto = new ProductSpecDto();
        productSpecDto.setPage(page);
        productSpecDto.setRow(row);
        productSpecDto.setStoreId(storeId);
        return getProductSpecBMOImpl.get(productSpecDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProductSpecDetail
     * @path /app/product/saveProductSpecDetail
     */
    @RequestMapping(value = "/saveProductSpecDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductSpecDetail(@RequestBody JSONObject reqJson,
                                                        @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "detailName", "请求报文中未包含detailName");
        Assert.hasKeyAndValue(reqJson, "detailValue", "请求报文中未包含detailValue");


        ProductSpecDetailPo productSpecDetailPo = BeanConvertUtil.covertBean(reqJson, ProductSpecDetailPo.class);
        productSpecDetailPo.setStoreId(storeId);
        return saveProductSpecDetailBMOImpl.save(productSpecDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/updateProductSpecDetail
     * @path /app/product/updateProductSpecDetail
     */
    @RequestMapping(value = "/updateProductSpecDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductSpecDetail(@RequestBody JSONObject reqJson,
                                                          @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "detailName", "请求报文中未包含detailName");
        Assert.hasKeyAndValue(reqJson, "detailValue", "请求报文中未包含detailValue");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ProductSpecDetailPo productSpecDetailPo = BeanConvertUtil.covertBean(reqJson, ProductSpecDetailPo.class);
        productSpecDetailPo.setStoreId(storeId);
        return updateProductSpecDetailBMOImpl.update(productSpecDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/deleteProductSpecDetail
     * @path /app/product/deleteProductSpecDetail
     */
    @RequestMapping(value = "/deleteProductSpecDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductSpecDetail(@RequestBody JSONObject reqJson,
                                                          @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ProductSpecDetailPo productSpecDetailPo = BeanConvertUtil.covertBean(reqJson, ProductSpecDetailPo.class);
        productSpecDetailPo.setStoreId(storeId);
        return deleteProductSpecDetailBMOImpl.delete(productSpecDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /product/queryProductSpecDetail
     * @path /app/product/queryProductSpecDetail
     */
    @RequestMapping(value = "/queryProductSpecDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryProductSpecDetail(@RequestHeader(value = "store-id") String storeId,
                                                         @RequestParam(value = "specId", required = false) String specId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ProductSpecDetailDto productSpecDetailDto = new ProductSpecDetailDto();
        productSpecDetailDto.setPage(page);
        productSpecDetailDto.setRow(row);
        productSpecDetailDto.setSpecId(specId);
        productSpecDetailDto.setStoreId(storeId);
        return getProductSpecDetailBMOImpl.get(productSpecDetailDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProductSpecValue
     * @path /app/product/saveProductSpecValue
     */
    @RequestMapping(value = "/saveProductSpecValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductSpecValue(@RequestBody JSONObject reqJson,
                                                       @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "stock", "请求报文中未包含stock");
        Assert.hasKeyAndValue(reqJson, "price", "请求报文中未包含price");
        Assert.hasKeyAndValue(reqJson, "sales", "请求报文中未包含sales");
        Assert.hasKeyAndValue(reqJson, "costPrice", "请求报文中未包含costPrice");
        Assert.hasKeyAndValue(reqJson, "vipPrice", "请求报文中未包含vipPrice");
        Assert.hasKeyAndValue(reqJson, "otPrice", "请求报文中未包含otPrice");


        ProductSpecValuePo productSpecValuePo = BeanConvertUtil.covertBean(reqJson, ProductSpecValuePo.class);
        productSpecValuePo.setStoreId(storeId);
        return saveProductSpecValueBMOImpl.save(productSpecValuePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/updateProductSpecValue
     * @path /app/product/updateProductSpecValue
     */
    @RequestMapping(value = "/updateProductSpecValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductSpecValue(@RequestBody JSONObject reqJson,
                                                         @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "stock", "请求报文中未包含stock");
        Assert.hasKeyAndValue(reqJson, "price", "请求报文中未包含price");
        Assert.hasKeyAndValue(reqJson, "sales", "请求报文中未包含sales");
        Assert.hasKeyAndValue(reqJson, "costPrice", "请求报文中未包含costPrice");
        Assert.hasKeyAndValue(reqJson, "vipPrice", "请求报文中未包含vipPrice");
        Assert.hasKeyAndValue(reqJson, "otPrice", "请求报文中未包含otPrice");
        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        ProductSpecValuePo productSpecValuePo = BeanConvertUtil.covertBean(reqJson, ProductSpecValuePo.class);
        productSpecValuePo.setStoreId(storeId);
        return updateProductSpecValueBMOImpl.update(productSpecValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/deleteProductSpecValue
     * @path /app/product/deleteProductSpecValue
     */
    @RequestMapping(value = "/deleteProductSpecValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductSpecValue(@RequestBody JSONObject reqJson,
                                                         @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        ProductSpecValuePo productSpecValuePo = BeanConvertUtil.covertBean(reqJson, ProductSpecValuePo.class);
        productSpecValuePo.setStoreId(storeId);
        return deleteProductSpecValueBMOImpl.delete(productSpecValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /product/queryProductSpecValue
     * @path /app/product/queryProductSpecValue
     */
    @RequestMapping(value = "/queryProductSpecValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryProductSpecValue(@RequestHeader(value = "store-id") String storeId,
                                                        @RequestParam(value = "productId", required = false) String productId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        ProductSpecValueDto productSpecValueDto = new ProductSpecValueDto();
        productSpecValueDto.setPage(page);
        productSpecValueDto.setRow(row);
        productSpecValueDto.setStoreId(storeId);
        productSpecValueDto.setProductId(productId);
        return getProductSpecValueBMOImpl.get(productSpecValueDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/saveProductAttr
     * @path /app/product/saveProductAttr
     */
    @RequestMapping(value = "/saveProductAttr", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductAttr(@RequestBody JSONObject reqJson,
                                                  @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");


        ProductAttrPo productAttrPo = BeanConvertUtil.covertBean(reqJson, ProductAttrPo.class);
        productAttrPo.setStoreId(storeId);
        return saveProductAttrBMOImpl.save(productAttrPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/updateProductAttr
     * @path /app/product/updateProductAttr
     */
    @RequestMapping(value = "/updateProductAttr", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductAttr(@RequestBody JSONObject reqJson,
                                                    @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");


        ProductAttrPo productAttrPo = BeanConvertUtil.covertBean(reqJson, ProductAttrPo.class);
        productAttrPo.setStoreId(storeId);
        return updateProductAttrBMOImpl.update(productAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /product/deleteProductAttr
     * @path /app/product/deleteProductAttr
     */
    @RequestMapping(value = "/deleteProductAttr", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductAttr(@RequestBody JSONObject reqJson,
                                                    @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");


        ProductAttrPo productAttrPo = BeanConvertUtil.covertBean(reqJson, ProductAttrPo.class);
        productAttrPo.setStoreId(storeId);
        return deleteProductAttrBMOImpl.delete(productAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /product/queryProductAttr
     * @path /app/product/queryProductAttr
     */
    @RequestMapping(value = "/queryProductAttr", method = RequestMethod.GET)
    public ResponseEntity<String> queryProductAttr(@RequestHeader(value = "store-id") String storeId,
                                                   @RequestParam(value = "productId", required = false) String productId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        ProductAttrDto productAttrDto = new ProductAttrDto();
        productAttrDto.setPage(page);
        productAttrDto.setRow(row);
        productAttrDto.setStoreId(storeId);
        productAttrDto.setProductId(productId);
        return getProductAttrBMOImpl.get(productAttrDto);
    }
}
