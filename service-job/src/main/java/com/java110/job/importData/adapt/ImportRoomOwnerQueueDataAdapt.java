package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importData.ImportOwnerRoomDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.intf.community.IImportOwnerRoomInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房产信息导入 适配器
 * 前端请求 时 必须传入
 *   param.append('importAdapt', "importRoomOwner");
 */
@Service("importRoomOwnerQueueData")
public class ImportRoomOwnerQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {


    @Autowired
    private IImportOwnerRoomInnerServiceSMO importOwnerRoomInnerServiceSMOImpl;


    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        importDatas(assetImportLogDetailDtos);
    }

    private void importDatas(List<AssetImportLogDetailDto> infos) {
        String state = "";
        String msg = "";
        for (AssetImportLogDetailDto assetImportLogDetailDto : infos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }

    }

    /**
     * 导入数据
     *
     * @param assetImportLogDetailDto
     */
    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportOwnerRoomDto importOwnerRoomDto = BeanConvertUtil.covertBean(data, ImportOwnerRoomDto.class);
        int flag = importOwnerRoomInnerServiceSMOImpl.saveOwnerRoom(importOwnerRoomDto);
        if (flag < 1) {
            throw new IllegalArgumentException("导入失败");
        }

    }


}
