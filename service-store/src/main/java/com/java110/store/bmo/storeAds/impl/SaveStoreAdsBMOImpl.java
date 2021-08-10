package com.java110.store.bmo.storeAds.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.store.IStoreAdsInnerServiceSMO;
import com.java110.po.storeAds.StoreAdsPo;
import com.java110.store.bmo.storeAds.ISaveStoreAdsBMO;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreAdsBMOImpl")
public class SaveStoreAdsBMOImpl implements ISaveStoreAdsBMO {

    @Autowired
    private IStoreAdsInnerServiceSMO storeAdsInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeAdsPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(StoreAdsPo storeAdsPo) {

        if (StringUtil.isEmpty(storeAdsPo.getUrl())) {
            throw new IllegalArgumentException("未包含图片信息");
        }

        storeAdsPo.setState("2000");

        //保存图片
        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(storeAdsPo.getUrl());
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(storeAdsPo.getShareId());
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        storeAdsPo.setUrl(fileName);
        storeAdsPo.setAdsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_adsId));
        int flag = storeAdsInnerServiceSMOImpl.saveStoreAds(storeAdsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
