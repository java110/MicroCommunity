package com.java110.user.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.PageDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerPo;
import com.java110.user.dao.IOwnerServiceDao;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.OwnerTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerInnerServiceSMO {

    @Autowired
    private IOwnerServiceDao ownerServiceDaoImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public List<OwnerDto> queryOwners(@RequestBody OwnerDto ownerDto) {

        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        Map ownerInfo = BeanConvertUtil.beanCovertMap(ownerDto);
        ownerInfo.put("communityId", ownerDto.getCommunityId());
        ownerInfo.put("ownerTypeCd", OwnerTypeConstant.OWNER);
        // ownerInfo.put("ownerIds", getOwnerIds(communityMemberDtos));
        //ownerInfo.put("ownerTypeCd", ownerDto.getOwnerTypeCd());
        // ownerInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);

        List<OwnerDto> owners = BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.getOwnerInfo(ownerInfo), OwnerDto.class);

        if (owners == null || owners.size() == 0) {
            return owners;
        }

        String[] userIds = getUserIds(owners);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
        String[] memberIds = getMemberIds(owners);
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberIds(memberIds);
        ownerAttrDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        for (OwnerDto owner : owners) {
            refreshOwner(owner, users, ownerAttrDtos);
        }

        updateOwnerPhone(owners);
        return owners;
    }

    private boolean updateOwnerPhone(List<OwnerDto> owners) {
        if (owners.size() > 15) {
            return true;
        }

        List<String> memberIds = new ArrayList<>();

        for (OwnerDto tmpOwnerDto : owners) {
            memberIds.add(tmpOwnerDto.getMemberId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        //fileRelDto.setObjId(owners.get(0).getMemberId());
        fileRelDto.setObjIds(memberIds.toArray(new String[memberIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return true;
        }

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");

        for (OwnerDto tmpOwnerDto : owners) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!tmpOwnerDto.getMemberId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }

                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    tmpOwnerDto.setUrl(tmpFileRelDto.getFileSaveName());
                } else {
                    tmpOwnerDto.setUrl(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }

        return false;
    }

    @Override
    public List<OwnerDto> queryOwnerMembers(@RequestBody OwnerDto ownerDto) {
        List<OwnerDto> owners = BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.getOwnerInfo(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
        if (owners == null || owners.size() == 0) {
            return owners;
        }

        String[] userIds = getUserIds(owners);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
        String[] memberIds = getMemberIds(owners);
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberIds(memberIds);
        ownerAttrDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        for (OwnerDto owner : owners) {
            refreshOwner(owner, users, ownerAttrDtos);
        }

        updateOwnerPhone(owners);
        return owners;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param owner 小区业主信息
     * @param users 用户列表
     */
    private void refreshOwner(OwnerDto owner, List<UserDto> users, List<OwnerAttrDto> ownerAttrDtos) {
        for (UserDto user : users) {
            if (owner.getUserId().equals(user.getUserId())) {
                //BeanConvertUtil.covertBean(user, owner);
                owner.setUserName(user.getUserName());
                break;
            }
        }

        if (ownerAttrDtos == null || ownerAttrDtos.size() < 1) {
            return;
        }
        List<OwnerAttrDto> tmpOwnerAttrDtos = new ArrayList<>();
        for (OwnerAttrDto ownerAttrDto : ownerAttrDtos) {
            if (ownerAttrDto.getMemberId().equals(owner.getMemberId())) {
                tmpOwnerAttrDtos.add(ownerAttrDto);
            }
        }

        owner.setOwnerAttrDtos(tmpOwnerAttrDtos);
    }

    /**
     * 获取批量userId
     *
     * @param communityMemberDtos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getOwnerIds(List<CommunityMemberDto> communityMemberDtos) {
        List<String> ownerIds = new ArrayList<String>();
        for (CommunityMemberDto communityMemberDto : communityMemberDtos) {
            ownerIds.add(communityMemberDto.getMemberId());
        }

        return ownerIds.toArray(new String[ownerIds.size()]);
    }

    /**
     * 获取批量userId
     *
     * @param owners 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerDto> owners) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerDto owner : owners) {
            userIds.add(owner.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    /**
     * 获取批量userId
     *
     * @param owners 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getMemberIds(List<OwnerDto> owners) {
        List<String> memberIds = new ArrayList<String>();
        for (OwnerDto owner : owners) {
            memberIds.add(owner.getMemberId());
        }

        return memberIds.toArray(new String[memberIds.size()]);
    }

    @Override
    public int queryOwnersCount(@RequestBody OwnerDto ownerDto) {

        //调用 小区服务查询 小区成员业主信息
        /*CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(ownerDto.getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.OWNER);
        return communityInnerServiceSMOImpl.getCommunityMemberCount(communityMemberDto);*/
        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        Map ownerInfo = BeanConvertUtil.beanCovertMap(ownerDto);
        ownerInfo.put("communityId", ownerDto.getCommunityId());
        ownerInfo.put("ownerTypeCd", OwnerTypeConstant.OWNER);
        // ownerInfo.put("ownerIds", getOwnerIds(communityMemberDtos));
        //ownerInfo.put("ownerTypeCd", ownerDto.getOwnerTypeCd());
        ownerInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);

        return ownerServiceDaoImpl.getOwnerInfoCount(ownerInfo);

    }

    @Override
    public int queryOwnerCountByCondition(@RequestBody OwnerDto ownerDto) {

        //校验是否传了 分页信息

//        int page = ownerDto.getPage();
//
//        if (page != PageDto.DEFAULT_PAGE) {
//            ownerDto.setPage((page - 1) * ownerDto.getRow());
//        }
        return ownerServiceDaoImpl.queryOwnersCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public List<OwnerDto> queryOwnersByCondition(@RequestBody OwnerDto ownerDto) {
//校验是否传了 分页信息

        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }
        List<OwnerDto> owners = BeanConvertUtil.covertBeanList(
                ownerServiceDaoImpl.getOwnerInfoByCondition(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
        if (owners == null || owners.size() == 0) {
            return owners;
        }

        String[] userIds = getUserIds(owners);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        String[] memberIds = getMemberIds(owners);
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberIds(memberIds);
        ownerAttrDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        for (OwnerDto owner : owners) {
            refreshOwner(owner, users, ownerAttrDtos);
        }
        updateOwnerPhone(owners);
        return owners;
    }


    @Override
    public int queryNoEnterRoomOwnerCount(@RequestBody OwnerDto ownerDto) {
        return ownerServiceDaoImpl.queryNoEnterRoomOwnerCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public List<OwnerDto> queryOwnersByRoom(@RequestBody OwnerDto ownerDto) {
        return BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.queryOwnersByRoom(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
    }

    @Override
    public List<OwnerDto> queryOwnersByParkingSpace(@RequestBody OwnerDto ownerDto) {
        return BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.queryOwnersByParkingSpace(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
    }

    @Override
    public int updateOwnerMember(OwnerPo ownerPo) {
        Map info = BeanConvertUtil.beanCovertMap(ownerPo);
        info.put("statusCd", "0");
        ownerServiceDaoImpl.updateOwnerInfoInstance(info);
        return 1;
    }

    @Override
    public int queryOwnerLogsCountByRoom(@RequestBody OwnerDto ownerDto) {
        return ownerServiceDaoImpl.queryOwnerLogsCountByRoom(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public List<OwnerDto> queryOwnerLogsByRoom(@RequestBody OwnerDto ownerDto) {
        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }
        return BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.queryOwnerLogsByRoom(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
    }

    @Override
    public List<OwnerDto> queryAllOwners(@RequestBody OwnerDto ownerDto) {
        List<OwnerDto> allOwners = BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.getOwnerInfo(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);
        return allOwners;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    public IOwnerServiceDao getOwnerServiceDaoImpl() {
        return ownerServiceDaoImpl;
    }

    public void setOwnerServiceDaoImpl(IOwnerServiceDao ownerServiceDaoImpl) {
        this.ownerServiceDaoImpl = ownerServiceDaoImpl;
    }

}
