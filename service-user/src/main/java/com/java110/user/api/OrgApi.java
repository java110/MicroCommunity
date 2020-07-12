package com.java110.user.api;

import com.java110.dto.org.OrgDto;
import com.java110.intf.user.org.IOrgApi;
import com.java110.user.service.org.IQueryOrgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName OrgApi
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/12 20:01
 * @Version 1.0
 * add by wuxw 2020/7/12
 **/
@RestController
public class OrgApi implements IOrgApi {


    @Autowired
    private IQueryOrgsService queryOrgsServiceImpl;

    @Override
    public List<OrgDto> queryOrgs(@RequestParam(value = "orgId", required = false) String orgId,
                                  @RequestParam(value = "staffId", required = false) String staffId) {
        OrgDto orgDto = new OrgDto();
        orgDto.setOrgId(orgId);
        orgDto.setStaffId(staffId);
        List<OrgDto> orgDtos = queryOrgsServiceImpl.queryOrgs(orgDto);

        return orgDtos;
    }
}
