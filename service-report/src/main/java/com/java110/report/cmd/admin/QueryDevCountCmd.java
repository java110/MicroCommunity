package com.java110.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.dto.mapping.MappingDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.service.RouteDto;
import com.java110.dto.service.ServiceDto;
import com.java110.dto.shop.ShopDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.*;
import com.java110.intf.dev.ITaskV1InnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.mall.IShopInnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "admin.queryDevCount")
public class QueryDevCountCmd extends Cmd {


    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Autowired
    private ITaskV1InnerServiceSMO taskV1InnerServiceSMOImpl;

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_DEV);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统开发者账户操作");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<Map> datas = new ArrayList<>();
        //todo 应用数

        AppDto appDto = new AppDto();
        int appCount = appInnerServiceSMOImpl.queryAppsCount(appDto);
        setDatas(datas, "应用数", appCount);

        //todo 接口数
        ServiceDto serviceDto = new ServiceDto();
        int storeCount = serviceInnerServiceSMOImpl.queryServicesCount(serviceDto);
        setDatas(datas, "接口数", storeCount);

        //todo 服务注册
        RouteDto routeDto = new RouteDto();
        int routeCount =  routeInnerServiceSMOImpl.queryRoutesCount(routeDto);
        setDatas(datas, "服务注册数", routeCount);

        //todo 定时任务
        TaskDto taskDto = new TaskDto();
        int taskCount = taskV1InnerServiceSMOImpl.queryTasksCount(taskDto);
        setDatas(datas, "任务总数", taskCount);

        //todo 启用定时任务
         taskDto = new TaskDto();
         taskDto.setState("002");
         taskCount = taskV1InnerServiceSMOImpl.queryTasksCount(taskDto);
        setDatas(datas, "启用任务数", taskCount);

        //todo 房屋数
        MappingDto mappingDto = new MappingDto();
        int mappingCount = mappingInnerServiceSMOImpl.queryMappingsCount(mappingDto);
        setDatas(datas, "配置数", mappingCount);

        context.setResponseEntity(ResultVo.createResponseEntity(datas));

    }

    private void setDatas(List<Map> datas, String name, int value) {
        Map info = new HashMap();
        info.put("name", name);
        info.put("value", value);
        datas.add(info);
    }
}
