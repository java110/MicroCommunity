package com.java110.goods.bmo.storeOrderCart.impl;

import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.bmo.storeOrderCart.IGetStoreOrderCartBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.intf.goods.IStoreOrderCartInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderCartBMOImpl")
public class GetStoreOrderCartBMOImpl implements IGetStoreOrderCartBMO {

    @Autowired
    private IStoreOrderCartInnerServiceSMO storeOrderCartInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param storeOrderCartDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderCartDto storeOrderCartDto) {


        int count = storeOrderCartInnerServiceSMOImpl.queryStoreOrderCartsCount(storeOrderCartDto);

        List<StoreOrderCartDto> storeOrderCartDtos = null;
        if (count > 0) {
            storeOrderCartDtos = storeOrderCartInnerServiceSMOImpl.queryStoreOrderCarts(storeOrderCartDto);
            freshOrderCart(storeOrderCartDtos);
        } else {
            storeOrderCartDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderCartDto.getRow()), count, storeOrderCartDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 刷新规格
     * @param storeOrderCartDtos
     */
    private void freshOrderCart(List<StoreOrderCartDto> storeOrderCartDtos) {

        List<String> specIds = new ArrayList<>();

        for(StoreOrderCartDto storeOrderCartDto : storeOrderCartDtos){
            specIds.add(storeOrderCartDto.getSpecId());
        }

        ProductSpecDetailDto productSpecDetailDto = new ProductSpecDetailDto();
        productSpecDetailDto.setSpecIds(specIds.toArray(new String[specIds.size()]));
        List<ProductSpecDetailDto> productSpecDetailDtos = productSpecDetailInnerServiceSMOImpl.queryProductSpecDetails(productSpecDetailDto);
        List<ProductSpecDetailDto> tmpProductSpecDetailDtos = null;
        for (StoreOrderCartDto storeOrderCartDto : storeOrderCartDtos) {
            tmpProductSpecDetailDtos = new ArrayList<>();
            for (ProductSpecDetailDto tmpProductSpecDetailDto : productSpecDetailDtos) {
                if (storeOrderCartDto.getSpecId().equals(tmpProductSpecDetailDto.getSpecId())) {
                    tmpProductSpecDetailDtos.add(tmpProductSpecDetailDto);
                }
            }
            storeOrderCartDto.setProductSpecDetails(tmpProductSpecDetailDtos);
        }
    }

}
