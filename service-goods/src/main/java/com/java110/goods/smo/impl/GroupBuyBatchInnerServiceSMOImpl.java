package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.groupBuyBatch.GroupBuyBatchDto;
import com.java110.goods.dao.IGroupBuyBatchServiceDao;
import com.java110.intf.goods.IGroupBuyBatchInnerServiceSMO;
import com.java110.po.groupBuyBatch.GroupBuyBatchPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 拼团批次内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GroupBuyBatchInnerServiceSMOImpl extends BaseServiceSMO implements IGroupBuyBatchInnerServiceSMO {

    @Autowired
    private IGroupBuyBatchServiceDao groupBuyBatchServiceDaoImpl;


    @Override
    public int saveGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo) {
        int saveFlag = 1;
        groupBuyBatchServiceDaoImpl.saveGroupBuyBatchInfo(BeanConvertUtil.beanCovertMap(groupBuyBatchPo));
        return saveFlag;
    }

    @Override
    public int updateGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo) {
        int saveFlag = 1;
        groupBuyBatchServiceDaoImpl.updateGroupBuyBatchInfo(BeanConvertUtil.beanCovertMap(groupBuyBatchPo));
        return saveFlag;
    }

    @Override
    public int deleteGroupBuyBatch(@RequestBody GroupBuyBatchPo groupBuyBatchPo) {
        int saveFlag = 1;
        groupBuyBatchPo.setStatusCd("1");
        groupBuyBatchServiceDaoImpl.updateGroupBuyBatchInfo(BeanConvertUtil.beanCovertMap(groupBuyBatchPo));
        return saveFlag;
    }

    @Override
    public List<GroupBuyBatchDto> queryGroupBuyBatchs(@RequestBody GroupBuyBatchDto groupBuyBatchDto) {

        //校验是否传了 分页信息

        int page = groupBuyBatchDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            groupBuyBatchDto.setPage((page - 1) * groupBuyBatchDto.getRow());
        }

        List<GroupBuyBatchDto> groupBuyBatchs = BeanConvertUtil.covertBeanList(groupBuyBatchServiceDaoImpl.getGroupBuyBatchInfo(BeanConvertUtil.beanCovertMap(groupBuyBatchDto)), GroupBuyBatchDto.class);

        return groupBuyBatchs;
    }


    @Override
    public int queryGroupBuyBatchsCount(@RequestBody GroupBuyBatchDto groupBuyBatchDto) {
        return groupBuyBatchServiceDaoImpl.queryGroupBuyBatchsCount(BeanConvertUtil.beanCovertMap(groupBuyBatchDto));
    }

    public IGroupBuyBatchServiceDao getGroupBuyBatchServiceDaoImpl() {
        return groupBuyBatchServiceDaoImpl;
    }

    public void setGroupBuyBatchServiceDaoImpl(IGroupBuyBatchServiceDao groupBuyBatchServiceDaoImpl) {
        this.groupBuyBatchServiceDaoImpl = groupBuyBatchServiceDaoImpl;
    }
}
