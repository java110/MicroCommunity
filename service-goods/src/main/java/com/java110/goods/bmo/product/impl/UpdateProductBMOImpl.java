package com.java110.goods.bmo.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.goods.bmo.product.IUpdateProductBMO;
import com.java110.intf.IProductDetailInnerServiceSMO;
import com.java110.intf.IProductSpecValueInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.product.ProductPo;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("updateProductBMOImpl")
public class UpdateProductBMOImpl implements IUpdateProductBMO {

    @Autowired
    private IProductInnerServiceSMO productInnerServiceSMOImpl;
    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    /**
     * @param productPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductPo productPo, String coverPhoto, JSONArray carouselFigurePhoto,
                                         List<ProductSpecValuePo> productSpecValuePos, ProductDetailPo productDetailPo) {

        int flag = productInnerServiceSMOImpl.updateProduct(productPo);

//保存商品封面
        //删除 图片
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(productPo.getProductId());
        fileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_COVER);
        fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
        doSavaRentingFile(coverPhoto, productPo, FileRelDto.REL_TYPE_CD_GOODS_COVER);

        if (carouselFigurePhoto != null) {
            fileRelPo = new FileRelPo();
            fileRelPo.setObjId(productPo.getProductId());
            fileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_CAROUSEL_FIGURE);
            fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
            for (int carouselIndex = 0; carouselIndex < carouselFigurePhoto.size(); carouselIndex++) {
                doSavaRentingFile(carouselFigurePhoto.getString(carouselIndex), productPo, FileRelDto.REL_TYPE_CD_GOODS_CAROUSEL_FIGURE);
            }
        }
        if (productSpecValuePos != null) {
            //删除所有 规格
            ProductSpecValuePo deleteProductSpecValuePo = new ProductSpecValuePo();
            deleteProductSpecValuePo.setStoreId(productPo.getStoreId());
            deleteProductSpecValuePo.setProductId(productPo.getProductId());
            productSpecValueInnerServiceSMOImpl.deleteProductSpecValue(deleteProductSpecValuePo);
            for (ProductSpecValuePo productSpecValuePo : productSpecValuePos) {
                productSpecValuePo.setStoreId(productPo.getStoreId());
                productSpecValuePo.setProductId(productPo.getProductId());
                productSpecValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
                flag = productSpecValueInnerServiceSMOImpl.saveProductSpecValue(productSpecValuePo);

                if (flag < 1) {
                    throw new IllegalArgumentException("保存规格失败");
                }
            }
        }

        if (productDetailPo == null) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setProductId(productPo.getProductId());
        productDetailDto.setStoreId(productPo.getStoreId());
        List<ProductDetailDto> productDetailDtos = productDetailInnerServiceSMOImpl.queryProductDetails(productDetailDto);

        if (productDetailDtos == null || productDetailDtos.size() < 1) {
            productDetailPo.setProductId(productPo.getProductId());
            productDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            flag = productDetailInnerServiceSMOImpl.saveProductDetail(productDetailPo);
        } else {
            productDetailPo.setProductId(productPo.getProductId());
            flag = productDetailInnerServiceSMOImpl.updateProductDetail(productDetailPo);

        }
        if (flag < 1) {
            throw new IllegalArgumentException("保存规格失败");
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }


    /**
     * 保存房屋租赁 图片
     *
     * @param photo
     * @param productPo
     */
    private void doSavaRentingFile(String photo, ProductPo productPo, String fileTypeCd) {

        if (StringUtil.isEmpty(photo)) {
            return;
        }


        FileDto fileDto = new FileDto();
        fileDto.setCommunityId("-1");
        fileDto.setContext(photo);
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());

        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        FileRelPo fileRelPo = null;
        fileRelPo = new FileRelPo();
        fileRelPo.setObjId(productPo.getProductId());
        fileRelPo.setRelTypeCd(fileTypeCd);
        fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        fileRelPo.setFileRealName(fileName);
        fileRelPo.setFileSaveName(fileName);
        fileRelPo.setSaveWay(FileRelDto.SAVE_WAY_FTP);
        int save = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);

        if (save < 1) {
            throw new IllegalArgumentException("保存文件失败");
        }
    }

}
