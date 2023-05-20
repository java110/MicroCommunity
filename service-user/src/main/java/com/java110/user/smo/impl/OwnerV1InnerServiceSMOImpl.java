/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.smo.impl;


import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.user.dao.IOwnerV1ServiceDao;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-09-14 18:56:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class OwnerV1InnerServiceSMOImpl extends BaseServiceSMO implements IOwnerV1InnerServiceSMO {

    @Autowired
    private IOwnerV1ServiceDao ownerV1ServiceDaoImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public int saveOwner(@RequestBody OwnerPo ownerPo) {
        int saveFlag = ownerV1ServiceDaoImpl.saveOwnerInfo(BeanConvertUtil.beanCovertMap(ownerPo));

        //业主 开通 现金账户，不然配合商城 会存在bug
        addAccountDto(ownerPo.getMemberId(), ownerPo.getCommunityId());
        return saveFlag;
    }

    private void addAccountDto(String ownerId, String communityId) {
        if (StringUtil.isEmpty(ownerId)) {
            return;
        }

        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + "AddCountDto" + ownerId;
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            AccountPo accountPo = new AccountPo();
            accountPo.setAmount("0");
            accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
            accountPo.setObjId(ownerId);
            accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
            accountPo.setAcctType(AccountDto.ACCT_TYPE_CASH);
            OwnerDto tmpOwnerDto = new OwnerDto();
            tmpOwnerDto.setMemberId(ownerId);
            tmpOwnerDto.setCommunityId(communityId);
            List<OwnerDto> ownerDtos = queryOwners(tmpOwnerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            accountPo.setAcctName(ownerDtos.get(0).getName());
            accountPo.setPartId(communityId);
            accountPo.setLink(ownerDtos.get(0).getLink());
            accountInnerServiceSMOImpl.saveAccount(accountPo);
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    @Override
    public int updateOwner(@RequestBody OwnerPo ownerPo) {
        int saveFlag = ownerV1ServiceDaoImpl.updateOwnerInfo(BeanConvertUtil.beanCovertMap(ownerPo));
        return saveFlag;
    }

    @Override
    public int deleteOwner(@RequestBody OwnerPo ownerPo) {
        ownerPo.setStatusCd("1");
        int saveFlag = ownerV1ServiceDaoImpl.updateOwnerInfo(BeanConvertUtil.beanCovertMap(ownerPo));
        return saveFlag;
    }

    @Override

    public List<OwnerDto>  queryOwners(@RequestBody OwnerDto ownerDto) {

        //校验是否传了 分页信息

        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        List<OwnerDto> owners = BeanConvertUtil.covertBeanList(ownerV1ServiceDaoImpl.getOwnerInfo(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);

        return owners;
    }


    @Override
    public int queryOwnersCount(@RequestBody OwnerDto ownerDto) {
        return ownerV1ServiceDaoImpl.queryOwnersCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public int queryOwnersBindCount(@RequestBody OwnerDto ownerDto) {
        return ownerV1ServiceDaoImpl.queryOwnersBindCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public List<Map> queryOwnerMembersCount(@RequestBody List<String> ownerIds) {

        Map info = new HashMap();
        info.put("ownerIds", ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> result = ownerV1ServiceDaoImpl.queryOwnerMembersCount(info);
        return result;
    }

}
