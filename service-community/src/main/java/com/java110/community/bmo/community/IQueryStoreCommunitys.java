package com.java110.community.bmo.community;/*
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

import com.java110.dto.CommunityMemberDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询 商户入驻成功的小区信息
 * <p>
 * add by 吴学文 2020-12-23
 * <p>
 * 文档参考：http://www.homecommunity.cn
 */
public interface IQueryStoreCommunitys {

    /**
     * 查询商户入驻 小区信息
     *
     * @param communityMemberDto 小区成员信息
     * @return
     */
    ResponseEntity<String> query(CommunityMemberDto communityMemberDto);
}
