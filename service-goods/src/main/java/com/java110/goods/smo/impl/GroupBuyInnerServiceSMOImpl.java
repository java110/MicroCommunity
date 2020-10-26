package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.groupBuy.GroupBuyDto;
import com.java110.goods.dao.IGroupBuyServiceDao;
import com.java110.intf.goods.IGroupBuyInnerServiceSMO;
import com.java110.po.groupBuy.GroupBuyPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 拼团购买内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GroupBuyInnerServiceSMOImpl extends BaseServiceSMO implements IGroupBuyInnerServiceSMO {

    @Autowired
    private IGroupBuyServiceDao groupBuyServiceDaoImpl;


    @Override
    public int saveGroupBuy(@RequestBody GroupBuyPo groupBuyPo) {
        int saveFlag = 1;
        groupBuyServiceDaoImpl.saveGroupBuyInfo(BeanConvertUtil.beanCovertMap(groupBuyPo));
        return saveFlag;
    }

    @Override
    public int updateGroupBuy(@RequestBody GroupBuyPo groupBuyPo) {
        int saveFlag = 1;
        groupBuyServiceDaoImpl.updateGroupBuyInfo(BeanConvertUtil.beanCovertMap(groupBuyPo));
        return saveFlag;
    }

    @Override
    public int deleteGroupBuy(@RequestBody GroupBuyPo groupBuyPo) {
        int saveFlag = 1;
        groupBuyPo.setStatusCd("1");
        groupBuyServiceDaoImpl.updateGroupBuyInfo(BeanConvertUtil.beanCovertMap(groupBuyPo));
        return saveFlag;
    }

    @Override
    public List<GroupBuyDto> queryGroupBuys(@RequestBody GroupBuyDto groupBuyDto) {

        //校验是否传了 分页信息

        int page = groupBuyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            groupBuyDto.setPage((page - 1) * groupBuyDto.getRow());
        }

        List<GroupBuyDto> groupBuys = BeanConvertUtil.covertBeanList(groupBuyServiceDaoImpl.getGroupBuyInfo(BeanConvertUtil.beanCovertMap(groupBuyDto)), GroupBuyDto.class);

        return groupBuys;
    }


    @Override
    public int queryGroupBuysCount(@RequestBody GroupBuyDto groupBuyDto) {
        return groupBuyServiceDaoImpl.queryGroupBuysCount(BeanConvertUtil.beanCovertMap(groupBuyDto));
    }

    public IGroupBuyServiceDao getGroupBuyServiceDaoImpl() {
        return groupBuyServiceDaoImpl;
    }

    public void setGroupBuyServiceDaoImpl(IGroupBuyServiceDao groupBuyServiceDaoImpl) {
        this.groupBuyServiceDaoImpl = groupBuyServiceDaoImpl;
    }
}
