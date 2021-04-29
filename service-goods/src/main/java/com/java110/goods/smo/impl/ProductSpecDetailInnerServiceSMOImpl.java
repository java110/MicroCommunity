package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.goods.dao.IProductSpecDetailServiceDao;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.po.product.ProductSpecDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品规格明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductSpecDetailInnerServiceSMOImpl extends BaseServiceSMO implements IProductSpecDetailInnerServiceSMO {

    @Autowired
    private IProductSpecDetailServiceDao productSpecDetailServiceDaoImpl;


    @Override
    public int saveProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo) {
        int saveFlag = 1;
        productSpecDetailServiceDaoImpl.saveProductSpecDetailInfo(BeanConvertUtil.beanCovertMap(productSpecDetailPo));
        return saveFlag;
    }

    @Override
    public int updateProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo) {
        int saveFlag = 1;
        productSpecDetailServiceDaoImpl.updateProductSpecDetailInfo(BeanConvertUtil.beanCovertMap(productSpecDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo) {
        int saveFlag = 1;
        productSpecDetailPo.setStatusCd("1");
        productSpecDetailServiceDaoImpl.updateProductSpecDetailInfo(BeanConvertUtil.beanCovertMap(productSpecDetailPo));
        return saveFlag;
    }

    @Override
    public List<ProductSpecDetailDto> queryProductSpecDetails(@RequestBody ProductSpecDetailDto productSpecDetailDto) {

        //校验是否传了 分页信息

        int page = productSpecDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productSpecDetailDto.setPage((page - 1) * productSpecDetailDto.getRow());
        }

        List<ProductSpecDetailDto> productSpecDetails
                = BeanConvertUtil.covertBeanList(productSpecDetailServiceDaoImpl.getProductSpecDetailInfo(BeanConvertUtil.beanCovertMap(productSpecDetailDto)), ProductSpecDetailDto.class);

        return productSpecDetails;
    }


    @Override
    public int queryProductSpecDetailsCount(@RequestBody ProductSpecDetailDto productSpecDetailDto) {
        return productSpecDetailServiceDaoImpl.queryProductSpecDetailsCount(BeanConvertUtil.beanCovertMap(productSpecDetailDto));
    }

    public IProductSpecDetailServiceDao getProductSpecDetailServiceDaoImpl() {
        return productSpecDetailServiceDaoImpl;
    }

    public void setProductSpecDetailServiceDaoImpl(IProductSpecDetailServiceDao productSpecDetailServiceDaoImpl) {
        this.productSpecDetailServiceDaoImpl = productSpecDetailServiceDaoImpl;
    }
}
