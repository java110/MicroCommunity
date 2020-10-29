package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.goods.dao.IGroupBuyProductServiceDao;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 拼团产品内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GroupBuyProductInnerServiceSMOImpl extends BaseServiceSMO implements IGroupBuyProductInnerServiceSMO {

    @Autowired
    private IGroupBuyProductServiceDao groupBuyProductServiceDaoImpl;


    @Override
    public int saveGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo) {
        int saveFlag = 1;
        groupBuyProductServiceDaoImpl.saveGroupBuyProductInfo(BeanConvertUtil.beanCovertMap(groupBuyProductPo));
        return saveFlag;
    }

    @Override
    public int updateGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo) {
        int saveFlag = 1;
        groupBuyProductServiceDaoImpl.updateGroupBuyProductInfo(BeanConvertUtil.beanCovertMap(groupBuyProductPo));
        return saveFlag;
    }

    @Override
    public int deleteGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo) {
        int saveFlag = 1;
        groupBuyProductPo.setStatusCd("1");
        groupBuyProductServiceDaoImpl.updateGroupBuyProductInfo(BeanConvertUtil.beanCovertMap(groupBuyProductPo));
        return saveFlag;
    }

    @Override
    public List<GroupBuyProductDto> queryGroupBuyProducts(@RequestBody GroupBuyProductDto groupBuyProductDto) {

        //校验是否传了 分页信息

        int page = groupBuyProductDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            groupBuyProductDto.setPage((page - 1) * groupBuyProductDto.getRow());
        }

        List<Map> groupBuyProductMaps =  groupBuyProductServiceDaoImpl.getGroupBuyProductInfo(BeanConvertUtil.beanCovertMap(groupBuyProductDto));
        List<GroupBuyProductDto> groupBuyProductDtos = new ArrayList<>();
        GroupBuyProductDto tmpGroupBuyProductDto = null;
        GroupBuyProductSpecDto groupBuyProductSpecDto = null;
        for(Map groupBuyProductMap: groupBuyProductMaps){
            tmpGroupBuyProductDto = BeanConvertUtil.covertBean(groupBuyProductMap,GroupBuyProductDto.class);
            groupBuyProductSpecDto = BeanConvertUtil.covertBean(groupBuyProductMap,GroupBuyProductSpecDto.class);
            tmpGroupBuyProductDto.setDefaultGroupBuyProductSpec(groupBuyProductSpecDto);
            groupBuyProductDtos.add(tmpGroupBuyProductDto);
        }


        return groupBuyProductDtos;
    }


    @Override
    public int queryGroupBuyProductsCount(@RequestBody GroupBuyProductDto groupBuyProductDto) {
        return groupBuyProductServiceDaoImpl.queryGroupBuyProductsCount(BeanConvertUtil.beanCovertMap(groupBuyProductDto));
    }

    public IGroupBuyProductServiceDao getGroupBuyProductServiceDaoImpl() {
        return groupBuyProductServiceDaoImpl;
    }

    public void setGroupBuyProductServiceDaoImpl(IGroupBuyProductServiceDao groupBuyProductServiceDaoImpl) {
        this.groupBuyProductServiceDaoImpl = groupBuyProductServiceDaoImpl;
    }
}
