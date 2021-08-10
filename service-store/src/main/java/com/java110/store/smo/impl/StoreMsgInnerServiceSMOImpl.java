package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeMsg.StoreMsgDto;
import com.java110.intf.store.IStoreMsgInnerServiceSMO;
import com.java110.po.storeMsg.StoreMsgPo;
import com.java110.store.dao.IStoreMsgServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商户消息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreMsgInnerServiceSMOImpl extends BaseServiceSMO implements IStoreMsgInnerServiceSMO {

    @Autowired
    private IStoreMsgServiceDao storeMsgServiceDaoImpl;


    @Override
    public int saveStoreMsg(@RequestBody StoreMsgPo storeMsgPo) {
        int saveFlag = 1;
        storeMsgServiceDaoImpl.saveStoreMsgInfo(BeanConvertUtil.beanCovertMap(storeMsgPo));
        return saveFlag;
    }

    @Override
    public int updateStoreMsg(@RequestBody StoreMsgPo storeMsgPo) {
        int saveFlag = 1;
        storeMsgServiceDaoImpl.updateStoreMsgInfo(BeanConvertUtil.beanCovertMap(storeMsgPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreMsg(@RequestBody StoreMsgPo storeMsgPo) {
        int saveFlag = 1;
        storeMsgPo.setStatusCd("1");
        storeMsgServiceDaoImpl.updateStoreMsgInfo(BeanConvertUtil.beanCovertMap(storeMsgPo));
        return saveFlag;
    }

    @Override
    public List<StoreMsgDto> queryStoreMsgs(@RequestBody StoreMsgDto storeMsgDto) {

        //校验是否传了 分页信息

        int page = storeMsgDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeMsgDto.setPage((page - 1) * storeMsgDto.getRow());
        }

        List<StoreMsgDto> storeMsgs = BeanConvertUtil.covertBeanList(storeMsgServiceDaoImpl.getStoreMsgInfo(BeanConvertUtil.beanCovertMap(storeMsgDto)), StoreMsgDto.class);

        return storeMsgs;
    }


    @Override
    public int queryStoreMsgsCount(@RequestBody StoreMsgDto storeMsgDto) {
        return storeMsgServiceDaoImpl.queryStoreMsgsCount(BeanConvertUtil.beanCovertMap(storeMsgDto));
    }

    public IStoreMsgServiceDao getStoreMsgServiceDaoImpl() {
        return storeMsgServiceDaoImpl;
    }

    public void setStoreMsgServiceDaoImpl(IStoreMsgServiceDao storeMsgServiceDaoImpl) {
        this.storeMsgServiceDaoImpl = storeMsgServiceDaoImpl;
    }
}
