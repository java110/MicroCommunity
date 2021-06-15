package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.goods.dao.IGroupBuyProductSpecServiceDao;
import com.java110.intf.goods.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 拼团产品规格内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GroupBuyProductSpecInnerServiceSMOImpl extends BaseServiceSMO implements IGroupBuyProductSpecInnerServiceSMO {

    @Autowired
    private IGroupBuyProductSpecServiceDao groupBuyProductSpecServiceDaoImpl;


    @Override
    public int saveGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo) {
        int saveFlag = 1;
        groupBuyProductSpecServiceDaoImpl.saveGroupBuyProductSpecInfo(BeanConvertUtil.beanCovertMap(groupBuyProductSpecPo));
        return saveFlag;
    }

    @Override
    public int updateGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo) {
        int saveFlag = 1;
        groupBuyProductSpecServiceDaoImpl.updateGroupBuyProductSpecInfo(BeanConvertUtil.beanCovertMap(groupBuyProductSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteGroupBuyProductSpec(@RequestBody GroupBuyProductSpecPo groupBuyProductSpecPo) {
        int saveFlag = 1;
        groupBuyProductSpecPo.setStatusCd("1");
        groupBuyProductSpecServiceDaoImpl.updateGroupBuyProductSpecInfo(BeanConvertUtil.beanCovertMap(groupBuyProductSpecPo));
        return saveFlag;
    }

    @Override
    public List<GroupBuyProductSpecDto> queryGroupBuyProductSpecs(@RequestBody GroupBuyProductSpecDto groupBuyProductSpecDto) {

        //校验是否传了 分页信息

        int page = groupBuyProductSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            groupBuyProductSpecDto.setPage((page - 1) * groupBuyProductSpecDto.getRow());
        }

        List<GroupBuyProductSpecDto> groupBuyProductSpecs = BeanConvertUtil.covertBeanList(groupBuyProductSpecServiceDaoImpl.getGroupBuyProductSpecInfo(BeanConvertUtil.beanCovertMap(groupBuyProductSpecDto)), GroupBuyProductSpecDto.class);

        return groupBuyProductSpecs;
    }


    @Override
    public int queryGroupBuyProductSpecsCount(@RequestBody GroupBuyProductSpecDto groupBuyProductSpecDto) {
        return groupBuyProductSpecServiceDaoImpl.queryGroupBuyProductSpecsCount(BeanConvertUtil.beanCovertMap(groupBuyProductSpecDto));
    }

    @Override
    public List<GroupBuyProductSpecDto> queryProductStockAndSales(GroupBuyProductSpecDto groupBuyProductSpecDto) {
        return BeanConvertUtil.covertBeanList(groupBuyProductSpecServiceDaoImpl.queryProductStockAndSales(BeanConvertUtil.beanCovertMap(groupBuyProductSpecDto)), GroupBuyProductSpecDto.class);
    }


    public IGroupBuyProductSpecServiceDao getGroupBuyProductSpecServiceDaoImpl() {
        return groupBuyProductSpecServiceDaoImpl;
    }

    public void setGroupBuyProductSpecServiceDaoImpl(IGroupBuyProductSpecServiceDao groupBuyProductSpecServiceDaoImpl) {
        this.groupBuyProductSpecServiceDaoImpl = groupBuyProductSpecServiceDaoImpl;
    }
}
