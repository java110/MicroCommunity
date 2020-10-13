package com.java110.goods.bmo.productSpecDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.productSpecDetail.ISaveProductSpecDetailBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveProductSpecDetailBMOImpl")
public class SaveProductSpecDetailBMOImpl implements ISaveProductSpecDetailBMO {

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param productSpecDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ProductSpecDetailPo productSpecDetailPo) {

        productSpecDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = productSpecDetailInnerServiceSMOImpl.saveProductSpecDetail(productSpecDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
