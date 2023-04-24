package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeePrintSpecDto;
import com.java110.fee.dao.IFeePrintSpecServiceDao;
import com.java110.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.java110.po.feePrintSpec.FeePrintSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 打印说明内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeePrintSpecInnerServiceSMOImpl extends BaseServiceSMO implements IFeePrintSpecInnerServiceSMO {

    @Autowired
    private IFeePrintSpecServiceDao feePrintSpecServiceDaoImpl;


    @Override
    public int saveFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo) {
        int saveFlag = 1;
        feePrintSpecServiceDaoImpl.saveFeePrintSpecInfo(BeanConvertUtil.beanCovertMap(feePrintSpecPo));
        return saveFlag;
    }

    @Override
    public int updateFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo) {
        int saveFlag = 1;
        feePrintSpecServiceDaoImpl.updateFeePrintSpecInfo(BeanConvertUtil.beanCovertMap(feePrintSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteFeePrintSpec(@RequestBody FeePrintSpecPo feePrintSpecPo) {
        int saveFlag = 1;
        feePrintSpecPo.setStatusCd("1");
        feePrintSpecServiceDaoImpl.updateFeePrintSpecInfo(BeanConvertUtil.beanCovertMap(feePrintSpecPo));
        return saveFlag;
    }

    @Override
    public List<FeePrintSpecDto> queryFeePrintSpecs(@RequestBody FeePrintSpecDto feePrintSpecDto) {

        //校验是否传了 分页信息

        int page = feePrintSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feePrintSpecDto.setPage((page - 1) * feePrintSpecDto.getRow());
        }

        List<FeePrintSpecDto> feePrintSpecs = BeanConvertUtil.covertBeanList(feePrintSpecServiceDaoImpl.getFeePrintSpecInfo(BeanConvertUtil.beanCovertMap(feePrintSpecDto)), FeePrintSpecDto.class);

        return feePrintSpecs;
    }


    @Override
    public int queryFeePrintSpecsCount(@RequestBody FeePrintSpecDto feePrintSpecDto) {
        return feePrintSpecServiceDaoImpl.queryFeePrintSpecsCount(BeanConvertUtil.beanCovertMap(feePrintSpecDto));
    }

    public IFeePrintSpecServiceDao getFeePrintSpecServiceDaoImpl() {
        return feePrintSpecServiceDaoImpl;
    }

    public void setFeePrintSpecServiceDaoImpl(IFeePrintSpecServiceDao feePrintSpecServiceDaoImpl) {
        this.feePrintSpecServiceDaoImpl = feePrintSpecServiceDaoImpl;
    }
}
