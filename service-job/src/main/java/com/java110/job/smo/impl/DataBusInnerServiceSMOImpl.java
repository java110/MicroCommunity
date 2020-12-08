package com.java110.job.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.entity.order.Business;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.job.adapt.IDatabusAdapt;
import com.java110.utils.cache.DatabusCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class DataBusInnerServiceSMOImpl extends BaseServiceSMO implements IDataBusInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(DataBusInnerServiceSMOImpl.class);

    public static final String DEFAULT_OPEN_DOOR_PROTOCOL = "ximoOpenDoorAdapt";//吸墨门禁


   // @Autowired
    private IDatabusAdapt databusAdaptImpl;

    @Override
    public boolean exchange(@RequestBody List<Business> businesses) {
        List<BusinessDatabusDto> databusDtos = DatabusCache.getDatabuss();
        for (Business business : businesses) {
            doExchange(business, businesses, databusDtos);
        }
        return false;
    }

    @Override
    public ResultVo openDoor(@RequestBody JSONObject reqJson) {
        databusAdaptImpl = ApplicationContextFactory.getBean(DEFAULT_OPEN_DOOR_PROTOCOL, IDatabusAdapt.class);
        return databusAdaptImpl.openDoor(reqJson);

    }

    /**
     * 处理业务类
     *
     * @param business    当前业务
     * @param businesses  全部业务
     * @param databusDtos databus
     */
    private void doExchange(Business business, List<Business> businesses, List<BusinessDatabusDto> databusDtos) {
        for (BusinessDatabusDto databusDto : databusDtos) {
            try {
                if (business.getBusinessTypeCd().equals(databusDto.getBusinessTypeCd())) {
                    databusAdaptImpl = ApplicationContextFactory.getBean(databusDto.getBeanName(), IDatabusAdapt.class);
                    databusAdaptImpl.execute(business, businesses);
                }
            } catch (Exception e) {
                logger.error("执行databus失败", e);
            }
        }
    }
}
