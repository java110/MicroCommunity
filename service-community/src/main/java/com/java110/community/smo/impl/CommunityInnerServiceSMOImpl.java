package com.java110.community.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.community.CommunityPo;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区服务内部类
 */
@RestController
public class CommunityInnerServiceSMOImpl extends BaseServiceSMO implements ICommunityInnerServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(CommunityServiceSMOImpl.class);


    @Autowired
    private ICommunityServiceDao communityServiceDaoImpl;


    @Override
    public List<CommunityMemberDto> getCommunityMembers(@RequestBody CommunityMemberDto communityMemberDto) {

        logger.debug("communityMemberDto：{}", JSONObject.toJSONString(communityMemberDto));

        //校验是否传了 分页信息

        int page = communityMemberDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityMemberDto.setPage((page - 1) * communityMemberDto.getRow());
        }

        List<Map> communityMembers = communityServiceDaoImpl.getCommunityMembers(BeanConvertUtil.beanCovertMap(communityMemberDto));
        return BeanConvertUtil.covertBeanList(communityMembers, CommunityMemberDto.class);
    }

    @Override
    public List<CommunityDto> getStoreCommunitys(CommunityMemberDto communityMemberDto) {
        List<Map> communityMembers = communityServiceDaoImpl.getStoreCommunitys(BeanConvertUtil.beanCovertMap(communityMemberDto));
        List<CommunityDto> communitys = BeanConvertUtil.covertBeanList(communityMembers, CommunityDto.class);

        List<String> communityIds = new ArrayList<>();

        if (communitys == null || communitys.size() < 1) {
            return communitys;
        }
        for (CommunityDto tmpCommunityDto : communitys) {
            communityIds.add(tmpCommunityDto.getCommunityId());
        }
        Map info = new HashMap();
        info.put("communityIds", communityIds.toArray(new String[communityIds.size()]));
        List<CommunityAttrDto> communityAttrDtos = BeanConvertUtil.covertBeanList(communityServiceDaoImpl.getCommunityAttrs(info), CommunityAttrDto.class);

        if (communityAttrDtos == null || communityAttrDtos.size() < 1) {
            return communitys;
        }
        for (CommunityDto tmpCommunityDto : communitys) {
            List<CommunityAttrDto> tmpCommunityAttrDtos = new ArrayList<>();
            for (CommunityAttrDto communityAttrDto : communityAttrDtos) {
                if (tmpCommunityDto.getCommunityId().equals(communityAttrDto.getCommunityId())) {
                    tmpCommunityAttrDtos.add(communityAttrDto);
                }
            }
            tmpCommunityDto.setCommunityAttrDtos(tmpCommunityAttrDtos);
        }
        return communitys;
    }

    @Override
    public int getCommunityMemberCount(@RequestBody CommunityMemberDto communityMemberDto) {
        logger.debug("getCommunityMemberCount：{}", JSONObject.toJSONString(communityMemberDto));

        return communityServiceDaoImpl.getCommunityMemberCount(BeanConvertUtil.beanCovertMap(communityMemberDto));
    }

    @Override
    public List<CommunityAttrDto> getCommunityAttrs(@RequestBody CommunityAttrDto communityAttrDto) {
        return BeanConvertUtil.covertBeanList(communityServiceDaoImpl.getCommunityAttrs(BeanConvertUtil.beanCovertMap(communityAttrDto)), CommunityAttrDto.class);
    }

    @Override
    public int getCommunityAttrsCount(@RequestBody CommunityAttrDto communityAttrDto) {
        logger.debug("queryCommunityAttrsCount：{}", JSONObject.toJSONString(communityAttrDto));

        return communityServiceDaoImpl.getCommunityAttrsCount(BeanConvertUtil.beanCovertMap(communityAttrDto));
    }

    /**
     * 保存小区属性
     * @param communityAttrPo 数据对象分享
     * @return
     */
    @Override
    public int saveCommunityAttr(CommunityAttrPo communityAttrPo) {
        return communityServiceDaoImpl.saveCommunityAttr(BeanConvertUtil.beanCovertMap(communityAttrPo));
    }

    @Override
    public List<CommunityDto> queryCommunitys(@RequestBody CommunityDto communityDto) {

        //校验是否传了 分页信息

        int page = communityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityDto.setPage((page - 1) * communityDto.getRow());
        }

        List<CommunityDto> communitys = BeanConvertUtil.covertBeanList(communityServiceDaoImpl.getCommunityInfoNew(BeanConvertUtil.beanCovertMap(communityDto)), CommunityDto.class);

        List<String> communityIds = new ArrayList<>();

        if (communitys == null || communitys.size() < 1) {
            return communitys;
        }
        for (CommunityDto tmpCommunityDto : communitys) {
            communityIds.add(tmpCommunityDto.getCommunityId());
        }
        Map info = new HashMap();
        info.put("communityIds", communityIds.toArray(new String[communityIds.size()]));
        List<CommunityAttrDto> communityAttrDtos = BeanConvertUtil.covertBeanList(communityServiceDaoImpl.getCommunityAttrs(info), CommunityAttrDto.class);

        if (communityAttrDtos == null || communityAttrDtos.size() < 1) {
            return communitys;
        }


        for (CommunityDto tmpCommunityDto : communitys) {
            List<CommunityAttrDto> tmpCommunityAttrDtos = new ArrayList<>();
            for (CommunityAttrDto communityAttrDto : communityAttrDtos) {
                if (tmpCommunityDto.getCommunityId().equals(communityAttrDto.getCommunityId())) {
                    tmpCommunityAttrDtos.add(communityAttrDto);
                }
            }
            tmpCommunityDto.setCommunityAttrDtos(tmpCommunityAttrDtos);

        }

        return communitys;
    }


    @Override
    public int queryCommunitysCount(@RequestBody CommunityDto communityDto) {
        return communityServiceDaoImpl.queryCommunitysCount(BeanConvertUtil.beanCovertMap(communityDto));
    }

//    @Override
//    public int saveCommunity(@RequestBody CommunityPo communityPo) {
//        return communityServiceDaoImpl.saveCommunity(BeanConvertUtil.beanCovertMap(communityPo));
//    }


    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
