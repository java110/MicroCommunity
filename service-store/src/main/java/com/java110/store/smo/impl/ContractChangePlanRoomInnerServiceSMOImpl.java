package com.java110.store.smo.impl;


import com.java110.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import com.java110.store.dao.IContractChangePlanRoomServiceDao;
import com.java110.dto.contract.ContractChangePlanRoomDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同房屋变更内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractChangePlanRoomInnerServiceSMOImpl extends BaseServiceSMO implements IContractChangePlanRoomInnerServiceSMO {

    @Autowired
    private IContractChangePlanRoomServiceDao contractChangePlanRoomServiceDaoImpl;


    @Override
    public int saveContractChangePlanRoom(@RequestBody ContractChangePlanRoomPo contractChangePlanRoomPo) {
        int saveFlag = 1;
        contractChangePlanRoomServiceDaoImpl.saveContractChangePlanRoomInfo(BeanConvertUtil.beanCovertMap(contractChangePlanRoomPo));
        return saveFlag;
    }

     @Override
    public int updateContractChangePlanRoom(@RequestBody  ContractChangePlanRoomPo contractChangePlanRoomPo) {
        int saveFlag = 1;
         contractChangePlanRoomServiceDaoImpl.updateContractChangePlanRoomInfo(BeanConvertUtil.beanCovertMap(contractChangePlanRoomPo));
        return saveFlag;
    }

     @Override
    public int deleteContractChangePlanRoom(@RequestBody  ContractChangePlanRoomPo contractChangePlanRoomPo) {
        int saveFlag = 1;
        contractChangePlanRoomPo.setStatusCd("1");
        contractChangePlanRoomServiceDaoImpl.updateContractChangePlanRoomInfo(BeanConvertUtil.beanCovertMap(contractChangePlanRoomPo));
        return saveFlag;
    }

    @Override
    public List<ContractChangePlanRoomDto> queryContractChangePlanRooms(@RequestBody  ContractChangePlanRoomDto contractChangePlanRoomDto) {

        //校验是否传了 分页信息

        int page = contractChangePlanRoomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractChangePlanRoomDto.setPage((page - 1) * contractChangePlanRoomDto.getRow());
        }

        List<ContractChangePlanRoomDto> contractChangePlanRooms = BeanConvertUtil.covertBeanList(contractChangePlanRoomServiceDaoImpl.getContractChangePlanRoomInfo(BeanConvertUtil.beanCovertMap(contractChangePlanRoomDto)), ContractChangePlanRoomDto.class);

        return contractChangePlanRooms;
    }


    @Override
    public int queryContractChangePlanRoomsCount(@RequestBody ContractChangePlanRoomDto contractChangePlanRoomDto) {
        return contractChangePlanRoomServiceDaoImpl.queryContractChangePlanRoomsCount(BeanConvertUtil.beanCovertMap(contractChangePlanRoomDto));    }

    public IContractChangePlanRoomServiceDao getContractChangePlanRoomServiceDaoImpl() {
        return contractChangePlanRoomServiceDaoImpl;
    }

    public void setContractChangePlanRoomServiceDaoImpl(IContractChangePlanRoomServiceDao contractChangePlanRoomServiceDaoImpl) {
        this.contractChangePlanRoomServiceDaoImpl = contractChangePlanRoomServiceDaoImpl;
    }
}
