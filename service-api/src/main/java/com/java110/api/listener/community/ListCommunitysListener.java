package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.dto.area.AreaDto;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listCommunitysListener")
public class ListCommunitysListener extends AbstractServiceApiListener {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_COMMUNITYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);

        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<ApiCommunityDataVo> communitys = null;

        if (count > 0) {
            communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);

            refreshCommunityCity(communitys);
        } else {
            communitys = new ArrayList<>();
        }


        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 刷新cityName
     *
     * @param communitys
     */
    private void refreshCommunityCity(List<ApiCommunityDataVo> communitys) {
        List<String> areaCodes = new ArrayList<>();
        for (ApiCommunityDataVo communityDataVo : communitys) {
            areaCodes.add(communityDataVo.getCityCode());
        }
        if(areaCodes.size() > 0){
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaCodes(areaCodes.toArray(new String[areaCodes.size()]));
            List<AreaDto> areaDtos = areaInnerServiceSMOImpl.getProvCityArea(areaDto);
            for (ApiCommunityDataVo communityDataVo : communitys) {
                for (AreaDto tmpAreaDto : areaDtos) {
                    if (communityDataVo.getCityCode().equals(tmpAreaDto.getAreaCode())) {
                        communityDataVo.setCityName(tmpAreaDto.getProvName() + tmpAreaDto.getCityName() + tmpAreaDto.getAreaName());
                        continue;
                    }
                    communityDataVo.setCityName("未知");
                }
            }
        }
    }

    public IAreaInnerServiceSMO getAreaInnerServiceSMOImpl() {
        return areaInnerServiceSMOImpl;
    }

    public void setAreaInnerServiceSMOImpl(IAreaInnerServiceSMO areaInnerServiceSMOImpl) {
        this.areaInnerServiceSMOImpl = areaInnerServiceSMOImpl;
    }
}
