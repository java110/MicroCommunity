package com.java110.order.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.order.IOrderInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.order.OrderDto;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 组织内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OrderInnerServiceSMOImpl extends BaseServiceSMO implements IOrderInnerServiceSMO {

    @Autowired
    private ICenterServiceDAO centerServiceDAOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OrderDto> queryOrders(@RequestBody OrderDto orderDto) {

        //校验是否传了 分页信息

        /*int page = orderDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orderDto.setPage((page - 1) * orderDto.getRow());
        }

        List<OrderDto> orders = BeanConvertUtil.covertBeanList(centerServiceDAOImpl.(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);


        return orders;*/
        return null;
    }


    @Override
    public int queryOrdersCount(@RequestBody OrderDto orderDto) {
        //return orderServiceDaoImpl.queryOrdersCount(BeanConvertUtil.beanCovertMap(orderDto));
        return 0;
    }

    @Override
    public List<OrderDto> queryOwenrOrders(@RequestBody OrderDto orderDto) {
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryOwenrOrders(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);
    }

    public int updateBusinessStatusCd(@RequestBody OrderDto orderDto){
        return centerServiceDAOImpl.updateBusinessStatusCd(BeanConvertUtil.beanCovertMap(orderDto));
    }

    @Override
    public List<OrderDto> queryMachineOrders(@RequestBody OrderDto orderDto) {
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryManchineOrders(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);
    }

    /**
     * <p>查询上级组织信息</p>
     *
     * @param orderDto 数据对象分享
     * @return OrderDto 对象数据
     */
    public List<OrderDto> queryApplicationKeyOrders(@RequestBody OrderDto orderDto){
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryApplicationKeyOrders(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);
    }


    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
