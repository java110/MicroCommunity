package com.java110.goods.bmo.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.goods.bmo.product.ISaveProductBMO;
import com.java110.intf.IProductDetailInnerServiceSMO;
import com.java110.intf.IProductSpecValueInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.product.ProductPo;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveProductBMOImpl")
public class SaveProductBMOImpl implements ISaveProductBMO {

    @Autowired
    private IProductInnerServiceSMO productInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductPo productPo, String coverPhoto, JSONArray carouselFigurePhoto,
                                       List<ProductSpecValuePo> productSpecValuePos, ProductDetailPo productDetailPo) {

        productPo.setProductId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_productId));
        int flag = productInnerServiceSMOImpl.saveProduct(productPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //保存商品封面
        doSavaRentingFile(coverPhoto, productPo, FileRelDto.REL_TYPE_CD_GOODS_COVER);
        for (int carouselIndex = 0; carouselIndex < carouselFigurePhoto.size(); carouselIndex++) {
            doSavaRentingFile(carouselFigurePhoto.getString(carouselIndex), productPo, FileRelDto.REL_TYPE_CD_GOODS_CAROUSEL_FIGURE);
        }

        for (ProductSpecValuePo productSpecValuePo : productSpecValuePos) {
            productSpecValuePo.setStoreId(productPo.getStoreId());
            productSpecValuePo.setProductId(productPo.getProductId());
            productSpecValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            flag = productSpecValueInnerServiceSMOImpl.saveProductSpecValue(productSpecValuePo);

            if (flag < 1) {
                throw new IllegalArgumentException("保存规格失败");
            }
        }

        productDetailPo.setProductId(productPo.getProductId());
        productDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

        flag = productDetailInnerServiceSMOImpl.saveProductDetail(productDetailPo);
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

        FileDto fileDto = new FileDto();
        fileDto.setCommunityId("-1");
        fileDto.setContext(photo);
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());

        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

        FileRelPo fileRelPo = new FileRelPo();
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
