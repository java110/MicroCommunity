package com.java110.goods.bmo.productSpecDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productSpecDetail.IUpdateProductSpecDetailBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateProductSpecDetailBMOImpl")
public class UpdateProductSpecDetailBMOImpl implements IUpdateProductSpecDetailBMO {

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productSpecDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ProductSpecDetailPo productSpecDetailPo) {

        int flag = productSpecDetailInnerServiceSMOImpl.updateProductSpecDetail(productSpecDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
