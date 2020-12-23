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
package com.java110.community.api;

import com.java110.community.bmo.community.IQueryStoreCommunitys;
import com.java110.dto.CommunityMemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小区相关对外提供接口类
 * <p>
 * 功能包括：
 * 商户小区查询接口
 *
 * @desc add by 吴学文 8:30
 * <p>
 * 文档参考 ： http://www.homecommunity.cn/
 */
@RestController
@RequestMapping(value = "/communitys")
public class CommunitysApi {

    @Autowired
    private IQueryStoreCommunitys queryStoreCommunitysImpl;


    /**
     * 查询商户入驻小区
     *
     * @param memberId
     * @param memberTypeCd
     * @param auditStatusCd
     * @return
     * @ServiceCode /communitys/queryStoreCommunitys
     * @Path /app/communitys/queryStoreCommunitys
     */
    @RequestMapping(value = "/queryStoreCommunitys", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreCommunitys(@RequestParam(value = "memberId") String memberId,
                                                       @RequestParam(value = "memberTypeCd") String memberTypeCd,
                                                       @RequestParam(value = "auditStatusCd", required = false) String auditStatusCd) {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(memberId);
        communityMemberDto.setMemberTypeCd(memberTypeCd);
        communityMemberDto.setAuditStatusCd(auditStatusCd);
        return queryStoreCommunitysImpl.query(communityMemberDto);
    }
}
