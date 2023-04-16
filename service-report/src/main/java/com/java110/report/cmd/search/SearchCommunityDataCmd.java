package com.java110.report.cmd.search;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.data.SearchDataDto;
import com.java110.report.bmo.search.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 模糊搜索，主要用于 不管在那个页面看到想要搜索的内容 就搜索一把，让操作简单化
 * <p>
 * add by wuxw 2023-04-16
 */

@Java110Cmd(serviceCode = "search.searchCommunityData")
public class SearchCommunityDataCmd extends Cmd {

    @Autowired
    private ISearchRoomBMO searchRoomBMOImpl;

    @Autowired
    private ISearchOwnerBMO searchOwnerBMOImpl;

    @Autowired
    private ISearchOwnerMemberBMO searchOwnerMemberBMOImpl;

    @Autowired
    private ISearchCarBMO searchCarBMOImpl;

    @Autowired
    private ISearchCarMemberBMO searchCarMemberBMOImpl;

    @Autowired
    private ISearchContractBMO searchContractBMOImpl;

    @Autowired
    private ISearchRepairBMO searchRepairBMOImpl;

    @Autowired
    private ISearchStaffBMO searchStaffBMOImpl;

    @Autowired
    private ISearchVisitBMO searchVisitBMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "searchValue", "未包含搜索内容");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        SearchDataDto searchDataDto = new SearchDataDto();
        searchDataDto.setCommunityId(reqJson.getString("communityId"));
        searchDataDto.setSearchValue(reqJson.getString("searchValue").trim());
        searchDataDto.setStoreId(storeId);
        if (StringUtil.isNumber(reqJson.getString("searchValue").trim())) {
            searchDataDto.setTel(reqJson.getString("searchValue").trim());
        }

        //todo 搜索房屋
        searchDataDto = searchRoomBMOImpl.query(searchDataDto);

        //todo 搜索业主
        searchDataDto = searchOwnerBMOImpl.query(searchDataDto);

        //todo 搜索业主成员
        searchDataDto = searchOwnerMemberBMOImpl.query(searchDataDto);

        //todo 搜索车辆
        searchDataDto = searchCarBMOImpl.query(searchDataDto);

        //todo 搜索车辆成员
        searchDataDto = searchCarMemberBMOImpl.query(searchDataDto);

        //todo 搜索合同
        searchDataDto = searchContractBMOImpl.query(searchDataDto);

        //todo 搜索报修单
        searchDataDto = searchRepairBMOImpl.query(searchDataDto);

        //todo 搜索员工
        searchDataDto = searchStaffBMOImpl.query(searchDataDto);

        //todo 搜索访客
        searchDataDto = searchVisitBMOImpl.query(searchDataDto);

        context.setResponseEntity(ResultVo.createResponseEntity(searchDataDto));

    }
}
