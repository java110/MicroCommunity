package com.java110.goods.bmo.productLabel.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.productLabel.IDeleteProductLabelBMO;
import com.java110.intf.goods.IProductLabelInnerServiceSMO;
import com.java110.po.productLabel.ProductLabelPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteProductLabelBMOImpl")
public class DeleteProductLabelBMOImpl implements IDeleteProductLabelBMO {

    @Autowired
    private IProductLabelInnerServiceSMO productLabelInnerServiceSMOImpl;

    /**
     * @param productLabelPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ProductLabelPo productLabelPo) {

        int flag = productLabelInnerServiceSMOImpl.deleteProductLabel(productLabelPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
