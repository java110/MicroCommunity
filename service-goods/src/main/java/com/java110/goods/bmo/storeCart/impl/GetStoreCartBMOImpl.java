package com.java110.goods.bmo.storeCart.impl;

import com.java110.dto.file.FileRelDto;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.goods.bmo.storeCart.IGetStoreCartBMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreCartBMOImpl")
public class GetStoreCartBMOImpl implements IGetStoreCartBMO {

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param storeCartDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreCartDto storeCartDto) {


        int count = storeCartInnerServiceSMOImpl.queryStoreCartsCount(storeCartDto);

        List<StoreCartDto> storeCartDtos = null;
        if (count > 0) {
            storeCartDtos = storeCartInnerServiceSMOImpl.queryStoreCarts(storeCartDto);
            freshStoreCartDtos(storeCartDtos);
        } else {
            storeCartDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeCartDto.getRow()), count, storeCartDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshStoreCartDtos(List<StoreCartDto> storeCartDtos) {

        if (storeCartDtos == null || storeCartDtos.size() < 1) {
            return;
        }

        //输入
        List<String> productIds = new ArrayList<>();

        for (StoreCartDto tmpProductDto : storeCartDtos) {
            productIds.add(tmpProductDto.getProductId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(productIds.toArray(new String[productIds.size()]));
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_COVER);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        //刷入图片信息
        List<String> photoVos = null;
        String url = null;
        for (StoreCartDto tmpProductDto : storeCartDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpProductDto.getProductId().equals(tmpFileRelDto.getObjId())) {
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=-1";
                    tmpProductDto.setCoverPhoto(url);
                }
            }
        }


    }

}
