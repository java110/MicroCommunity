package com.java110.store.smo.impl;


import com.java110.dto.store.StoreAttrDto;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.po.store.StoreAttrPo;
import com.java110.store.dao.IStoreAttrServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.IStoreAttrInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商户属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreAttrInnerServiceSMOImpl extends BaseServiceSMO implements IStoreAttrInnerServiceSMO {

    @Autowired
    private IStoreAttrServiceDao storeAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<StoreAttrDto> queryStoreAttrs(@RequestBody  StoreAttrDto storeAttrDto) {

        //校验是否传了 分页信息

        int page = storeAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeAttrDto.setPage((page - 1) * storeAttrDto.getRow());
        }

        List<StoreAttrDto> storeAttrs = BeanConvertUtil.covertBeanList(storeAttrServiceDaoImpl.getStoreAttrInfo(BeanConvertUtil.beanCovertMap(storeAttrDto)), StoreAttrDto.class);

        if (storeAttrs == null || storeAttrs.size() == 0) {
            return storeAttrs;
        }

        return storeAttrs;
    }

    @Override
    public int saveStoreAttrInfoInstance(@RequestBody StoreAttrPo storeAttrPo) {

        int saveFlag = storeAttrServiceDaoImpl.saveStoreAttrInfoInstance(BeanConvertUtil.beanCovertMap(storeAttrPo));

        return saveFlag;
    }
    @Override
    public int saveStoreAttrInfo(@RequestBody StoreAttrPo storeAttrPo) {

        int saveFlag = storeAttrServiceDaoImpl.saveStoreAttrInfo(BeanConvertUtil.beanCovertMap(storeAttrPo));

        return saveFlag;
    }
    @Override
    public int updateStoreAttrInfoInstance(@RequestBody StoreAttrPo storeAttrPo) {

        int saveFlag = storeAttrServiceDaoImpl.updateStoreAttrInfoInstance(BeanConvertUtil.beanCovertMap(storeAttrPo));

        return saveFlag;
    }

    @Override
    public int queryStoreAttrsCount(@RequestBody StoreAttrDto storeAttrDto) {
        return storeAttrServiceDaoImpl.queryStoreAttrsCount(BeanConvertUtil.beanCovertMap(storeAttrDto));    }

    public IStoreAttrServiceDao getStoreAttrServiceDaoImpl() {
        return storeAttrServiceDaoImpl;
    }

    public void setStoreAttrServiceDaoImpl(IStoreAttrServiceDao storeAttrServiceDaoImpl) {
        this.storeAttrServiceDaoImpl = storeAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
