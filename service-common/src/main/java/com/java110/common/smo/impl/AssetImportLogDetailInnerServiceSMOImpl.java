package com.java110.common.smo.impl;


import com.java110.common.dao.IAssetImportLogDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 批量操作日志详情内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AssetImportLogDetailInnerServiceSMOImpl extends BaseServiceSMO implements IAssetImportLogDetailInnerServiceSMO {

    @Autowired
    private IAssetImportLogDetailServiceDao assetImportLogDetailServiceDaoImpl;


    @Override
    public int saveAssetImportLogDetail(@RequestBody AssetImportLogDetailPo assetImportLogDetailPo) {
        int saveFlag = 1;
        assetImportLogDetailServiceDaoImpl.saveAssetImportLogDetailInfo(BeanConvertUtil.beanCovertMap(assetImportLogDetailPo));
        return saveFlag;
    }

    @Override
    public int updateAssetImportLogDetail(@RequestBody AssetImportLogDetailPo assetImportLogDetailPo) {
        int saveFlag = 1;
        assetImportLogDetailServiceDaoImpl.updateAssetImportLogDetailInfo(BeanConvertUtil.beanCovertMap(assetImportLogDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteAssetImportLogDetail(@RequestBody AssetImportLogDetailPo assetImportLogDetailPo) {
        int saveFlag = 1;
        assetImportLogDetailPo.setStatusCd("1");
        assetImportLogDetailServiceDaoImpl.updateAssetImportLogDetailInfo(BeanConvertUtil.beanCovertMap(assetImportLogDetailPo));
        return saveFlag;
    }

    @Override
    public List<AssetImportLogDetailDto> queryAssetImportLogDetails(@RequestBody AssetImportLogDetailDto assetImportLogDetailDto) {

        //校验是否传了 分页信息

        int page = assetImportLogDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            assetImportLogDetailDto.setPage((page - 1) * assetImportLogDetailDto.getRow());
        }

        List<AssetImportLogDetailDto> assetImportLogDetails = BeanConvertUtil.covertBeanList(assetImportLogDetailServiceDaoImpl.getAssetImportLogDetailInfo(BeanConvertUtil.beanCovertMap(assetImportLogDetailDto)), AssetImportLogDetailDto.class);

        return assetImportLogDetails;
    }


    @Override
    public int queryAssetImportLogDetailsCount(@RequestBody AssetImportLogDetailDto assetImportLogDetailDto) {
        return assetImportLogDetailServiceDaoImpl.queryAssetImportLogDetailsCount(BeanConvertUtil.beanCovertMap(assetImportLogDetailDto));
    }

    @Override
    public int saveAssetImportLogDetails(@RequestBody List<AssetImportLogDetailPo> assetImportLogDetailPos) {
        Map info = new HashMap();
        info.put("assetImportLogDetailPos",assetImportLogDetailPos);
        return assetImportLogDetailServiceDaoImpl.saveAssetImportLogDetails(info);
    }

    public IAssetImportLogDetailServiceDao getAssetImportLogDetailServiceDaoImpl() {
        return assetImportLogDetailServiceDaoImpl;
    }

    public void setAssetImportLogDetailServiceDaoImpl(IAssetImportLogDetailServiceDao assetImportLogDetailServiceDaoImpl) {
        this.assetImportLogDetailServiceDaoImpl = assetImportLogDetailServiceDaoImpl;
    }
}
