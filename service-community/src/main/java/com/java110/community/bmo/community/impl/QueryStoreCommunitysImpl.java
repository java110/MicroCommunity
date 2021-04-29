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
package com.java110.community.bmo.community.impl;

import com.java110.community.bmo.community.IQueryStoreCommunitys;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户小区 查询实现类
 * <p>
 * add by 吴学文 2020-12-23
 * <p>
 * 文档参考：http://www.homecommunity.cn
 */
@Service
public class QueryStoreCommunitysImpl implements IQueryStoreCommunitys {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(CommunityMemberDto communityMemberDto) {
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.getStoreCommunitys(communityMemberDto);
        return ResultVo.createResponseEntity(communityDtos);
    }
}
