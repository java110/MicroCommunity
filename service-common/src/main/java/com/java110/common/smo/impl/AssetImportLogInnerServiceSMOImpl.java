package com.java110.common.smo.impl;


import com.java110.common.dao.IAssetImportLogServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.po.assetImportLog.AssetImportLogPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 批量操作日志内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AssetImportLogInnerServiceSMOImpl extends BaseServiceSMO implements IAssetImportLogInnerServiceSMO {

    @Autowired
    private IAssetImportLogServiceDao assetImportLogServiceDaoImpl;


    @Override
    public int saveAssetImportLog(@RequestBody AssetImportLogPo assetImportLogPo) {
        int saveFlag = 1;
        assetImportLogServiceDaoImpl.saveAssetImportLogInfo(BeanConvertUtil.beanCovertMap(assetImportLogPo));
        return saveFlag;
    }

    @Override
    public int updateAssetImportLog(@RequestBody AssetImportLogPo assetImportLogPo) {
        int saveFlag = 1;
        assetImportLogServiceDaoImpl.updateAssetImportLogInfo(BeanConvertUtil.beanCovertMap(assetImportLogPo));
        return saveFlag;
    }

    @Override
    public int deleteAssetImportLog(@RequestBody AssetImportLogPo assetImportLogPo) {
        int saveFlag = 1;
        assetImportLogPo.setStatusCd("1");
        assetImportLogServiceDaoImpl.updateAssetImportLogInfo(BeanConvertUtil.beanCovertMap(assetImportLogPo));
        return saveFlag;
    }

    @Override
    public List<AssetImportLogDto> queryAssetImportLogs(@RequestBody AssetImportLogDto assetImportLogDto) {

        //校验是否传了 分页信息

        int page = assetImportLogDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            assetImportLogDto.setPage((page - 1) * assetImportLogDto.getRow());
        }

        List<AssetImportLogDto> assetImportLogs = BeanConvertUtil.covertBeanList(assetImportLogServiceDaoImpl.getAssetImportLogInfo(BeanConvertUtil.beanCovertMap(assetImportLogDto)), AssetImportLogDto.class);

        return assetImportLogs;
    }


    @Override
    public int queryAssetImportLogsCount(@RequestBody AssetImportLogDto assetImportLogDto) {
        return assetImportLogServiceDaoImpl.queryAssetImportLogsCount(BeanConvertUtil.beanCovertMap(assetImportLogDto));
    }

    public IAssetImportLogServiceDao getAssetImportLogServiceDaoImpl() {
        return assetImportLogServiceDaoImpl;
    }

    public void setAssetImportLogServiceDaoImpl(IAssetImportLogServiceDao assetImportLogServiceDaoImpl) {
        this.assetImportLogServiceDaoImpl = assetImportLogServiceDaoImpl;
    }
}
