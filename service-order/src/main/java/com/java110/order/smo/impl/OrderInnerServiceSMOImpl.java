package com.java110.order.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.order.BusinessDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Override
    public List<OrderDto> queryOrderByBusinessType(@RequestBody OrderDto orderDto) {
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryOrderByBusinessType(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);
    }

    public List<OrderDto> queryOrderByBId(@RequestBody BusinessDto businessDto) {
        List<OrderDto> orderDtos = BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryOrderByBId(BeanConvertUtil.beanCovertMap(businessDto)), OrderDto.class);

        if (orderDtos == null || orderDtos.size() < 1) {
            return orderDtos;
        }

        String[] userIds = getUserIds(orderDtos);
        if(userIds == null || userIds.length <1){
            return orderDtos;
        }
        if(userIds != null && userIds.length > 0) {
            //根据 userId 查询用户信息
            List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
            for (OrderDto orderDto : orderDtos) {
                for (UserDto userDto : users) {
                    if (orderDto.getUserId().equals(userDto.getUserId())) {
                        orderDto.setUserName(userDto.getUserName());
                        break;
                    }
                }
            }
        }
        return orderDtos;
    }

    /**
     * 获取批量userId
     *
     * @param orderDtos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OrderDto> orderDtos) {
        List<String> userIds = new ArrayList<String>();
        for (OrderDto orderDto : orderDtos) {

            if (StringUtil.isEmpty(orderDto.getUserId()) || orderDto.getUserId().startsWith("-")) {
                continue;
            }
            userIds.add(orderDto.getUserId());

        }

        return userIds.toArray(new String[userIds.size()]);
    }


    public int updateBusinessStatusCd(@RequestBody OrderDto orderDto) {
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
    public List<OrderDto> queryApplicationKeyOrders(@RequestBody OrderDto orderDto) {
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.queryApplicationKeyOrders(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);
    }

    /**
     * 查询 同订单 订单项
     *
     * @param businessDto
     * @return
     */
    public List<BusinessDto> querySameOrderBusiness(@RequestBody BusinessDto businessDto) {
        return BeanConvertUtil.covertBeanList(centerServiceDAOImpl.querySameOrderBusiness(BeanConvertUtil.beanCovertMap(businessDto)), BusinessDto.class);
    }


    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
